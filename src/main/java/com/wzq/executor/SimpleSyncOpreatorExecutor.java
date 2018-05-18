package com.wzq.executor;

import com.wzq.core.connector.Connector;
import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.generator.Generator;
import com.wzq.core.structure.Attach;
import com.wzq.core.structure.Structure;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.manager.MappingManager;
import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.CoverOpreater;
import com.wzq.sql.structure.MappingAttach;
import com.wzq.sql.structure.MappingStructure;
import com.wzq.sql.structure.TableStructure;
import com.wzq.util.KeyValue;

import java.util.*;

public class SimpleSyncOpreatorExecutor implements SyncOpreatorExecutor {

    public void execute(SyncOpreator syncOpreator, MappingManager mappingManager, Connector connector) {

        MappingAttach ma = canUse(syncOpreator.getAttach());

        List<MappingStructure> mappingStructures = ma.getMappingStructures();
        List<CoverOpreater> coverOpreaters = ma.getCoverOpreaters();

        // 规范化MappingManager
        mappingManager.standardize();
        List<Mapping> mappings = mappingManager.getMappings();
        // 获取必要参数
        Map<String, Generator> ag = new HashMap<String, Generator>();
        Map<String, Mapping> am = new HashMap<String, Mapping>();

        if (ma.isSyncAllMappings()) {
            mappingStructures = new ArrayList<MappingStructure>();
            coverOpreaters = null;
            Set<String> um = new HashSet<String>();
            for (Mapping m : mappings) {
                mappingStructures.add(m.getIMappingStructure());
                if (!um.contains(m.getName())) {
                    um.add(m.getName());
                    ag.put(m.getName(), mappingManager.getGenerator(m.getName()));
                    am.put(m.getName(), m);
                }
            }
            // 判断是否同步结构
            if (ma.isSyncStructure()) {
                // 同步结构
                syncStructure(connector, am, ag);
            }
            for (Map.Entry<String, Mapping> entry : am.entrySet()) {
                MappingStructure ims = entry.getValue().getIMappingStructure();
                List<TableStructure> tables = ims.getTables();
                for (TableStructure ts : tables) {
                    String[] allOtNames = entry.getValue().getAllOtNames(ts.getName());
                    String[] allItColumns = entry.getValue().getAllItColumns(ts.getName(), allOtNames);
                    String[] allItWhereColumns = entry.getValue().getAllItWhereColumns(ts.getName(), allOtNames);
                    Generator generator = ag.get(entry.getKey());
                    // 查询对方数据 必要排序
                    Iterable<Structure> ors = connector.connect(generator.generator(ims.newReverseSelectCommand(ts.getName(), allItColumns, new String[0], allOtNames)));
                    // 查询己方数据 必要排序
                    Iterable<Structure> irs = connector.connect(generator.generator(ims.newSelectCommand(ts.getName(), allItColumns, new String[0], allOtNames)));
                    // 数据顺序对比 差异的存入缓存Target中 -> 超过阀值（己方缓存数 * 对方缓存数）> 200000 -> 整表同步
                    int ori = 0;
                    int iri = 0;
                    Iterator<Structure> oriter = ors.iterator();
                    Iterator<Structure> iriter = irs.iterator();
                    while (oriter.hasNext()) {
                        MappingStructure ms = MappingStructure.cast(oriter.next());
                        // 比较缓存中的数据是否存在 存在则清空此条数据 并将数据更新到己方数据库中
                        Iterable<Structure> ici = connector.cache(generator.generator(ms.newSelectCommand(ts.getName(), allItColumns, allItWhereColumns, allOtNames)));
                        Iterator<Structure> iciiter = ici.iterator();
                        if (iciiter.hasNext()) {
                            // 此条数据存在为更新操作，前面数据为插入操作
                            Structure next = iciiter.next();
                            if (iciiter.hasNext()) {
                                // 重复不唯一的数据，触发整表同步
                                // TODO
                            }
                        } else {
                            // 不存则放入到缓存中去
                            // 判断缓存是否超过阀值，超过触发整表同步
                            // TODO
                        }
                    }
                    // 同步完成 触发视图或连接对象切换

                    // TODO 同步数据
                }
            }
        } else {
            // TODO "暂不支持的功能！"
            throw new RuntimeException("暂不支持的功能！");
        }
    }

    private void syncStructure(Connector connector, Map<String, Mapping> am, Map<String, Generator> ag) {
        // 只同步己方表结构不动对面表结构
        for (Map.Entry<String, Mapping> entry : am.entrySet()) {
            // 获取当前正在使用的Mapping结构
            Structure s = connector.getStructure(connector.getIt(), entry.getValue().getAllItNames());
            // 获取需要变为的Mapping结构
            MappingStructure ims = entry.getValue().getIMappingStructure();
            // 求出差异表结构
            KeyValue<Structure, Structure> sskv = ims.differenceSet(s);
            MappingStructure ms = MappingStructure.cast(sskv.getKey());
            List<TableStructure> tables = ms.getTables();
            // 根据差异表结构执行相关操作
            for (TableStructure ts : tables) {
                if (ts != null && ts.isValidate()) {
                    // 得到需要变为的数据结构
                    TableStructure imts = ims.findTable(ts.getName());
                    String[] allOtNames = entry.getValue().getAllOtNames(imts.getName());
                    Generator generator = ag.get(entry.getKey());
                    // 删除结构变动的表
                    connector.connect(generator.generator(ims.newDropCommand(imts.getName(), allOtNames)));
                    // 得到己方的表的所有字段
                    String[] allItColumns = entry.getValue().getAllItColumns(imts.getName(), allOtNames);
                    // 创建结构变动的表
                    connector.connect(generator.generator(ims.newCreateCommand(imts.getName(), allItColumns, connector.getIt().getDialect(), allOtNames)));
                    // 同步数据到被删除的表中
                    Iterable<Structure> rs = connector.connect(generator.generator(ims.newReverseSelectCommand(imts.getName(), allItColumns, new String[0], allOtNames)));
                    for (Structure r : rs) {
                        MappingStructure msx = MappingStructure.cast(r);
                        MappingStructure imsx = entry.getValue().getIMappingStructure(msx);
                        connector.connect(generator.generator(imsx.newInsertCommand(imts.getName(), allItColumns, allOtNames)));
                    }
                    // 提交全部操作
                    connector.commit();
                }
            }

        }
    }

    private static MappingAttach canUse(Attach attach) {
        MappingAttach ma = null;
        if (attach instanceof MappingAttach) {
            ma = (MappingAttach) attach;
        } else {
            ma = MappingAttach.getInstance();
        }
        return ma;
    }
}

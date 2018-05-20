package com.wzq.executor;

import com.wzq.core.command.Command;
import com.wzq.core.connector.Connector;
import com.wzq.core.context.SyncContext;
import com.wzq.core.context.SyncMappingContext;
import com.wzq.core.context.SyncTableContext;
import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.generator.Generator;
import com.wzq.core.listener.SyncMappingListener;
import com.wzq.core.listener.SyncTableListener;
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
import java.util.logging.Logger;

public class SimpleSyncOpreatorExecutor implements SyncOpreatorExecutor {

    /**
     * 执行同步操作
     */
    public void execute(SyncOpreator syncOpreator, SyncContext syncContext) {

        MappingAttach ma = canUse(syncOpreator.getAttach());
        MappingManager mappingManager = syncContext.getMappingManager();
        Connector connector = syncContext.getConnector();

        List<MappingStructure> mappingStructures = ma.getMappingStructures();
        List<CoverOpreater> coverOpreaters = ma.getCoverOpreaters();

        // 规范化MappingManager
        mappingManager.standardize();
        List<Mapping> mappings = mappingManager.getMappings();
        // 获取必要参数
        Map<String, Generator> ag = new HashMap<String, Generator>();
        Map<String, Mapping> am = new HashMap<String, Mapping>();

        // 全表同步
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
            List<MappingStructure> mss = null;
            // 判断是否同步结构
            if (ma.isSyncStructure()) {
                // 同步结构
                mss = syncStructure(syncOpreator, syncContext, connector, am, ag);
            }
            for (Map.Entry<String, Mapping> entry : am.entrySet()) {
                MappingStructure ims = entry.getValue().getIMappingStructure();
                // 排除已经同步了结构的表
                for (MappingStructure mappingStructure : mss) {
                    if (mappingStructure.getName().equals(ims.getName())) {
                        ims = MappingStructure.cast(ims.differenceSet(mappingStructure).getKey());
                    }
                }
                // 构建Mapping同步的上下文
                SyncMappingContext syncMappingContext = new SyncMappingContext(syncContext, entry.getValue(), ims);
                // 触发同步Mapping前置的上下文
                handlerSyncMappingListener(syncOpreator, syncMappingContext, true);
                List<TableStructure> tables = ims.getTables();
                for (TableStructure ts : tables) {
                    // 构建TableMapping同步的上下文
                    SyncTableContext syncTableContext = new SyncTableContext(syncMappingContext, entry.getValue().findByItName(ts.getName()), ts);
                    // 触发同步TableMapping前置的上下文
                    handlerSyncTableListener(syncOpreator, syncTableContext, true);
                    syncIncAndDec(entry.getValue(), ts, ag.get(entry.getKey()), connector, ims, syncOpreator);
                    connector.commit();
                    // 触发同步TableMapping后置的上下文
                    handlerSyncTableListener(syncOpreator, syncTableContext, false);
                }
                connector.commit();
                // 触发同步Mapping后置的上下文
                handlerSyncMappingListener(syncOpreator, syncMappingContext, false);
            }
            //其它同步
        } else {
            // TODO "暂不支持的功能！"
            throw new RuntimeException("暂不支持的功能！");
        }
        // 提交操作
        connector.commit();
    }

    private void handlerSyncTableListener(SyncOpreator syncOpreator, SyncTableContext syncTableContext, boolean pa) {
        List<SyncTableListener> syncTableListener = syncTableContext.getSyncTableListener();
        for (SyncTableListener tableListener : syncTableListener) {
            if (tableListener != null) {
                if (pa) {
                    tableListener.preSync(syncOpreator, syncTableContext);
                } else {
                    tableListener.afterSync(syncOpreator, syncTableContext);
                }
            }
        }
    }

    private void handlerSyncMappingListener(SyncOpreator syncOpreator, SyncMappingContext syncMappingContext, boolean pa) {
        List<SyncMappingListener> syncMappingListener = syncMappingContext.getSyncMappingListener();
        for (SyncMappingListener mappingListener : syncMappingListener) {
            if (mappingListener != null) {
                if (pa) {
                    mappingListener.preSync(syncOpreator, syncMappingContext);
                } else {
                    mappingListener.afterSync(syncOpreator, syncMappingContext);
                }
            }
        }
    }

    /**
     * 同步表的增减量数据，一定情况下会触发整表同步：没有唯一标识，唯一标识存在重复
     * @param mapping
     * @param ts
     * @param generator
     * @param connector
     * @param ims
     * @param syncOpreator
     */
    private void syncIncAndDec(Mapping mapping, TableStructure ts, Generator generator, Connector connector, MappingStructure ims, SyncOpreator syncOpreator) {
        String[] allOtNames = mapping.getAllOtNames(ts.getName());
        String[] allItColumns = mapping.getAllItColumns(ts.getName(), allOtNames);
        String[] allItWhereColumns = mapping.getAllItWhereColumns(ts.getName(), allOtNames);
        // 判断是否拥有唯一标识
        if (allItWhereColumns == null || allItWhereColumns.length <= 0) {
            // 没有唯一标识的表，整表同步
            syncReTableAndStructure(ts, ims, mapping, generator, connector);
            return;
        }
        // 查询对方数据 必要排序
        Iterable<Structure> ors = connector.connect(ims.newReverseSelectCommand(generator, ts.getName(), allItColumns, new String[0], allOtNames));
        // 查询己方数据 必要排序
        Iterable<Structure> irs = connector.connect(ims.newSelectCommand(generator, ts.getName(), allItColumns, new String[0], allOtNames));
        // 数据顺序对比 差异的存入缓存Target中 -> 超过阀值（己方缓存数 * 对方缓存数）> 200000 -> 整表同步

        // 删除以前的缓存 缓存格式都使用己方结构
        Command dc = ims.newDropCommand(generator, ts.getName(), allOtNames);
        connector.cacheI(dc);
        connector.cacheO(dc);
        // 创建两个缓存 缓存格式都使用己方结构
        Command cc = ims.newCreateCommand(generator, ts.getName(), allItColumns, connector.getCacheIt().getDialect(), allOtNames);
        connector.cacheI(cc);
        connector.cacheO(cc);

        long cacheICount = 0L;
        long cacheOCount = 0L;

        Iterator<Structure> oriter = ors.iterator();
        Iterator<Structure> iriter = irs.iterator();
        while (oriter.hasNext() || iriter.hasNext()) {
            // 对方转换成己方的
            MappingStructure omsx = null;
            // 己方的
            MappingStructure imsx = null;

            if (oriter.hasNext()) {
                omsx = mapping.getIMappingStructure(MappingStructure.cast(oriter.next()));
            }
            if (iriter.hasNext()) {
                omsx = MappingStructure.cast(iriter.next());
            }

            if (omsx != null) {
                if (imsx != null) {
                    connector.cacheO(omsx.newInsertCommand(generator, ts.getName(), allItColumns, allOtNames));
                    cacheOCount++; // 缓存个数加1
                }
            }

            if (imsx != null) {
                if (omsx != null) {
                    connector.cacheI(imsx.newInsertCommand(generator, ts.getName(), allItColumns, allOtNames));
                    cacheICount++; // 缓存个数加1
                }
            }

            if (omsx != null) {
                Iterable<Structure> cs = connector.cacheI(omsx.newSelectCommand(generator, ts.getName(), allItColumns, allItWhereColumns, allOtNames));
                Iterator<Structure> citer = cs.iterator();
                if (citer.hasNext()) {
                    MappingStructure cast = MappingStructure.cast(citer.next());
                    if (citer.hasNext()) {
                        // 数据不唯一触发整表同步
                        syncReTableAndStructure(ts, cast, mapping, generator, connector);
                    }
                    // 查询到相同数据了
                    // 己方缓存数据到此条的之前数据执行真实删除操作 清空操作过的数据
                    Iterable<Structure> csx = connector.cacheI(omsx.newSelectCommand(generator, ts.getName(), allItColumns, new String[0], allOtNames));
                    Iterator<Structure> citerx = csx.iterator();
                    while (citerx.hasNext()) {
                        MappingStructure msc = MappingStructure.cast(citerx.next());
                        // 判断是否到了查询到的位置了
                        if (cast.equals(msc)) {
                            break;
                        } else {
                            Command dec = msc.newDeleteCommand(generator, ts.getName(), allItWhereColumns, allOtNames);
                            // 删除真实库
                            connector.connect(dec);
                            // 删除缓存库
                            connector.cacheI(dec);
                            // 缓存减一
                            cacheICount --;
                        }
                    }
                    // 对方缓存数据全部是执行插入操作 清空对方缓存数据
                    Iterable<Structure> ocs = connector.cacheO(omsx.newSelectCommand(generator, ts.getName(), allItColumns, new String[0], allOtNames));
                    Iterator<Structure> ociter = ocs.iterator();
                    while (ociter.hasNext()) {
                        MappingStructure msc = MappingStructure.cast(ociter.next());
                        connector.connect(msc.newInsertCommand(generator, ts.getName(), allItColumns, allOtNames));
                    }
                    // 清空此缓存表
                    clearCacheTable(generator, ts, omsx, connector, allItColumns, allOtNames, false);
                    cacheOCount = 0L; // 缓存重设为0
                    // 比较此条数据是否全等 若不是，此条数据更新操作
                    if (!omsx.equals(cast)) {
                        connector.connect(omsx.newUpdateCommand(generator, ts.getName(), allItColumns, allItWhereColumns, allOtNames));
                    }
                    continue;
                }
            }

            if (imsx != null) {
                Iterable<Structure> cs = connector.cacheO(imsx.newSelectCommand(generator, ts.getName(), allItColumns, allItWhereColumns, allOtNames));
                Iterator<Structure> citer = cs.iterator();
                if (citer.hasNext()) {
                    MappingStructure cast = MappingStructure.cast(citer.next());
                    if (citer.hasNext()) {
                        // 数据不唯一触发整表同步
                        syncReTableAndStructure(ts, cast, mapping, generator, connector);
                        break;
                    }

                    // 查询到相同数据了
                    // 对方缓存数据到此条的之前数据执行真实插入操作 清空操作过的数据
                    Iterable<Structure> csx = connector.cacheO(imsx.newSelectCommand(generator, ts.getName(), allItColumns, new String[0], allOtNames));
                    Iterator<Structure> citerx = csx.iterator();
                    while (citerx.hasNext()) {
                        MappingStructure msc = MappingStructure.cast(citerx.next());
                        // 判断是否到了查询到的位置了
                        if (cast.equals(msc)) {
                            break;
                        } else {
                            Command dec = msc.newDeleteCommand(generator, ts.getName(), allItWhereColumns, allOtNames);
                            Command isc = msc.newInsertCommand(generator, ts.getName(), allItColumns, allOtNames);
                            // 插入真实库
                            connector.connect(isc);
                            // 删除缓存库
                            connector.cacheO(dec);
                            // 缓存减一
                            cacheOCount --;
                        }
                    }
                    // 己方缓存数据全部是执行删除操作 清空己方缓存数据
                    Iterable<Structure> ics = connector.cacheI(imsx.newSelectCommand(generator, ts.getName(), allItColumns, new String[0], allOtNames));
                    Iterator<Structure> iciter = ics.iterator();
                    while (iciter.hasNext()) {
                        MappingStructure msc = MappingStructure.cast(iciter.next());
                        connector.connect(msc.newDeleteCommand(generator, ts.getName(), allItWhereColumns, allOtNames));
                    }
                    // 清空此缓存表
                    clearCacheTable(generator, ts, imsx, connector, allItColumns, allOtNames, true);
                    cacheICount = 0L; // 缓存重设为0
                    // 比较此条数据是否全等 若不是，此条数据更新操作
                    if (!omsx.equals(cast)) {
                        connector.connect(omsx.newUpdateCommand(generator, ts.getName(), allItColumns, allItWhereColumns, allOtNames));
                    }
                    continue;
                }
            }

            // 判断是否到了缓存阀值
            if ((cacheICount * cacheOCount) >= syncOpreator.getCacheMaxCount()) {
                // 超过缓存阀值 执行整表同步
                syncReTableAndStructure(ts, ims, mapping, generator, connector);
                break;
            }
        }
    }

    /**
     * 同步表结构
     * @param connector
     * @param am
     * @param ag
     * @return
     */
    private List<MappingStructure> syncStructure(SyncOpreator syncOpreator, SyncContext syncContext, Connector connector, Map<String, Mapping> am, Map<String, Generator> ag) {
        List<MappingStructure> mss = new ArrayList<MappingStructure>();
        // 只同步己方表结构不动对面表结构
        for (Map.Entry<String, Mapping> entry : am.entrySet()) {
            // 获取当前正在使用的Mapping结构
            Structure s = connector.getStructure(connector.getIt(), entry.getValue(), entry.getValue().getAllItNames());
            // 获取需要变为的Mapping结构
            MappingStructure ims = entry.getValue().getIMappingStructure();
            // 求出差异表结构
            KeyValue<Structure, Structure> sskv = ims.differenceSet(s);
            MappingStructure ms = MappingStructure.cast(sskv.getKey());
            if (ms != null) {
                // 添加差异集合
                mss.add(ms);
                ms = (MappingStructure) ms.clone();
                // 判断是否同步了全部的Mapping 如果是将触发同步事件
                SyncMappingContext syncMappingContext = null;
                if (ms.equals(ims)) {
                    // 构建了Mapping上下文
                    syncMappingContext = new SyncMappingContext(syncContext, entry.getValue(), ms);
                    // 触发同步Mapping前置监听器
                    handlerSyncMappingListener(syncOpreator, syncMappingContext, true);
                }
                List<TableStructure> tables = ms.getTables();
                // 根据差异表结构执行相关操作
                for (TableStructure ts : tables) {
                    // 构建了TableMapping上下文
                    SyncTableContext syncTableContext = new SyncTableContext(syncMappingContext, entry.getValue().findByItName(ts.getName()), ts);
                    // 触发同步TableMapping前置监听器
                    handlerSyncTableListener(syncOpreator, syncTableContext, true);
                    syncReTableAndStructure(ts, ims, entry.getValue(), ag.get(entry.getKey()), connector);
                    connector.commit();
                    // 触发同步TableMapping后置监听器
                    handlerSyncTableListener(syncOpreator, syncTableContext, false);
                }
                if (syncMappingContext != null)  {
                    connector.commit();
                    // 触发同步Mapping后置监听器
                    handlerSyncMappingListener(syncOpreator, syncMappingContext, false);
                }
            }
        }
        return mss;
    }

    /**
     * 清空缓存表 真实操作先删除表再创建表
     * @param ts
     * @param ms
     * @param connector
     * @param allItColumns
     * @param allOtNames
     * @param iot
     */
    private void clearCacheTable(Generator generator, TableStructure ts, MappingStructure ms, Connector connector, String[] allItColumns, String[] allOtNames, boolean iot) {
        // 删除以前的缓存 缓存格式都使用己方结构
        Command dc = ms.newDropCommand(generator, ts.getName(), allOtNames);
        // 创建两个缓存 缓存格式都使用己方结构
        Command cc = ms.newCreateCommand(generator, ts.getName(), allItColumns, connector.getCacheIt().getDialect(), allOtNames);
        if (iot) {
            // 删除表
            connector.cacheI(dc);
            connector.cacheI(cc);
        } else {
            // 创建表
            connector.cacheO(dc);
            connector.cacheO(cc);
        }
    }

    /**
     * 重新同步表结构和数据
     * @param ts
     * @param ims
     * @param mapping
     * @param generator
     * @param connector
     */
    private void syncReTableAndStructure(TableStructure ts, MappingStructure ims, Mapping mapping, Generator generator, Connector connector) {
        if (ts != null && ts.isValidate()) {
            // 得到需要变为的数据结构
            TableStructure imts = ims.findTable(ts.getName());
            String[] allOtNames = mapping.getAllOtNames(imts.getName());
            // 删除结构变动的表
            try {
                connector.connect(ims.newDropCommand(generator, imts.getName(), allOtNames));
            } catch (Exception e) {}
            // 得到己方的表的所有字段
            String[] allItColumns = mapping.getAllItColumns(imts.getName(), allOtNames);
            // 创建结构变动的表
            connector.connect(ims.newCreateCommand(generator, imts.getName(), allItColumns, connector.getIt().getDialect(), allOtNames));
            // 同步整张表
            syncTable(connector, generator, mapping, ims, imts, allItColumns, allOtNames);
        }
    }

    /**
     * 同步表数据
     * @param connector
     * @param generator
     * @param mapping
     * @param ims
     * @param imts
     * @param allItColumns
     * @param allOtNames
     */
    private void syncTable(Connector connector, Generator generator, Mapping mapping, MappingStructure ims, TableStructure imts, String[] allItColumns, String[] allOtNames) {
        Iterable<Structure> rs = connector.connect(ims.newReverseSelectCommand(generator, imts.getName(), allItColumns, new String[0], allOtNames));
        for (Structure r : rs) {
            MappingStructure msx = MappingStructure.cast(r);
            MappingStructure imsx = mapping.getIMappingStructure(msx);
            connector.connect(imsx.newInsertCommand(generator, imts.getName(), allItColumns, allOtNames));
        }
    }

    /**
     *  如果附加不满足则获取默认的
     * @param attach
     * @return
     */
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

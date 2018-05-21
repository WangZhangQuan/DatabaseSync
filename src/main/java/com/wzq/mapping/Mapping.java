package com.wzq.mapping;

import com.wzq.able.SwapBothSidesAble;
import com.wzq.sql.structure.ColumnStructure;
import com.wzq.sql.structure.DownTableRelation;
import com.wzq.sql.structure.MappingStructure;
import com.wzq.sql.structure.TableStructure;
import com.wzq.util.KeyValue;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import java.io.Serializable;
import java.util.*;

/**
 * 数据库映射关系
 */
public class Mapping implements SwapBothSidesAble, Cloneable, Serializable {
    public Mapping() {
    }

    public Mapping(String name) {
        this.name = name;
    }

    public Mapping(String name, List<TableMapping> tableMaps) {
        this.name = name;
        this.tableMaps = tableMaps;
    }

    public static final String SWAP_PREFIX = "SWAP@";
    public static final String FUSION_LINK = "&";
    public static final String UNCOUPLE_LINK = "<<";

    /**
     * 映射名称
     */
    private String name = "Unknow";
    /**
     * 表映射关系
     */
    private List<TableMapping> tableMaps;

    /**
     * 己方主导表
     */
    private String mainIt;

    /**
     * 对方主导表
     */
    private String mainOt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableMapping> getTableMaps() {
        return tableMaps;
    }

    public void setTableMaps(List<TableMapping> tableMaps) {
        this.tableMaps = tableMaps;
    }

    /**
     * 生成一个双方交换位置的Mapping
     *
     * @return
     */
    public Mapping generateSwapBothSides() {
        Mapping mapping = new Mapping();
        mapping.name = swapName();
        mapping.mainIt = mainOt;
        mapping.mainOt = mainIt;
        List<TableMapping> tms = new ArrayList<TableMapping>();
        for (TableMapping tm : this.tableMaps) {
            tms.add(tm.generateSwapBothSides());
        }
        mapping.tableMaps = tms;
        return mapping;
    }

    private String swapName() {
        if (name.startsWith(SWAP_PREFIX)) {
            return name.substring(SWAP_PREFIX.length());
        }
        return name;
    }

    /**
     * 融合 后面的数据差异 后面覆盖前面
     * 影响字段 pt，whereIc，whereOc
     * 主导表取this表的
     * @param mapping
     * @return
     */
    public Mapping fusion(Mapping mapping) {
        Mapping m = (Mapping) clone();
        mapping = (Mapping) mapping.clone();
        if (mapping != null) {
            m.name = fusionName(mapping.getName());
            Map<String, TableMapping> um = new HashMap<String, TableMapping>();
            fusionByMap(m, um);
            fusionByMap(mapping, um);
            m.tableMaps = new ArrayList<TableMapping>(um.values());
        }
        return m;
    }

    private void fusionByMap(Mapping m, Map<String, TableMapping> um) {
        for (TableMapping mtm : m.tableMaps) {
            String s = mtm.getIt() + FUSION_LINK + mtm.getOt();
            if (um.containsKey(s)) {
                TableMapping tmx = um.get(s);
                tmx.fusion(mtm);
                mtm = tmx;
            }
            um.put(s, mtm);
        }
    }

    private String fusionName(String mappingName) {
        return name + FUSION_LINK + mappingName;
    }

    /**
     * 通过itName获取tableMapping
     *
     * @param itName
     * @return
     */
    public List<TableMapping> findByItName(String itName) {
        List<TableMapping> tms = new ArrayList<TableMapping>();
        for (TableMapping tableMap : tableMaps) {
            if (tableMap.getIt().equals(itName)) {
                tms.add(tableMap);
            }
        }
        return tms;
    }

    /**
     * 通过otName获取tableMapping
     *
     * @param otName
     * @return
     */
    public List<TableMapping> findByOtName(String otName) {
        List<TableMapping> tms = new ArrayList<TableMapping>();
        for (TableMapping tableMap : tableMaps) {
            if (tableMap.getOt().equals(otName)) {
                tms.add(tableMap);
            }
        }
        return tms;
    }

    /**
     * 获取与己方表相关联的otNames
     *
     * @param itName
     * @return
     */
    public String[] getAllOtNames(String itName) {
        List<String> otns = new ArrayList<String>();
        List<TableMapping> tms = findByItName(itName);
        for (TableMapping tm : tms) {
            otns.add(tm.getOt());
        }
        return otns.toArray(new String[otns.size()]);
    }

    /**
     * 通过itName和OtNames获取多个tableMapping
     *
     * @param itName
     * @param otNames
     * @return
     */
    public List<TableMapping> findByItNameAndOtNames(String itName, String... otNames) {
        List<TableMapping> tms = new ArrayList<TableMapping>();
        for (TableMapping tm : tableMaps) {
            if (tm.getIt().equals(itName)) {
                for (String otName : otNames) {
                    if (tm.getOt().equals(otName)) {
                        tms.add(tm);
                        break;
                    }
                }
            }
        }
        return tms;
    }

    /**
     * 获取能过滤的己方字段
     *
     * @param itName
     * @param otNames
     * @return
     */
    public String[] getAllItWhereColumns(String itName, String... otNames) {
        List<String> cmns = new ArrayList<String>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (TableMapping tm : tms) {
            cmns.addAll(tm.getAllIcNames(true, null));
        }
        return cmns.toArray(new String[cmns.size()]);
    }

    /**
     * 获取能过滤的己方字段 去重
     *
     * @param itName
     * @param otNames
     * @return
     */
    public String[] getAllUniqueItWhereColumns(String itName, String... otNames) {
        return unique(getAllItWhereColumns(itName, otNames));
    }

    /**
     * 获取能影响的己方字段
     *
     * @param itName
     * @param otNames
     * @return
     */
    public String[] getAllItColumns(String itName, String... otNames) {
        List<String> cmns = new ArrayList<String>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (TableMapping tm : tms) {
            cmns.addAll(tm.getAllIcNames());
        }
        return cmns.toArray(new String[cmns.size()]);
    }

    /**
     * 获取能影响的己方字段 去重
     *
     * @param itName
     * @param otNames
     * @return
     */
    public String[] getAllUniqueItColumns(String itName, String... otNames) {
        return unique(getAllItColumns(itName, otNames));
    }

    /**
     * 获取能过滤的他方字段
     *
     * @param itName
     * @param otNames
     * @return
     */
    public Map<String, String[]> getAllOtWhereColumns(String itName, String... otNames) {
        Map<String, String[]> m = new HashMap<String, String[]>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (String otName : otNames) {
            List<String> cmns = new ArrayList<String>();
            for (TableMapping tm : tms) {
                if (tm.getOt().equals(otName)) {
                    cmns.addAll(tm.getAllOcNames(null, true));
                }
            }
            m.put(otName, cmns.toArray(new String[cmns.size()]));
        }
        return m;
    }

    /**
     * 获取能过滤的他方字段 去重
     *
     * @param itName
     * @param otNames
     * @return
     */
    public Map<String, String[]> getAllUniqueOtWhereColumns(String itName, String... otNames) {
        Map<String, String[]> aowcs = getAllOtWhereColumns(itName, otNames);
        for (String s : aowcs.keySet()) {
            aowcs.put(s, unique(aowcs.get(s)));
        }
        return aowcs;
    }

    /**
     * 获取能影响的他方字段
     *
     * @param itName
     * @param otNames
     * @return
     */
    public Map<String, String[]> getAllOtColumns(String itName, String... otNames) {
        Map<String, String[]> m = new HashMap<String, String[]>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (String otName : otNames) {
            List<String> cmns = new ArrayList<String>();
            for (TableMapping tm : tms) {
                if (tm.getOt().equals(otName)) {
                    cmns.addAll(tm.getAllOcNames());
                }
            }
            m.put(otName, cmns.toArray(new String[cmns.size()]));
        }
        return m;
    }

    /**
     * 获取能影响的他方字段 去重
     *
     * @param itName
     * @param otNames
     * @return
     */
    public Map<String, String[]> getAllUniqueOtColumns(String itName, String... otNames) {
        Map<String, String[]> aocs = getAllOtColumns(itName, otNames);
        for (String s : aocs.keySet()) {
            aocs.put(s, unique(aocs.get(s)));
        }
        return aocs;
    }

    /**
     * 获取对面的字段名称
     *
     * @param itName
     * @param itColumnNames
     * @param otNames
     * @return
     */
    public Map<String, String[]> getOtColumns(String itName, String[] itColumnNames, String... otNames) {
        Map<String, String[]> m = new HashMap<String, String[]>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (String otName : otNames) {
            List<String> cmns = new ArrayList<String>();
            for (TableMapping tm : tms) {
                if (tm.getOt().equals(otName)) {
                    cmns.addAll(tm.getOcNames(itColumnNames));
                }
            }
            m.put(otName, cmns.toArray(new String[cmns.size()]));
        }
        return m;
    }

    /**
     * 获取对面的字段关系
     *
     * @param itName
     * @param itColumnNames
     * @param otNames
     * @return
     */
    public Map<String, ColumnMapping[]> getOtColumnMappings(String itName, String[] itColumnNames, String... otNames) {
        Map<String, ColumnMapping[]> m = new HashMap<String, ColumnMapping[]>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (String otName : otNames) {
            List<ColumnMapping> cmns = new ArrayList<ColumnMapping>();
            for (TableMapping tm : tms) {
                if (tm.getOt().equals(otName)) {
                    cmns.addAll(tm.getColumnMaps());
                }
            }
            m.put(otName, cmns.toArray(new ColumnMapping[cmns.size()]));
        }
        return m;
    }

    /**
     * 获取对面的字段名称 去重
     *
     * @param itName
     * @param itColumnNames
     * @param otNames
     * @return
     */
    public Map<String, String[]> getUniqueOtColumns(String itName, String[] itColumnNames, String... otNames) {
        Map<String, String[]> otColumns = getOtColumns(itName, itColumnNames, otNames);
        for (String s : otColumns.keySet()) {
            otColumns.put(s, unique(otColumns.get(s)));
        }
        return otColumns;
    }

    /**
     * 获取对面的过滤字段
     *
     * @param itName
     * @param itWhereColumnNames
     * @param otNames
     * @return
     */
    public Map<String, String[]> getOtWhereColumns(String itName, String[] itWhereColumnNames, String... otNames) {
        Map<String, String[]> m = new HashMap<String, String[]>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (String otName : otNames) {
            List<String> cmns = new ArrayList<String>();
            for (TableMapping tm : tms) {
                if (tm.getOt().equals(otName)) {
                    cmns.addAll(tm.getOcNames(null, true, itWhereColumnNames));
                }
            }
            m.put(otName, cmns.toArray(new String[cmns.size()]));
        }
        return m;
    }

    /**
     * 获取对面的过滤字段
     *
     * @param itName
     * @param itWhereColumnNames
     * @param otNames
     * @return
     */
    public Map<String, ColumnMapping[]> getOtWhereMappingColumns(String itName, String[] itWhereColumnNames, String... otNames) {
        Map<String, ColumnMapping[]> m = new HashMap<String, ColumnMapping[]>();
        List<TableMapping> tms = findByItNameAndOtNames(itName, otNames);
        for (String otName : otNames) {
            List<ColumnMapping> cmns = new ArrayList<ColumnMapping>();
            for (TableMapping tm : tms) {
                if (tm.getOt().equals(otName)) {
                    cmns.addAll(tm.getOcMappings(null, true, itWhereColumnNames));
                }
            }
            m.put(otName, cmns.toArray(new ColumnMapping[cmns.size()]));
        }
        return m;
    }

    /**
     * 获取对面的过滤字段 去重
     *
     * @param itName
     * @param itWhereColumnNames
     * @param otNames
     * @return
     */
    public Map<String, String[]> getUniqueOtWhereColumns(String itName, String[] itWhereColumnNames, String... otNames) {
        Map<String, String[]> oWheretColumns = getOtWhereColumns(itName, itWhereColumnNames, otNames);
        for (String s : oWheretColumns.keySet()) {
            oWheretColumns.put(s, unique(oWheretColumns.get(s)));
        }
        return oWheretColumns;
    }

    public MappingStructure getIMappingStructure(MappingStructure omStructure) {
        return getMappingStructure(omStructure, true);
    }

    public MappingStructure getOMappingStructure(MappingStructure imStructure) {
        return getMappingStructure(imStructure, false);
    }

    private MappingStructure getMappingStructure(MappingStructure structure, boolean iot) {
        MappingStructure ms = null;
        if (name.equals(structure.getName())) {
            ms = new MappingStructure(this);
            List<TableStructure> tss = structure.getTables();
            List<TableStructure> tssx = new ArrayList<TableStructure>();
            for (TableStructure ts : tss) {
                List<TableMapping> tms = findByItName(ts.getName());
                for (TableMapping tm : tms) {
                    tssx.add(iot ? tm.getItStructure(ts) : tm.getOtStructure(ts));
                }
            }
            ms.setTables(tssx);
            ms.standardize();
        }
        return ms;
    }


    private boolean isValidate() {
        if (tableMaps != null && tableMaps.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public MappingStructure getIMappingStructure() {
        return getStructure(true);
    }

    public MappingStructure getOMappingStructure() {
        return getStructure(false);
    }

    private MappingStructure getStructure(boolean iot) {
        MappingStructure ms = new MappingStructure(this);
        List<TableStructure> tss = new ArrayList<TableStructure>();
        for (TableMapping tm : tableMaps) {
            if (iot) {
                tss.add(tm.getIStructure());
            } else {
                tss.add(tm.getOStructure());
            }
        }
        ms.setTables(tss);
        ms.standardize();
        return ms;
    }

    public String[] getAllItNames() {
        if (isValidate()) {
            Set<String> tns = new HashSet<String>();
            for (TableMapping tm : tableMaps) {
                tns.add(tm.getIt());
            }
            return tns.toArray(new String[tns.size()]);
        }
        return new String[0];
    }

    private static List<TableStructure> findDownITableStructures(Mapping mapping, String itName) {
        MappingStructure ims = mapping.getIMappingStructure();
        String[] allItWhereColumns = mapping.getAllItWhereColumns(itName, mapping.getAllOtNames(itName));
        List<TableStructure> tss = new ArrayList<TableStructure>();
        for (TableStructure ts : ims.getTables()) {
            boolean f = false;
            for (ColumnStructure cs : ts.getColumns()) {
                for (String aiwc : allItWhereColumns) {
                    if (cs.getName().equals(aiwc)) {
                        tss.add(ts);
                        f = true;
                        break;
                    }
                }
                if (f) {
                    break;
                }
            }
        }
        return tss;
    }

    public List<TableStructure> findDownItTableStructures(String otName) {
        return findDownITableStructures(this, otName);
    }

    public List<TableStructure> findDownOtTableStructures(String otName) {
        return findDownITableStructures(generateSwapBothSides(), otName);
    }

    public static void resetIDownTableRelation(Mapping mapping, DownTableRelation dtr) {
        dtr.clearDowns();
        List<TableStructure> dits = findDownITableStructures(mapping, dtr.getTs().getName());
        for (TableStructure dit : dits) {
            DownTableRelation ddtr = new DownTableRelation(dit);
            if (dtr.validateAdd(ddtr)) {
                resetIDownTableRelation(mapping, ddtr);
            }
        }
    }

    public DownTableRelation getIDownTableRelation(String itName) {
        DownTableRelation dtr = new DownTableRelation(getIMappingStructure().findTable(itName));
        resetIDownTableRelation(this, dtr);
        return dtr;
    }

    public DownTableRelation getODownTableRelation(String otName) {
        Mapping mapping = generateSwapBothSides();
        DownTableRelation dtr = new DownTableRelation(mapping.getIMappingStructure().findTable(otName));
        resetIDownTableRelation(mapping, dtr);
        return dtr;
    }


    private static boolean validateTableName(String name) {
        if (name != null && !"".equals(name)) {
            return true;
        } else
            return false;
    }

    private static String[] unique(String[] strs) {
        Set<String> ss = new HashSet<String>();
        for (String str : strs) {
            ss.add(str);
        }
        return ss.toArray(new String[ss.size()]);
    }

    /**
     * 将多个mapping合并
     *
     * @param mappings
     * @return
     */
    public static Mapping fusionMappings(Mapping... mappings) {
        if (mappings != null && mappings.length > 0) {
            Mapping mapping = mappings[0];
            for (int i = 1; i < mappings.length; ++i) {
                mapping = mapping.fusion(mappings[i]);
            }
            return mapping;
        }
        return null;
    }

    /**
     * 将解分成多个mapping
     *
     * @param tNamess [{it, ot}]
     * @return
     */
    public static Mapping[] uncoupleMappings(Mapping mapping, KeyValue<String, String>[]... tNamess) {
        Mapping[] ms = new Mapping[tNamess.length];
        for (int i = 0; i < ms.length; ++i) {
            ms[i] = new Mapping();
            ms[i].setName(uncoupleName(mapping, tNamess[i]));
            ms[i].setTableMaps(new ArrayList<TableMapping>());
        }
        for (int i = 0; i < tNamess.length; ++i) {
            KeyValue<String, String>[] ns = tNamess[i];
            for (KeyValue<String, String> n : ns) {
                for (TableMapping tableMap : mapping.tableMaps) {
                    if (tableMap.getIt().equals(n.getKey()) && tableMap.getOt().equals(n.getValue())) {
                        ms[i].getTableMaps().add((TableMapping) tableMap.clone());
                    }
                }
            }
        }
        return ms;
    }

    private static String uncoupleName(Mapping mapping, KeyValue<String, String>[] kvs) {
        StringBuffer buf = new StringBuffer();
        for (KeyValue<String, String> kv : kvs) {
            buf
                    .append("[")
                    .append(kv.getKey())
                    .append(FUSION_LINK)
                    .append(kv.getValue())
                    .append("]");
        }
        return mapping.getName() + UNCOUPLE_LINK + buf.toString();
    }

    public static Mapping parseJson(JSONObject json) {
        return JSONValue.parse(JSONValue.toJSONString(json), Mapping.class);
    }

    @Override
    protected Object clone() {
        Mapping m = new Mapping();
        m.name = name;
        m.mainOt = mainOt;
        m.mainIt = mainIt;
        List<TableMapping> tms = new ArrayList<TableMapping>();
        for (TableMapping tm : this.tableMaps) {
            tms.add((TableMapping) tm.clone());
        }
        m.tableMaps = tms;
        return m;
    }

    public String getMainIt() {
        return mainIt;
    }

    public void setMainIt(String mainIt) {
        this.mainIt = mainIt;
    }

    public String getMainOt() {
        return mainOt;
    }

    public void setMainOt(String mainOt) {
        this.mainOt = mainOt;
    }
}

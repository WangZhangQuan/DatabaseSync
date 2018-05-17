package com.wzq.mapping;

import com.wzq.able.SwapBothSidesAble;
import com.wzq.sql.structure.ColumnStructure;
import com.wzq.sql.structure.TableStructure;

import java.util.*;

/**
 * TableMapping 数据库表字段映射类
 */
public class TableMapping implements SwapBothSidesAble, Cloneable {
    public TableMapping(String it, String ot, List<ColumnMapping> columnMaps) {
        this.it = it;
        this.ot = ot;
        this.columnMaps = columnMaps;
    }

    public TableMapping() {
    }

    public TableMapping(String it, String ot) {

        this.it = it;
        this.ot = ot;
    }

    /**
     * 己方表
     */
    private String it;
    /**
     * 他方表
     */
    private String ot;
    /**
     * 字段映射关系
     */
    private List<ColumnMapping> columnMaps;

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    public List<ColumnMapping> getColumnMaps() {
        return columnMaps;
    }

    public void setColumnMaps(List<ColumnMapping> columnMaps) {
        this.columnMaps = columnMaps;
    }

    /**
     * 返回全部的ic名称
     *
     * @return
     */
    public List<String> getAllIcNames() {
        List<String> cms = new ArrayList<String>();
        for (ColumnMapping cm : columnMaps) {
            cms.add(cm.getIc());
        }
        return cms;
    }

    /**
     * 去重版
     * 返回全部的ic名称
     *
     * @return
     */
    public Set<String> getAllUniqueIcNames() {
        Set<String> cms = new HashSet<String>();
        for (ColumnMapping cm : columnMaps) {
            cms.add(cm.getIc());
        }
        return cms;
    }

    /**
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public List<String> getAllIcNames(Boolean whereIc, Boolean whereOc) {
        List<String> cms = new ArrayList<String>();
        for (ColumnMapping cm : columnMaps) {
            cm = filter(cm, whereIc, whereOc);
            if (cm != null) {
                cms.add(cm.getIc());
            }
        }
        return cms;
    }

    /**
     * 去重版
     *
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public Set<String> getAllUniqueIcNames(Boolean whereIc, Boolean whereOc) {
        Set<String> cms = new HashSet<String>();
        for (ColumnMapping cm : columnMaps) {
            cm = filter(cm, whereIc, whereOc);
            if (cm != null) {
                cms.add(cm.getIc());
            }
        }
        return cms;
    }

    /**
     * 返回全部的oc名称
     *
     * @return
     */
    public List<String> getAllOcNames() {
        List<String> cms = new ArrayList<String>();
        for (ColumnMapping cm : columnMaps) {
            cms.add(cm.getOc());
        }
        return cms;
    }

    /**
     * 通过icNames获得ocNames
     * @param icNames
     * @return
     */
    public List<String> getOcNames(String... icNames) {
        List<String> cms = new ArrayList<String>();
        for (ColumnMapping cm : columnMaps) {
            for (String icName : icNames) {
                if (cm.getIc().equals(icName)) {
                    cms.add(cm.getOc());
                }
            }
        }
        return cms;
    }

    /**
     * 去重版
     * 通过icNames获得ocNames
     * @param icNames
     * @return
     */
    public Set<String> getUniqueOcNames(String... icNames) {
        Set<String> cms = new HashSet<String>();
        for (ColumnMapping cm : columnMaps) {
            for (String icName : icNames) {
                if (cm.getIc().equals(icName)) {
                    cms.add(cm.getOc());
                }
            }
        }
        return cms;
    }

    /**
     * 去重版
     * 返回全部的oc名称
     *
     * @return
     */
    public Set<String> getAllUniqueOcNames() {
        Set<String> cms = new HashSet<String>();
        for (ColumnMapping cm : columnMaps) {
            cms.add(cm.getOc());
        }
        return cms;
    }

    /**
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public List<String> getAllOcNames(Boolean whereIc, Boolean whereOc) {
        List<String> cms = new ArrayList<String>();
        for (ColumnMapping cm : columnMaps) {
            cm = filter(cm, whereIc, whereOc);
            if (cm != null) {
                cms.add(cm.getOc());
            }
        }
        return cms;
    }

    /**
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public List<String> getOcNames(Boolean whereIc, Boolean whereOc, String... icNames) {
        List<String> cms = new ArrayList<String>();
        for (ColumnMapping cm : columnMaps) {
            cm = filter(cm, whereIc, whereOc);
            if (cm != null) {
                for (String icName : icNames) {
                    if (cm.getIc().equals(icName)) {
                        cms.add(cm.getOc());
                    }
                }
            }
        }
        return cms;
    }

    /**
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public List<ColumnMapping> getOcMappings(Boolean whereIc, Boolean whereOc, String... icNames) {
        List<ColumnMapping> cms = new ArrayList<ColumnMapping>();
        for (ColumnMapping cm : columnMaps) {
            cm = filter(cm, whereIc, whereOc);
            if (cm != null) {
                for (String icName : icNames) {
                    if (cm.getIc().equals(icName)) {
                        cms.add(cm);
                    }
                }
            }
        }
        return cms;
    }

    /**
     * 去重版
     *
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public Set<String> getAllUniqueOcNames(Boolean whereIc, Boolean whereOc) {
        Set<String> cms = new HashSet<String>();
        for (ColumnMapping cm : columnMaps) {
            cm = filter(cm, whereIc, whereOc);
            if (cm != null) {
                cms.add(cm.getOc());
            }
        }
        return cms;
    }

    /**
     * 去重版
     * @param whereIc 如果是null则不进行过滤
     * @param whereOc 如果是null则不进行过滤
     * @return
     */
    public Set<String> getUniqueOcNames(Boolean whereIc, Boolean whereOc, String... icNames) {
        Set<String> cms = new HashSet<String>();
        cms.addAll(getOcNames(whereIc, whereOc, icNames));
        return cms;
    }

    public TableStructure getIStructure(){
        return getStructure(true);
    }

    public TableStructure getOStructure(){
        return getStructure(false);
    }

    private TableStructure getStructure(boolean iot){
        TableStructure ts = new TableStructure();
        ts.setName(iot ? it : ot);
        List<ColumnStructure> css = new ArrayList<ColumnStructure>();
        for (ColumnMapping cm : columnMaps) {
            if (iot) {
                css.add(cm.getIStructure());
            } else {
                css.add(cm.getOStructure());
            }
        }
        ts.setColumns(css);
        ts.standardize();
        return ts;
    }

    /**
     * 通过条件验证 不通过返回null
     *
     * @param cm
     * @param whereIc 如果是null不验证
     * @param whereOc 如果是null不验证
     * @return
     */
    public static ColumnMapping filter(ColumnMapping cm, Boolean whereIc, Boolean whereOc) {
        if (whereIc != null) {
            if (!cm.getWhereIc().equals(whereIc)) {
                cm = null;
            }
        }
        if (whereOc != null) {
            if (!cm.getWhereOc().equals(whereOc)) {
                cm = null;
            }
        }
        return cm;
    }

    /**
     * 生成一个双方交换位置的TableMapping
     *
     * @return
     */
    public TableMapping generateSwapBothSides() {

        TableMapping tm = new TableMapping();
        tm.ot = it;
        tm.it = ot;

        List<ColumnMapping> rcms = new ArrayList<ColumnMapping>();

        for (ColumnMapping columnMap : this.columnMaps) {
            rcms.add(columnMap.generateSwapBothSides());
        }
        tm.columnMaps = rcms;

        return tm;
    }

    @Override
    protected Object clone() {
        TableMapping tm = new TableMapping();
        tm.it = it;
        tm.ot = ot;
        List<ColumnMapping> cms = new ArrayList<ColumnMapping>();
        for (ColumnMapping cm : columnMaps) {
            cms.add((ColumnMapping) cm.clone());
        }
        tm.columnMaps = cms;
        return tm;
    }

    public TableMapping fusion(TableMapping tableMapping) {
        TableMapping tm = (TableMapping) clone();
        TableMapping ctm = (TableMapping) tableMapping.clone();
        tm.ot = ctm.ot;
        tm.it = ctm.it;
        Map<String, ColumnMapping> um = new HashMap<String, ColumnMapping>();
        fusionByMap(tm, um);
        fusionByMap(tm, um);
        tm.columnMaps = new ArrayList<ColumnMapping>(um.values());
        return tm;
    }

    private void fusionByMap(TableMapping tm, Map<String, ColumnMapping> um) {
        for (ColumnMapping cm : tm.columnMaps) {
            String s = cm.getIc() + Mapping.FUSION_LINK + cm.getOc();
            um.put(s, cm);
        }
    }
}

package com.wzq.mapping;

import com.wzq.able.SwapBothSidesAble;
import com.wzq.sql.structure.ColumnStructure;
import com.wzq.sql.value.PlaceholderValue;

/**
 * 字段关系映射
 */
public class ColumnMapping implements SwapBothSidesAble,Cloneable {
    public ColumnMapping() {
    }

    public ColumnMapping(String ic, String oc, String pt) {
        this.ic = ic;
        this.oc = oc;
        this.pt = pt;
    }

    public ColumnMapping(String ic, String oc, String pt, Boolean whereOc, Boolean whereIc) {
        this.ic = ic;
        this.oc = oc;
        this.pt = pt;
        this.whereOc = whereOc;
        this.whereIc = whereIc;
    }

    /**
     * 己方字段
     */
    private String ic;
    /**
     * 他方字段
     */
    private String oc;
    /**
     * 字段的程序类型
     */
    private String pt;
    /**
     * 是否是唯一约束字段 判断时将加入过滤
     */
    private Boolean whereOc = false;
    /**
     * 是否是唯一约束字段 判断时将加入过滤
     */
    private Boolean whereIc = false;

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getOc() {
        return oc;
    }

    public void setOc(String oc) {
        this.oc = oc;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public Boolean getWhereOc() {
        return whereOc;
    }

    public void setWhereOc(Boolean whereOc) {
        this.whereOc = whereOc;
    }

    public Boolean getWhereIc() {
        return whereIc;
    }

    public void setWhereIc(Boolean whereIc) {
        this.whereIc = whereIc;
    }
    /**
     * 生成一个双方交换位置的ColumnMapping
     * @return
     */
    public ColumnMapping  generateSwapBothSides(){
        ColumnMapping cm = new ColumnMapping();
        cm.ic = oc;
        cm.oc = ic;
        cm.pt = pt;
        cm.whereIc = whereOc;
        cm.whereOc = whereIc;
        return cm;
    }

    @Override
    protected Object clone() {
        ColumnMapping cm = new ColumnMapping();
        cm.ic = ic;
        cm.oc = oc;
        cm.pt = pt;
        cm.whereOc = whereOc;
        cm.whereIc = whereIc;
        return cm;
    }

    public ColumnStructure getIStructure(ColumnStructure oStructure) {
        return getStructure(oStructure, true);
    }
    public ColumnStructure getOStructure(ColumnStructure iStructure) {
        return getStructure(iStructure,false);
    }

    private ColumnStructure getStructure(ColumnStructure structure, boolean iot) {
        String name = iot ? ic : oc;
        ColumnStructure cs = null;
        if ((!iot ? ic : oc).equals(structure.getName())) {
            cs = new ColumnStructure(name, pt, structure.getValue());
        }
        return cs;
    }
    public ColumnStructure getIStructure() {
        return getStructure(true);
    }
    public ColumnStructure getOStructure() {
        return getStructure(false);
    }
    private ColumnStructure getStructure(boolean iot) {
        return new ColumnStructure(iot ? ic : oc, pt, PlaceholderValue.getInstance());
    }
}

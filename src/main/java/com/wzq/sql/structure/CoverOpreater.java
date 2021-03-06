package com.wzq.sql.structure;

import java.util.Set;

public class CoverOpreater {
    /**
     * mapping名称
     */
    private String mappingName;
    /**
     * 己方表名
     */
    private String itName;
    /**
     * 过滤字段
     */
    private Set<ColumnStructure> whereColumns;
    /**
     * 修改值字段
     */
    private Set<ColumnStructure> valueColumns;

    public CoverOpreater(Set<ColumnStructure> valueColumns) {
        this.valueColumns = valueColumns;
    }

    public CoverOpreater(String itName, Set<ColumnStructure> whereColumns, Set<ColumnStructure> valueColumns) {
        this.itName = itName;
        this.whereColumns = whereColumns;
        this.valueColumns = valueColumns;
    }

    public CoverOpreater(String itName) {

        this.itName = itName;
    }

    public CoverOpreater(String mappingName, String itName, Set<ColumnStructure> whereColumns, Set<ColumnStructure> valueColumns) {

        this.mappingName = mappingName;
        this.itName = itName;
        this.whereColumns = whereColumns;
        this.valueColumns = valueColumns;
    }

    public String getItName() {
        return itName;
    }

    public void setItName(String itName) {
        this.itName = itName;
    }

    public Set<ColumnStructure> getWhereColumns() {
        return whereColumns;
    }

    public void setWhereColumns(Set<ColumnStructure> whereColumns) {
        this.whereColumns = whereColumns;
    }

    public Set<ColumnStructure> getValueColumns() {
        return valueColumns;
    }

    public void setValueColumns(Set<ColumnStructure> valueColumns) {
        this.valueColumns = valueColumns;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }
}

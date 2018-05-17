package com.wzq.sql.structure;

import com.wzq.core.structure.Attach;

import java.util.List;

public class MappingAttach implements Attach {

    private static final MappingAttach MAPPING_ATTACH = new MappingAttach();

    /**
     * 是否同步全部Mapping
     */
    private boolean syncAllMappings = true;
    /**
     * 是否同步数据结构
     */
    private boolean syncStructure = true;
    /**
     * 是否同步全部表
     */
    private boolean syncAllIts = true;
    /**
     * 是否同步全部列
     */
    private boolean syncAllIcs = true;
    /**
     * 是否存在覆盖执行操作
     */
    private boolean coverValue = false;
    /**
     * Mapping名称集合
     */
    private String[] mappingNames;
    /**
     * 表名称集合
     */
    private String[] itNames;
    /**
     * 表结构集合
     */
    private List<TableStructure> tableStructures;
    /**
     * 覆盖操作 key:where过滤
     */
    private List<CoverOpreater> coverOpreaters;

    public boolean isSyncStructure() {
        return syncStructure;
    }

    public void setSyncStructure(boolean syncStructure) {
        this.syncStructure = syncStructure;
    }

    public boolean isSyncAllIts() {
        return syncAllIts;
    }

    public void setSyncAllIts(boolean syncAllIts) {
        this.syncAllIts = syncAllIts;
    }

    public boolean isSyncAllIcs() {
        return syncAllIcs;
    }

    public void setSyncAllIcs(boolean syncAllIcs) {
        this.syncAllIcs = syncAllIcs;
    }

    public boolean isCoverValue() {
        return coverValue;
    }

    public void setCoverValue(boolean coverValue) {
        this.coverValue = coverValue;
    }

    public String[] getItNames() {
        return itNames;
    }

    public void setItNames(String[] itNames) {
        this.itNames = itNames;
    }

    public List<TableStructure> getTableStructures() {
        return tableStructures;
    }

    public void setTableStructures(List<TableStructure> tableStructures) {
        this.tableStructures = tableStructures;
    }

    public List<CoverOpreater> getCoverOpreaters() {
        return coverOpreaters;
    }

    public void setCoverOpreaters(List<CoverOpreater> coverOpreaters) {
        this.coverOpreaters = coverOpreaters;
    }

    public boolean isSyncAllMappings() {
        return syncAllMappings;
    }

    public void setSyncAllMappings(boolean syncAllMappings) {
        this.syncAllMappings = syncAllMappings;
    }

    public String[] getMappingNames() {
        return mappingNames;
    }

    public void setMappingNames(String[] mappingNames) {
        this.mappingNames = mappingNames;
    }

    public static MappingAttach getInstance() {
        return MAPPING_ATTACH;
    }
}

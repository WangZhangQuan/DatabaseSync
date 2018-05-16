package com.wzq.generator;

import com.wzq.mapping.Mapping;
import com.wzq.sql.type.Dialect;

import java.util.Map;

/**
 * Mapping的SQL生成器
 */
public interface MappingSqlGenerator {
    /**
     * 生成己方更新SQL
     * @param itName 己方表名
     * @param whereColumn 己方过滤字段和值
     * @param updateItColumn 影响的己方字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String generateUpdateSql(String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames);
    /**
     * 反向生成他方更新SQL集
     * @param itName 己方表名
     * @param whereColumn 他方过滤字段和值
     * @param updateItColumn 影响的己方字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String[] generateReverseUpdateSql(String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成己方插入SQL
     * @param itName 己方表名
     * @param insertItColumn 影响的己方字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String generateInsertSql(String itName, Map<String, Object> insertItColumn, String...otNames);

    /**
     * 生成己方插入SQL
     * @param itName 己方表名
     * @param insertItColumn 影响的己方字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String[] generateReverseInsertSql(String itName, Map<String, Object> insertItColumn, String...otNames);
    /**
     * 生成己方删除SQL
     * @param itName 己方表名
     * @param whereColumn 己方过滤字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String generateDeleteSql(String itName, Map<String, Object> whereColumn, String...otNames);
    /**
     * 生成他方删除SQL
     * @param itName 己方表名
     * @param whereColumn 他方过滤字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String[] generateReverseDeleteSql(String itName, Map<String, Object> whereColumn, String...otNames);
    /**
     * 生成己方查询SQL
     * @param itName 己方表名
     * @param selectItColumns 影响的己方字段
     * @param whereColumn 己方过滤字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String generateSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String...otNames);
    /**
     * 生成他方查询SQL
     * @param itName 己方表名
     * @param selectItColumns 影响的己方字段
     * @param whereColumn 他方过滤字段和值
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String[] generateReverseSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String...otNames);
    /**
     * 生成己方创建表SQL
     * @param itName 己方表名
     * @param createItColumns 影响的己方字段
     * @param dialect 标准类型的方言 为空时使用 SqlTypes.getDefaultDialect() 方言
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String generateCreateTableSql(String itName, String[] createItColumns, Dialect dialect, String...otNames);
    /**
     * 生成他方创建表SQL
     * @param itName 己方表名
     * @param createItColumns 影响的己方字段
     * @param dialect 标准类型的方言 为空时使用 SqlTypes.getDefaultDialect() 方言
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String[] generateReverseCreateTableSql(String itName, String[] createItColumns, Dialect dialect, String...otNames);
    /**
     * 删除己方创建表SQL
     * @param itName 己方表名
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String generateDropTableSql(String itName, String...otNames);
    /**
     * 删除他方创建表SQL
     * @param itName 己方表名
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    String[] generateReverseDropTableSql(String itName, String...otNames);
    /**
     * 设置Mapping
     * @param mapping
     */
    void setMapping(Mapping mapping);
    Mapping getMapping();
}

package com.wzq.command;

import com.wzq.core.generator.Generator;
import com.wzq.sql.type.Dialect;
import com.wzq.core.command.CommandArgs;

import java.util.Map;

public class SqlGeneratorCommandArgs extends CommandArgs {

    /**
     * 用于生成执行Command的生成器
     */
    private Generator generator;
    /**
     * 己方表名称
     */
    private String itName;
    /**
     * 影响的列
     */
    private Map<String, Object> influenceColumnMap;
    /**
     * 过滤的列
     */
    private Map<String, Object> whereColumnMap;
    /**
     * 对方表集合
     */
    private String[] otNames;
    /**
     * 显示和创建的字段集合
     */
    private String[] columnNames;

    /**
     * 创建时使用的方言
     */
    private Dialect dialect;

    public String getItName() {
        return itName;
    }

    public void setItName(String itName) {
        this.itName = itName;
    }

    public Map<String, Object> getInfluenceColumnMap() {
        return influenceColumnMap;
    }

    public void setInfluenceColumnMap(Map<String, Object> influenceColumnMap) {
        this.influenceColumnMap = influenceColumnMap;
    }

    public Map<String, Object> getWhereColumnMap() {
        return whereColumnMap;
    }

    public void setWhereColumnMap(Map<String, Object> whereColumnMap) {
        this.whereColumnMap = whereColumnMap;
    }

    public String[] getOtNames() {
        return otNames;
    }

    public void setOtNames(String[] otNames) {
        this.otNames = otNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public static SqlGeneratorCommandArgs newUpdateArgs(Generator generator, String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames) {
        SqlGeneratorCommandArgs args = new SqlGeneratorCommandArgs();
        args.generator = generator;
        args.itName = itName;
        args.influenceColumnMap = updateItColumn;
        args.whereColumnMap = whereColumn;
        args.otNames = otNames;
        return args;
    }

    public static SqlGeneratorCommandArgs newInsertArgs(Generator generator, String itName, Map<String, Object> insertItColumn, String... otNames) {
        SqlGeneratorCommandArgs args = new SqlGeneratorCommandArgs();
        args.generator = generator;
        args.itName = itName;
        args.influenceColumnMap = insertItColumn;
        args.otNames = otNames;
        return args;
    }

    public static SqlGeneratorCommandArgs newDeleteArgs(Generator generator, String itName, Map<String, Object> whereColumn, String... otNames) {
        SqlGeneratorCommandArgs args = new SqlGeneratorCommandArgs();
        args.generator = generator;
        args.itName = itName;
        args.whereColumnMap = whereColumn;
        args.otNames = otNames;
        return args;
    }

    public static SqlGeneratorCommandArgs newSelectArgs(Generator generator, String itName, String[] selectItColumns, Map<String, Object> whereColumn, String... otNames) {
        SqlGeneratorCommandArgs args = new SqlGeneratorCommandArgs();
        args.generator = generator;
        args.itName = itName;
        args.columnNames = selectItColumns;
        args.whereColumnMap = whereColumn;
        args.otNames = otNames;
        return args;
    }

    public static SqlGeneratorCommandArgs newCreateArgs(Generator generator, String itName, String[] createItColumns, Dialect dialect, String... otNames) {
        SqlGeneratorCommandArgs args = new SqlGeneratorCommandArgs();
        args.generator = generator;
        args.itName = itName;
        args.columnNames = createItColumns;
        args.dialect = dialect;
        args.otNames = otNames;
        return args;
    }

    public static SqlGeneratorCommandArgs newDropArgs(Generator generator, String itName, String... otNames) {
        SqlGeneratorCommandArgs args = new SqlGeneratorCommandArgs();
        args.generator = generator;
        args.itName = itName;
        args.otNames = otNames;
        return args;
    }
}

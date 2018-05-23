package com.wzq.generator;

import com.wzq.core.command.Command;
import com.wzq.core.command.Opreator;
import com.wzq.command.SqlExecutorCommandArgs;
import com.wzq.command.SqlGeneratorCommandArgs;
import com.wzq.generator.impl.Sql;
import com.wzq.mapping.Mapping;
import com.wzq.sql.type.Dialect;
import com.wzq.core.generator.Generator;

import java.util.List;
import java.util.Map;

/**
 * Mapping的SQL生成器
 */
public abstract class MappingSqlGenerator implements Generator {


    public Command generator(Command command) {
        if (command != null) {
            SqlGeneratorCommandArgs args = (SqlGeneratorCommandArgs) command.getArgs();
            if (args != null) {
                Command result = null;
                switch (command.getOpreator()) {

                    case UPDATE:
                        result = new Command(Opreator.UPDATE, new SqlExecutorCommandArgs(this.generateUpdateSql(args.getItName(), args.getInfluenceColumnMap(), args.getWhereColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case REVERSE_UPDATE:
                        result = new Command(Opreator.REVERSE_UPDATE, new SqlExecutorCommandArgs(this.generateReverseUpdateSql(args.getItName(), args.getInfluenceColumnMap(), args.getWhereColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case NEW:
                        result = new Command(Opreator.NEW, new SqlExecutorCommandArgs(this.generateInsertSql(args.getItName(), args.getInfluenceColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case REVERSE_NEW:
                        result = new Command(Opreator.REVERSE_NEW, new SqlExecutorCommandArgs(this.generateReverseInsertSql(args.getItName(), args.getInfluenceColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case DELETE:
                        result = new Command(Opreator.DELETE, new SqlExecutorCommandArgs(this.generateDeleteSql(args.getItName(), args.getWhereColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case REVERSE_DELETE:
                        result = new Command(Opreator.REVERSE_DELETE, new SqlExecutorCommandArgs(this.generateReverseDeleteSql(args.getItName(), args.getWhereColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case SHOW:
                        result = new Command(Opreator.SHOW, new SqlExecutorCommandArgs(this.generateSelectSql(args.getItName(), args.getColumnNames(), args.getWhereColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case REVERSE_SHOW:
                        result = new Command(Opreator.REVERSE_SHOW, new SqlExecutorCommandArgs(this.generateReverseSelectSql(args.getItName(), args.getColumnNames(), args.getWhereColumnMap(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case CREATE:
                        result = new Command(Opreator.CREATE, new SqlExecutorCommandArgs(this.generateCreateTableSql(args.getItName(), args.getColumnNames(), args.getDialect(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case REVERSE_CREATE:
                        result = new Command(Opreator.REVERSE_CREATE, new SqlExecutorCommandArgs(this.generateReverseCreateTableSql(args.getItName(), args.getColumnNames(), args.getDialect(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case DROP:
                        result = new Command(Opreator.DROP, new SqlExecutorCommandArgs(this.generateDropTableSql(args.getItName(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    case REVERSE_DROP:
                        result = new Command(Opreator.REVERSE_DROP, new SqlExecutorCommandArgs(this.generateDropTableSql(args.getItName(), args.getOtNames())), command.getStructure(), command.getMapping());
                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupport Command Opreator: " + command.getOpreator());
                }
                return result;
            }
        }
        return null;
    }

    /**
     * 生成己方更新SQL
     *
     * @param itName         己方表名
     * @param whereColumn    己方过滤字段和值
     * @param updateItColumn 影响的己方字段和值
     * @param otNames        他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateUpdateSql(String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames);

    /**
     * 反向生成他方更新SQL集
     *
     * @param itName         己方表名
     * @param whereColumn    他方过滤字段和值
     * @param updateItColumn 影响的己方字段和值
     * @param otNames        他方表名
     * @return String类型的SQL
     */
    public abstract List<Sql> generateReverseUpdateSql(String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成己方插入SQL
     *
     * @param itName         己方表名
     * @param insertItColumn 影响的己方字段和值
     * @param otNames        他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateInsertSql(String itName, Map<String, Object> insertItColumn, String... otNames);

    /**
     * 生成己方插入SQL
     *
     * @param itName         己方表名
     * @param insertItColumn 影响的己方字段和值
     * @param otNames        他方表名
     * @return String类型的SQL
     */
    public abstract List<Sql> generateReverseInsertSql(String itName, Map<String, Object> insertItColumn, String... otNames);

    /**
     * 生成己方删除SQL
     *
     * @param itName      己方表名
     * @param whereColumn 己方过滤字段和值
     * @param otNames     他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateDeleteSql(String itName, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成他方删除SQL
     *
     * @param itName      己方表名
     * @param whereColumn 他方过滤字段和值
     * @param otNames     他方表名
     * @return String类型的SQL
     */
    public abstract List<Sql> generateReverseDeleteSql(String itName, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成己方查询SQL
     *
     * @param itName          己方表名
     * @param selectItColumns 影响的己方字段
     * @param whereColumn     己方过滤字段和值
     * @param otNames         他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成他方查询SQL
     *
     * @param itName          己方表名
     * @param selectItColumns 影响的己方字段
     * @param whereColumn     他方过滤字段和值
     * @param otNames         他方表名
     * @return String类型的SQL
     */
    public abstract List<Sql> generateReverseSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成他方级联查询sql
     *
     * @param itName          己方表名
     * @param selectItColumns 影响的己方字段
     * @param whereColumn     他方过滤字段和值
     * @param otNames         他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateReverseRelationSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String... otNames);

    /**
     * 生成己方创建表SQL
     *
     * @param itName          己方表名
     * @param createItColumns 影响的己方字段
     * @param dialect         标准类型的方言 为空时使用 SqlTypes.getDefaultDialect() 方言
     * @param otNames         他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateCreateTableSql(String itName, String[] createItColumns, Dialect dialect, String... otNames);

    /**
     * 生成他方创建表SQL
     *
     * @param itName          己方表名
     * @param createItColumns 影响的己方字段
     * @param dialect         标准类型的方言 为空时使用 SqlTypes.getDefaultDialect() 方言
     * @param otNames         他方表名
     * @return String类型的SQL
     */
    public abstract List<Sql> generateReverseCreateTableSql(String itName, String[] createItColumns, Dialect dialect, String... otNames);

    /**
     * 删除己方创建表SQL
     *
     * @param itName  己方表名
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    public abstract Sql generateDropTableSql(String itName, String... otNames);

    /**
     * 删除他方创建表SQL
     *
     * @param itName  己方表名
     * @param otNames 他方表名
     * @return String类型的SQL
     */
    public abstract List<Sql> generateReverseDropTableSql(String itName, String... otNames);

    /**
     * 设置Mapping
     *
     * @param mapping
     */
    public abstract void setMapping(Mapping mapping);

    public abstract Mapping getMapping();
}

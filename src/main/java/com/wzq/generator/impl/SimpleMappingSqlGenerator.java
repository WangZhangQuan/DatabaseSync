package com.wzq.generator.impl;

import com.wzq.generator.MappingSqlGenerator;
import com.wzq.mapping.ColumnMapping;
import com.wzq.mapping.Mapping;
import com.wzq.mapping.TableMapping;
import com.wzq.sql.type.Dialect;
import com.wzq.sql.type.SqlTypes;
import com.wzq.sql.type.UnsupportSqlType;
import com.wzq.sql.value.ArrayValue;
import com.wzq.sql.value.PlaceholderValue;
import com.wzq.sql.value.SqlExpressionValue;
import com.wzq.util.KeyValue;
import com.wzq.util.MapUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.lang.reflect.Array;
import java.util.*;

public class SimpleMappingSqlGenerator implements MappingSqlGenerator {

    public static final String DROP_TABLE_TYPE = "TABLE";

    private Mapping mapping;

    private Mapping reverseMapping;

    private MappingSqlGenerator reverseGenerater;

    public SimpleMappingSqlGenerator() {
    }

    public SimpleMappingSqlGenerator(Mapping mapping) {
        this.mapping = mapping;
        this.reverseMapping = mapping.generateSwapBothSides();
    }


    public Mapping getMapping() {
        return mapping;
    }

    public String generateUpdateSql(String itName, String[] updateItColumns, String[] whereColumns, String... otNames) {
        return generateUpdateSql(itName, MapUtils.mapFromO(Arrays.asList(updateItColumns), PlaceholderValue.getInstance()), MapUtils.mapFromO(Arrays.asList(whereColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String generateUpdateSql(String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames) && updateItColumn != null && updateItColumn.size() > 0) { // 存在更新的字段
            String[] allItColumns = mapping.getAllItColumns(itName, otNames);
            if (allItColumns.length > 0) { // 存在更新的字段
                Set<String> aicSet = new HashSet<String>(Arrays.asList(allItColumns));
                Set<String> uicSet = updateItColumn.keySet();
                aicSet.retainAll(uicSet); // 求交集
                if (aicSet.size() > 0) { // 存在更新的字段
                    Update update = new Update();
                    Table table = wrapTableName(itName);
                    update.setTable(table);
                    KeyValue<List<String>, List<Object>> llkv = MapUtils.mapSplit(MapUtils.mapIn(updateItColumn, aicSet));
                    List<Column> cs = wrapColumnNames(table, llkv.getKey().toArray(new String[llkv.getKey().size()]));
                    update.setColumns(cs);
                    update.setExpressions(wrapColumnValues(cs, llkv.getValue()));
                    String[] aiwcs = mapping.getAllItWhereColumns(itName, otNames);
                    // 设置过滤条件
                    Set<String> aiwcsSet = new HashSet<String>(Arrays.asList(aiwcs));
                    Set<String> wcsSet = whereColumn.keySet();
                    aiwcsSet.retainAll(wcsSet);
                    if (aiwcsSet.size() > 0) { // 存在过滤条件
                        KeyValue<List<String>, List<Object>> wllkv = MapUtils.mapSplit(MapUtils.mapIn(whereColumn, aiwcsSet));
                        update.setWhere(wrapWheresValues(wrapColumnNames(table, wllkv.getKey().toArray(new String[wllkv.getKey().size()])), wllkv.getValue()));
                    }
                    StatementDeParser statementDeParser = new StatementDeParser(new StringBuffer());
                    statementDeParser.visit(update);
                    return statementDeParser.getBuffer().toString();
                }
            }
        }
        return null;
    }

    public String[] generateReverseUpdateSql(String itName, String[] updateItColumns, String[] whereColumns, String... otNames) {
        return generateReverseUpdateSql(itName, MapUtils.mapFromO(Arrays.asList(updateItColumns), PlaceholderValue.getInstance()), MapUtils.mapFromO(Arrays.asList(whereColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String[] generateReverseUpdateSql(String itName, Map<String, Object> updateItColumn, Map<String, Object> whereColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames) && updateItColumn != null && updateItColumn.size() > 0) { // 存在更新的字段
            String[] aons = mapping.getAllOtNames(itName);
            Set<String> aonsSet = new HashSet<String>(Arrays.asList(aons));
            Set<String> onsSet = new HashSet<String>(Arrays.asList(otNames));
            aonsSet.retainAll(onsSet);
            if (aonsSet.size() > 0) { // 存在更新的表
                String[] aonsx = aonsSet.toArray(new String[aonsSet.size()]);
                Set<String> cks = updateItColumn.keySet();
                Map<String, ColumnMapping[]> ocms = mapping.getOtColumnMappings(itName, cks.toArray(new String[cks.size()]), aonsx);
                Set<String> vs = whereColumn.keySet();
                Map<String, ColumnMapping[]> ocwms = mapping.getOtColumnMappings(itName, vs.toArray(new String[vs.size()]), aonsx);
                List<String> sqls = new ArrayList<String>(aonsSet.size());
                MappingSqlGenerator rg = getReverseGenerater();
                for (Map.Entry<String, ColumnMapping[]> entry : ocms.entrySet()) {
                    if (entry.getValue() != null && entry.getValue().length > 0) { // 验证此表是否有更新的字段
                        ColumnMapping[] wcns = ocwms.get(entry.getKey());
                        if (wcns == null) {
                            wcns = new ColumnMapping[0];
                        }
                        sqls.add(rg.generateUpdateSql(entry.getKey(), reverseValues(entry.getValue(), updateItColumn), reverseValues(wcns, whereColumn), itName));
                    }
                }
                return sqls.toArray(new String[sqls.size()]);
            }
        }
        return new String[0];
    }

    public String generateInsertSql(String itName, String[] insertItColumns, String... otNames) {
        return generateInsertSql(itName, MapUtils.mapFromO(Arrays.asList(insertItColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String generateInsertSql(String itName, Map<String, Object> insertItColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames) && insertItColumn != null && insertItColumn.size() > 0) { // 存在插入的字段
            String[] allItColumns = mapping.getAllItColumns(itName, otNames);
            if (allItColumns.length > 0) { // 存在插入的字段
                Set<String> aicSet = new HashSet<String>(Arrays.asList(allItColumns));
                Set<String> ks = insertItColumn.keySet();
                Set<String> uicSet = new HashSet<String>(Arrays.asList(ks.toArray(new String[ks.size()])));
                aicSet.retainAll(uicSet); // 求交集
                if (aicSet.size() > 0) { // 存在插入的字段
                    Insert insert = new Insert();
                    Table table = wrapTableName(itName);
                    KeyValue<List<String>, List<Object>> llkv = MapUtils.mapSplit(MapUtils.mapIn(insertItColumn, aicSet));
                    List<Column> cs = wrapColumnNames(table, llkv.getKey().toArray(new String[llkv.getKey().size()]));
                    insert.setTable(table);
                    insert.setColumns(cs);
                    ExpressionList el = new ExpressionList();
                    el.setExpressions(wrapColumnValues(cs, llkv.getValue()));
                    insert.setItemsList(el);
                    return insert.toString();
                }
            }
        }
        return null;
    }

    public String[] generateReverseInsertSql(String itName, String[] insertItColumns, String... otNames) {
        return generateReverseInsertSql(itName, MapUtils.mapFromO(Arrays.asList(insertItColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String[] generateReverseInsertSql(String itName, Map<String, Object> insertItColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames) && insertItColumn != null && insertItColumn.size() > 0) { // 存在插入的字段
            String[] aons = mapping.getAllOtNames(itName);
            Set<String> aonsSet = new HashSet<String>(Arrays.asList(aons));
            Set<String> onsSet = new HashSet<String>(Arrays.asList(otNames));
            aonsSet.retainAll(onsSet);
            if (aonsSet.size() > 0) { // 存在插入的表
                String[] aonsx = aonsSet.toArray(new String[aonsSet.size()]);
                Set<String> ks = insertItColumn.keySet();
                Map<String, ColumnMapping[]> moc = mapping.getOtColumnMappings(itName, ks.toArray(new String[ks.size()]), aonsx);
                List<String> sqls = new ArrayList<String>(aonsSet.size());
                MappingSqlGenerator rg = getReverseGenerater();
                for (Map.Entry<String, ColumnMapping[]> entry : moc.entrySet()) {
                    if (entry.getValue() != null && entry.getValue().length > 0) { // 验证此表是否有插入的字段
                        sqls.add(rg.generateInsertSql(entry.getKey(), reverseValues(entry.getValue(), insertItColumn), itName));
                    }
                }
                return sqls.toArray(new String[sqls.size()]);
            }
        }
        return new String[0];
    }

    public String generateDeleteSql(String itName, String[] whereColumns, String... otNames) {
        return generateDeleteSql(itName, MapUtils.mapFromO(Arrays.asList(whereColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String generateDeleteSql(String itName, Map<String, Object> whereColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            List<TableMapping> tms = mapping.findByItNameAndOtNames(itName, otNames);
            if (tms != null && tms.size() > 0) {
                Delete delete = new Delete();
                Table table = wrapTableName(itName);
                delete.setTable(table);
                String[] aiwcs = mapping.getAllItWhereColumns(itName, otNames);
                // 设置过滤条件
                Set<String> aiwcsSet = new HashSet<String>(Arrays.asList(aiwcs));
                Set<String> ks = whereColumn.keySet();
                Set<String> wcsSet = new HashSet<String>(Arrays.asList(ks.toArray(new String[ks.size()])));
                aiwcsSet.retainAll(wcsSet);
                if (aiwcsSet.size() > 0) { // 存在过滤条件
                    KeyValue<List<String>, List<Object>> llkv = MapUtils.mapSplit(MapUtils.mapIn(whereColumn, aiwcsSet));
                    delete.setWhere(wrapWheresValues(wrapColumnNames(table, llkv.getKey().toArray(new String[llkv.getKey().size()])), llkv.getValue()));
                }
                return delete.toString();
            }
        }
        return null;
    }

    public String[] generateReverseDeleteSql(String itName, Map<String, Object> whereColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            String[] aons = mapping.getAllOtNames(itName);
            Set<String> aonsSet = new HashSet<String>(Arrays.asList(aons));
            Set<String> onsSet = new HashSet<String>(Arrays.asList(otNames));
            aonsSet.retainAll(onsSet);
            if (aonsSet.size() > 0) { // 存在删除数据的表
                String[] aonsx = aonsSet.toArray(new String[aonsSet.size()]);
                Set<String> ks = whereColumn.keySet();
                Map<String, ColumnMapping[]> owc = mapping.getOtWhereMappingColumns(itName, ks.toArray(new String[ks.size()]), aonsx);
                List<String> sqls = new ArrayList<String>(aonsSet.size());
                MappingSqlGenerator rg = getReverseGenerater();
                for (String aon : aons) {
                    ColumnMapping[] wcns = owc.get(aon);
                    if (wcns == null) {
                        wcns = new ColumnMapping[0];
                    }
                    sqls.add(rg.generateDeleteSql(aon, reverseValues(wcns, whereColumn), itName));
                }
                return sqls.toArray(new String[sqls.size()]);
            }
        }
        return new String[0];
    }

    public String generateSelectSql(String itName, String[] selectItColumns, String[] whereColumns, String... otNames) {
        return generateSelectSql(itName, selectItColumns, MapUtils.mapFromO(Arrays.asList(whereColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String generateSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            String[] allItColumns = mapping.getAllItColumns(itName, otNames);
            Set<String> aicSet = new HashSet<String>(Arrays.asList(allItColumns));
            Set<String> sicSet = new HashSet<String>(Arrays.asList(selectItColumns));
            aicSet.retainAll(sicSet);
            if (aicSet.size() > 0) { // 判断是否存在读取的字段
                Select select = new Select();
                Table table = wrapTableName(itName);
                PlainSelect ps = new PlainSelect();
                ps.setSelectItems(wrapColumnNames(table, aicSet.toArray(new String[aicSet.size()])));
                ps.setFromItem(table);
                String[] aiwcs = mapping.getAllItWhereColumns(itName, otNames);
                Set<String> aiwcSet = new HashSet<String>(Arrays.asList(aiwcs));
                Set<String> ks = whereColumn.keySet();
                Set<String> wcSet = new HashSet<String>(Arrays.asList(ks.toArray(new String[ks.size()])));
                aiwcSet.retainAll(wcSet);
                if (aiwcSet.size() > 0) { // 判断是否存在过滤条件
                    KeyValue<List<String>, List<Object>> llkv = MapUtils.mapSplit(MapUtils.mapIn(whereColumn, aiwcSet));
                    ps.setWhere(wrapWheresValues(wrapColumnNames(table, llkv.getKey().toArray(new String[llkv.getKey().size()])), llkv.getValue()));
                }
                select.setSelectBody(ps);
                return select.toString();
            }
        }
        return null;
    }

    public String[] generateReverseSelectSql(String itName, String[] selectItColumns, String[] whereColumns, String... otNames) {
        return generateReverseSelectSql(itName, selectItColumns, MapUtils.mapFromO(Arrays.asList(whereColumns), PlaceholderValue.getInstance()), otNames);
    }

    public String[] generateReverseSelectSql(String itName, String[] selectItColumns, Map<String, Object> whereColumn, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            String[] allItColumns = mapping.getAllItColumns(itName, otNames);
            Set<String> aicSet = new HashSet<String>(Arrays.asList(allItColumns));
            Set<String> sicSet = new HashSet<String>(Arrays.asList(selectItColumns));
            aicSet.retainAll(sicSet);
            if (aicSet.size() > 0) { // 判断是否存在读取的字段
                Map<String, String[]> otColumns = mapping.getOtColumns(itName, aicSet.toArray(new String[aicSet.size()]), otNames);
                String[] aiwcs = mapping.getAllItWhereColumns(itName, otNames);
                Set<String> aiwcSet = new HashSet<String>(Arrays.asList(aiwcs));
                Set<String> ks = whereColumn.keySet();
                Set<String> wcSet = new HashSet<String>(Arrays.asList(ks.toArray(new String[ks.size()])));
                aiwcSet.retainAll(wcSet);
                Map<String, ColumnMapping[]> otWhereColumns = null;
                if (aiwcSet.size() > 0) {
                    otWhereColumns = mapping.getOtWhereMappingColumns(itName, aiwcSet.toArray(new String[aiwcSet.size()]), otNames);
                }
                Set<Map.Entry<String, String[]>> entries = otColumns.entrySet();
                List<String> sqls = new ArrayList<String>();
                MappingSqlGenerator rg = getReverseGenerater();
                for (Map.Entry<String, String[]> entry : entries) {
                    ColumnMapping[] cwcs = null;
                    if (otWhereColumns != null) {
                        cwcs = otWhereColumns.get(entry.getKey());
                    }
                    if (cwcs == null) {
                        cwcs = new ColumnMapping[0];
                    }

                    sqls.add(rg.generateSelectSql(entry.getKey(), entry.getValue(), reverseValues(cwcs, whereColumn), itName));
                }
                return sqls.toArray(new String[sqls.size()]);
            }
        }
        return new String[0];
    }

    public String generateCreateTableSql(String itName, String[] createItColumns, Dialect dialect, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            String[] allItColumns = mapping.getAllItColumns(itName, otNames);
            Set<String> aicSet = new HashSet<String>(Arrays.asList(allItColumns));
            Set<String> cicSet = new HashSet<String>(Arrays.asList(createItColumns));
            aicSet.retainAll(cicSet);
            if (aicSet.size() > 0) { // 判断是否存在创建的字段
                HashSet<String> msc = new HashSet<String>();
                CreateTable createTable = new CreateTable();
                createTable.setTable(wrapTableName(itName));
                dialect = getCanUseDialect(dialect); // 获得能使用的方言
                List<ColumnDefinition> cds = new ArrayList<ColumnDefinition>();
                List<TableMapping> binaons = mapping.findByItNameAndOtNames(itName, otNames);
                for (TableMapping binaon : binaons) {
                    List<ColumnMapping> cms = binaon.getColumnMaps();
                    for (ColumnMapping cm : cms) {
                        if (!msc.contains(cm.getIc()) && aicSet.contains(cm.getIc())) { // 过滤重复的column
                            try {
                                msc.add(cm.getIc());
                                ColumnDefinition cd = new ColumnDefinition();
                                cd.setColumnName(cm.getIc());
                                cd.setColDataType(wrapColumnProgramType(cm.getPt(), dialect));
                                cds.add(cd);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                createTable.setColumnDefinitions(cds);
                if (createTable.getColumnDefinitions().size() <= 0) {
                    return null;
                }
                return createTable.toString();
            }
        }
        return null;
    }

    public String[] generateReverseCreateTableSql(String itName, String[] createItColumns, Dialect dialect, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            String[] allItColumns = mapping.getAllItColumns(itName, otNames);
            Set<String> aicSet = new HashSet<String>(Arrays.asList(allItColumns));
            Set<String> cicSet = new HashSet<String>(Arrays.asList(createItColumns));
            aicSet.retainAll(cicSet);
            if (aicSet.size() > 0) { // 判断是否存在创建的字段
                Map<String, String[]> ocs = mapping.getOtColumns(itName, aicSet.toArray(new String[aicSet.size()]), otNames);
                MappingSqlGenerator rg = getReverseGenerater();
                Set<Map.Entry<String, String[]>> entries = ocs.entrySet();
                List<String> sqls = new ArrayList<String>(entries.size());
                for (Map.Entry<String, String[]> entry : entries) {
                    sqls.add(rg.generateCreateTableSql(entry.getKey(), entry.getValue(), dialect, itName));
                }
                return sqls.toArray(new String[sqls.size()]);
            }
        }
        return new String[0];
    }

    public String generateDropTableSql(String itName, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            List<TableMapping> binaon = mapping.findByItNameAndOtNames(itName, otNames);
            if (binaon != null) {
                Drop drop = new Drop();
                drop.setName(itName);
                drop.setType(DROP_TABLE_TYPE);
                return drop.toString();
            }
        }
        return null;
    }

    public String[] generateReverseDropTableSql(String itName, String... otNames) {
        if (validateItNameAndOtNames(itName, otNames)) {
            List<TableMapping> binaon = mapping.findByItNameAndOtNames(itName, otNames);
            List<String> sqls = new ArrayList<String>();
            for (TableMapping tm : binaon) {
                Drop drop = new Drop();
                drop.setName(tm.getOt());
                drop.setType(DROP_TABLE_TYPE);
                sqls.add(drop.toString());
            }
            return sqls.toArray(new String[sqls.size()]);
        }
        return new String[0];
    }

    private static Map<String, Object> reverseValues(ColumnMapping[] mappings, Map<String, Object> values) {
        Map<String, Object> ms = new HashMap<String, Object>();
        for (ColumnMapping mapping : mappings) {
            ms.put(mapping.getOc(), values.get(mapping.getIc()));
        }
        return ms;
    }

    private static boolean validateItNameAndOtNames(String itName, String... otNames) {
        if (itName != null && !"".equals(itName) && otNames != null && otNames.length > 0) {
            return true;
        } else
            return false;
    }

    private static ColDataType wrapColumnProgramType(String pt, Dialect dialect) {
        Integer st = SqlTypes.getSqlTypeByJavaClassName(pt);
        String sts = dialect.getSqlTypeString(st);
        if (sts != null) {
            ColDataType cdt = new ColDataType();
            cdt.setDataType(sts);
            return cdt;
        } else
            throw new UnsupportSqlType("UnsupportSqlType: " + st + ", ProgramType: " + sts);
    }

    private static Dialect getCanUseDialect(Dialect dialect) {
        if (dialect != null) {
            return dialect;
        } else
            return SqlTypes.getDefaultDialect();
    }

    private static Table wrapTableName(String tableName) {
        Table table = new Table();
        table.setName(tableName);
        return table;
    }

    private static Column wrapColumnName(Table table, String cn) {
        Column c = new Column(table, cn);
        return c;
    }

    private static List<Column> wrapColumnNames(Table table, String[] cns) {
        List<Column> cs = new ArrayList<Column>();
        for (String cn : cns) {
            cs.add(wrapColumnName(table, cn));
        }
        return cs;
    }

    private static Expression wrapWheresPlaceholderValues(List<Column> wheres) {
        List<Object> values = new ArrayList<Object>();
        for (Column where : wheres) {
            values.add(PlaceholderValue.getInstance());
        }
        return wrapWheresValues(wheres, values);
    }

    private static Expression wrapWheresValues(List<Column> wheres, List<Object> values) {
        Expression ae = null;
        for (int i = 0; i < wheres.size(); ++i) {
            if (values.size() > i) {
                Expression te = wrapWhereValue(wheres.get(i), wrapColumnValue(values.get(i)));
                if (i > 0) {
                    ae = new AndExpression(ae, te);
                } else {
                    ae = te;
                }
            }
        }
        return ae;
    }

    private static Expression wrapWhereValue(Column where, Object value) {
        EqualsTo et = new EqualsTo();
        et.setLeftExpression(where);
        et.setRightExpression(wrapColumnValue(value));
        return et;
    }

    private static List<Expression> wrapColumnValues(List<Column> cs, List<Object> values) {
        List<Expression> es = new ArrayList<Expression>();
        for (int i = 0; i < cs.size(); ++i) {
            Object o = values.get(i);
            es.add(wrapColumnValue(o));
        }
        return es;
    }

    private static Expression wrapColumnValue(Object value) {
        if (value instanceof String) {
            return value == null ? new NullValue() : new StringValue(value.toString());
        } else if (value instanceof PlaceholderValue) {
            return new SqlExpressionValue(PlaceholderValue.PLACEHOLDER);
        } else {
            if (value != null) {
                Class<?> aClass = value.getClass();
                if (aClass.isArray()) {
                    ArrayValue av = new ArrayValue();
                    int length = Array.getLength(value);
                    for (int l = 0; l < length; ++l) {
                        Object o1 = Array.get(value, l);
                        Expression e = wrapColumnValue(o1);
                        av.getEs().add(e);
                    }
                    return av;
                } else {
                    return new SqlExpressionValue(value.toString());
                }
            } else {
                return new NullValue();
            }
        }
    }

    private static List<Expression> wrapColumnPlaceholderValues(List<Column> cs) {
        List<Object> values = new ArrayList<Object>();
        for (Column c : cs) {
            values.add(PlaceholderValue.getInstance());
        }
        return wrapColumnValues(cs, values);
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
        this.reverseMapping = mapping.generateSwapBothSides();
    }

    public MappingSqlGenerator getReverseGenerater() {
        synchronized (this) {
            if (reverseGenerater == null) {
                reverseGenerater = new SimpleMappingSqlGenerator();
            }
            ((SimpleMappingSqlGenerator) reverseGenerater).mapping = reverseMapping;
            return reverseGenerater;
        }
    }
}

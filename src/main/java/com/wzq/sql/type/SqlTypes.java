package com.wzq.sql.type;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL类型和java类型关系
 * 关联的SqlTypes类型
 */
public class SqlTypes {
    private static final Map<Integer, String> SQL_TYPES_TO_JAVA_TYPES = new HashMap<Integer, String>();

    static {
        SQL_TYPES_TO_JAVA_TYPES.put(Types.CHAR, String.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.VARCHAR, String.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.LONGVARCHAR, String.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.NUMERIC, BigDecimal.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.DECIMAL, BigDecimal.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.BIT, Boolean.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.TINYINT, Byte.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.SMALLINT, Short.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.INTEGER, Integer.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.BIGINT, Long.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.REAL, Float.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.FLOAT, Double.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.DOUBLE, Double.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.BINARY, Byte[].class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.VARBINARY, Byte[].class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.LONGVARBINARY, Byte[].class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.DATE, java.sql.Date.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.TIME, java.sql.Time.class.getName());
        SQL_TYPES_TO_JAVA_TYPES.put(Types.TIMESTAMP, java.sql.Timestamp.class.getName());
    }

    private static final Map<String, Integer> JAVA_TYPES_TO_SQL_TYPES = new HashMap<String, Integer>();

    static {
        JAVA_TYPES_TO_SQL_TYPES.put(String.class.getName(), Types.VARCHAR);
        JAVA_TYPES_TO_SQL_TYPES.put(BigDecimal.class.getName(), Types.NUMERIC);
        JAVA_TYPES_TO_SQL_TYPES.put(Boolean.class.getName(), Types.BIT);
        JAVA_TYPES_TO_SQL_TYPES.put(Byte.class.getName(), Types.SMALLINT);
        JAVA_TYPES_TO_SQL_TYPES.put(Short.class.getName(), Types.SMALLINT);
        JAVA_TYPES_TO_SQL_TYPES.put(Integer.class.getName(), Types.INTEGER);
        JAVA_TYPES_TO_SQL_TYPES.put(Long.class.getName(), Types.BIGINT);
        JAVA_TYPES_TO_SQL_TYPES.put(Float.class.getName(), Types.FLOAT);
        JAVA_TYPES_TO_SQL_TYPES.put(Double.class.getName(), Types.FLOAT);
        JAVA_TYPES_TO_SQL_TYPES.put(java.sql.Date.class.getName(), Types.DATE);
        JAVA_TYPES_TO_SQL_TYPES.put(java.sql.Time.class.getName(), Types.TIME);
        JAVA_TYPES_TO_SQL_TYPES.put(java.sql.Timestamp.class.getName(), Types.TIMESTAMP);
    }

    public static final Map<Integer, String> DEFAULT_SQL_TYPES_DIALECT = new ConcurrentHashMap<Integer, String>();

    static {
        DEFAULT_SQL_TYPES_DIALECT.put(Types.CHAR, "CHAR");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.VARCHAR, "VARCHAR(255)");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.LONGVARCHAR, "LONGVARCHAR");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.NUMERIC, "NUMERIC");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.DECIMAL, "DECIMAL");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.BIT, "BIT");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.TINYINT, "TINYINT");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.SMALLINT, "SMALLINT");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.INTEGER, "INT");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.BIGINT, "BIGINT");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.REAL, "REAL");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.FLOAT, "FLOAT");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.DOUBLE, "DOUBLE");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.BINARY, "BINARY");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.VARBINARY, "VARBINARY");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.LONGVARBINARY, "LONGVARBINARY");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.DATE, "DATE");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.TIME, "TIME");
        DEFAULT_SQL_TYPES_DIALECT.put(Types.TIMESTAMP, "TIMESTAMP");
    }

    private static Dialect DIALECT = new Dialect();

    public static String getJavaClassNameBySqlType(Integer sqlType) {
        return SQL_TYPES_TO_JAVA_TYPES.get(sqlType);
    }

    public static Integer getSqlTypeByJavaClassName(String javaClassName) {
        return JAVA_TYPES_TO_SQL_TYPES.get(javaClassName);
    }

    public static Class<?> getJavaClassBySqlType(Integer sqlType) throws ClassNotFoundException {
        return Class.forName(SQL_TYPES_TO_JAVA_TYPES.get(sqlType));
    }

    public static Integer getSqlTypeByJavaClass(Class<?> clazz) {
        return JAVA_TYPES_TO_SQL_TYPES.get(clazz.getName());
    }

    public static String getSqlTypeStringBySqlType(Integer sqlType) {
        return DEFAULT_SQL_TYPES_DIALECT.get(sqlType);
    }

    public static String getSqlTypeStringByJavaClass(Class<?> clazz) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClass(clazz));
    }

    public static String getSqlTypeStringByJavaClassName(String javaClassName) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClassName(javaClassName));
    }

    public static String getSqlTypeStringBySqlType(Integer sqlType, Map<Integer, String> typeDialect) {
        return typeDialect.get(sqlType);
    }

    public static String getSqlTypeStringByJavaClass(Class<?> clazz, Map<Integer, String> typeDialect) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClass(clazz), typeDialect);
    }

    public static String getSqlTypeStringByJavaClassName(String javaClassName, Map<Integer, String> typeDialect) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClassName(javaClassName), typeDialect);
    }

    public static Dialect getDefaultDialect() {
        return DIALECT;
    }
}

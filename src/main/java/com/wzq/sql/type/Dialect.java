package com.wzq.sql.type;

import java.util.Map;

public class Dialect {
    private Map<Integer, String> dialect = SqlTypes.DEFAULT_SQL_TYPES_DIALECT;

    public Dialect() {
    }

    public Dialect(Map<Integer, String> dialect) {
        this.dialect = dialect;
    }

    public String getJavaClassNameBySqlType(Integer sqlType) {
        return SqlTypes.SQL_TYPES_TO_JAVA_TYPES.get(sqlType);
    }

    public Integer getSqlTypeByJavaClassName(String javaClassName) {
        return SqlTypes.JAVA_TYPES_TO_SQL_TYPES.get(javaClassName);
    }

    public Class<?> getJavaClassBySqlType(Integer sqlType) throws ClassNotFoundException {
        return Class.forName(SqlTypes.SQL_TYPES_TO_JAVA_TYPES.get(sqlType));
    }

    public Integer getSqlTypeByJavaClass(Class<?> clazz) {
        return SqlTypes.JAVA_TYPES_TO_SQL_TYPES.get(clazz.getName());
    }

    public String getSqlTypeStringBySqlType(Integer sqlType) {
        return SqlTypes.DEFAULT_SQL_TYPES_DIALECT.get(sqlType);
    }

    public String getSqlTypeStringByJavaClass(Class<?> clazz) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClass(clazz));
    }

    public String getSqlTypeStringByJavaClassName(String javaClassName) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClassName(javaClassName));
    }

    public String getSqlTypeStringBySqlType(Integer sqlType, Map<Integer, String> typeDialect) {
        return typeDialect.get(sqlType);
    }

    public String getSqlTypeStringByJavaClass(Class<?> clazz, Map<Integer, String> typeDialect) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClass(clazz), typeDialect);
    }

    public String getSqlTypeStringByJavaClassName(String javaClassName, Map<Integer, String> typeDialect) {
        return getSqlTypeStringBySqlType(getSqlTypeByJavaClassName(javaClassName), typeDialect);
    }

    public String getSqlTypeString(Integer sqlType) {
        return dialect.get(sqlType);
    }

}

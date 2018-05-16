package com.wzq.sql.type;

import java.util.Map;

public class Dialect {
    private Map<Integer, String> dialect = SqlTypes.DEFAULT_SQL_TYPES_DIALECT;

    public Dialect() {
    }

    public Dialect(Map<Integer, String> dialect) {
        this.dialect = dialect;
    }

    public String getSqlTypeString(Integer sqlType) {
        return dialect.get(sqlType);
    }

}

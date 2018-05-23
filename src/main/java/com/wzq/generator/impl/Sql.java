package com.wzq.generator.impl;

import com.wzq.util.KeyValue;
import net.minidev.json.JSONValue;

import java.util.List;

public class Sql {
    public Sql(String sql) {
        this.sql = sql;
    }

    public Sql(String sql, List<KeyValue<String, Object>> parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    private String sql;
    private List<KeyValue<String, Object>> parameters;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<KeyValue<String, Object>> getParameters() {
        return parameters;
    }

    public void setParameters(List<KeyValue<String, Object>> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return JSONValue.toJSONString(this);
    }
}

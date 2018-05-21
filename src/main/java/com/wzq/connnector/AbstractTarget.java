package com.wzq.connnector;

import com.wzq.core.connector.Target;
import com.wzq.sql.type.Dialect;
import com.wzq.sql.type.SqlTypes;


public abstract class AbstractTarget implements Target {

    private Dialect dialect = SqlTypes.getDefaultDialect();
    private int batch = 1000;

    public Dialect getDialect() {
        return dialect;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }
}

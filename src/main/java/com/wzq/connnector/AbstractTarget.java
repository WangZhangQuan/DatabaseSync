package com.wzq.connnector;

import com.wzq.core.connector.Target;
import com.wzq.sql.type.Dialect;
import com.wzq.sql.type.SqlTypes;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class AbstractTarget implements Target {

    private Dialect dialect = SqlTypes.getDefaultDialect();
    private AtomicInteger batch = new AtomicInteger(1000);
    private AtomicInteger batchOffset = new AtomicInteger(0);

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public AtomicInteger getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch.set(batch);
    }

    public AtomicInteger getBatchOffset() {
        return batchOffset;
    }

    public void setBatchOffset(int batchOffset) {
        this.batchOffset.set(batchOffset);
    }

    public Dialect getDialect() {
        return dialect;
    }

}

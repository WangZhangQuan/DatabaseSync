package com.wzq.core.connector;

import com.wzq.sql.type.Dialect;

public interface Target {
    Dialect getDialect();
    int getBatch();
    void setBacth(int batch);
}

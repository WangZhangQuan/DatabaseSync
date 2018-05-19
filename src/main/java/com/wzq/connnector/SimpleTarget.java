package com.wzq.connnector;

import com.wzq.core.connector.Target;
import com.wzq.sql.type.Dialect;

public class SimpleTarget implements Target {
    public Dialect getDialect() {
        return null;
    }

    public int getBatch() {
        return 0;
    }

    public void setBacth(int batch) {

    }
}

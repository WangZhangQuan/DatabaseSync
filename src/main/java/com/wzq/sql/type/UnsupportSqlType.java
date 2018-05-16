package com.wzq.sql.type;

public class UnsupportSqlType extends RuntimeException {
    public UnsupportSqlType() {
    }

    public UnsupportSqlType(String message) {
        super(message);
    }

    public UnsupportSqlType(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportSqlType(Throwable cause) {
        super(cause);
    }

    public UnsupportSqlType(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

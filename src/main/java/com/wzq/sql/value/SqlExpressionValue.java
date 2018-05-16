package com.wzq.sql.value;

import net.sf.jsqlparser.expression.LongValue;

public class SqlExpressionValue extends LongValue {
    public SqlExpressionValue(String value) {
        super("0");
        super.setStringValue(value);
    }
}

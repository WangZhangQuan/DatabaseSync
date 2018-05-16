package com.wzq.sql.value;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

import java.util.ArrayList;
import java.util.List;

public class ArrayValue extends LongValue {

    public List<Expression> es = new ArrayList<Expression>();

    public ArrayValue() {
        super("0");
    }
    public ArrayValue(List<Expression> es) {
        super("0");
        this.es = es;
        arrayToSql();
    }

    @Deprecated
    public ArrayValue(String value) {
        super(value);
    }

    public List<Expression> getEs() {
        return es;
    }

    public void setEs(List<Expression> es) {
        this.es = es;
    }

    private void arrayToSql() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        for (int i = 0; i < es.size(); ++i) {
            Expression e = es.get(i);
            buf.append(e == null ? new NullValue().toString() : e.toString());
            if (i < es.size() - 1) {
                buf.append(",");
            }
        }
        buf.append(")");
        super.setStringValue(buf.toString());
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        arrayToSql();
        super.accept(expressionVisitor);
    }

    @Override
    public String getStringValue() {
        arrayToSql();
        return super.getStringValue();
    }

    @Override
    public String toString() {
        arrayToSql();
        return super.toString();
    }

    @Override
    @Deprecated
    public long getValue() {
//        return super.getValue();
        return 0;
    }

    @Override
    @Deprecated
    public void setValue(long d) {
//        super.setValue(d);
    }

    @Override
    @Deprecated
    public void setStringValue(String string) {
//        super.setStringValue(string);
    }
}

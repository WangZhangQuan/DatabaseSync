package com.wzq.util;

import net.sf.jsqlparser.schema.Column;

import java.util.Comparator;

/**
 * 字段按照 字段名称排序
 */
public class ColumnComparator implements Comparator<Column> {

    public static final ColumnComparator COMPARATOR = new ColumnComparator();

    protected ColumnComparator() {
    }

    public int compare(Column o1, Column o2) {
        if (o1 == null && o2 != null) {
            return -1;
        }
        if (o1 != null && o2 == null) {
            return 1;
        }
        if (o1 == null && o2 == null) {
            return 0;
        }
        return o1.getWholeColumnName().compareTo(o2.getWholeColumnName());
    }
}

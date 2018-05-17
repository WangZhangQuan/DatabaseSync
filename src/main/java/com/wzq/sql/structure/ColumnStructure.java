package com.wzq.sql.structure;

public class ColumnStructure implements Comparable {
    private String name;
    private String programType;
    private Object value;

    public ColumnStructure(String name, String programType, Object value) {
        this.name = name;
        this.programType = programType;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof ColumnStructure) {
            ColumnStructure cs = (ColumnStructure) obj;
            if (name.equals(cs.name) && programType.equals(cs.programType)) {
                if (value == null && value == cs.value) {
                    return true;
                } else if (value != null && value.equals(cs.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int compareTo(Object o) {
        if (hashCode() > o.hashCode()) {
            return 1;
        } else {
            if (hashCode() == o.hashCode()) {
                return 0;
            }
            return -1;
        }
    }

    @Override
    protected Object clone() {
        return new ColumnStructure(name, programType, value);
    }
}

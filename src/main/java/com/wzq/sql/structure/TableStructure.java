package com.wzq.sql.structure;

import com.wzq.core.structure.Structure;
import com.wzq.util.KeyValue;

import java.util.*;

public class TableStructure implements Structure {
    private String name;
    private List<ColumnStructure> columns;

    public TableStructure(String name, List<ColumnStructure> columns) {
        this.name = name;
        this.columns = columns;
    }

    public TableStructure(String name, ColumnStructure... columns) {
        this.name = name;
        this.columns = Arrays.asList(columns);
    }

    public TableStructure(String name) {
        this.name = name;
    }

    public void sortColumns(){
        Collections.sort(columns);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof TableStructure) {
            TableStructure ts = (TableStructure) obj;
            if (name.equals(ts.name) && columns.size() == ts.columns.size()) {
                sortColumns();
                ts.sortColumns();
                for (int i = 0; i < columns.size(); i++) {
                    if (!columns.get(i).equals(ts.columns.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isValidate() {
        if (columns != null && columns.size() > 0) {
            return true;
        } else
            return false;
    }

    public KeyValue<Structure, Structure> differenceSet(Structure structure) {
        if (structure instanceof TableStructure) {
            TableStructure s = (TableStructure) structure;
            if (this.getName().equals(s.getName())) {
                Set<ColumnStructure> ics = new HashSet<ColumnStructure>(columns);
                Set<ColumnStructure> scs = new HashSet<ColumnStructure>(s.columns);
                ics.retainAll(scs); // 求出交集
                // 克隆原有数据
                TableStructure cts = (TableStructure) clone();
                TableStructure scts = (TableStructure) s.clone();
                // 移除交集部分
                cts.columns.removeAll(ics);
                scts.columns.removeAll(ics);
                // 返回差集
                return new KeyValue<Structure, Structure>(cts, scts);
            }
            throw new UnsupportedOperationException("Unsupport Structure differenceSet because table name not is same: I:" + this.getName() + "，Con side:" + s.getName());
        }
        throw new UnsupportedOperationException("Unsupport Structure differenceSet because type inconsistency: I:" + this.getClass().getName() + "，Con side:" + structure.getClass().getName());
    }

    @Override
    protected Object clone() {
        TableStructure ts = new TableStructure(name);
        if (isValidate()) {
            List<ColumnStructure> cs = new ArrayList<ColumnStructure>(columns.size());
            for (ColumnStructure c : columns) {
                cs.add((ColumnStructure) c.clone());
            }
        }
        return ts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnStructure> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnStructure> columns) {
        this.columns = columns;
    }
}

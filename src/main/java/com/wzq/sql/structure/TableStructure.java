package com.wzq.sql.structure;

import com.wzq.able.Nameable;
import com.wzq.able.Standardable;
import com.wzq.core.structure.Structure;
import com.wzq.util.KeyValue;
import com.wzq.util.NameUtils;

import java.util.*;

public class TableStructure implements Structure,Nameable,Standardable {
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

    public TableStructure() {
    }

    public TableStructure(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof TableStructure) {
            TableStructure ts = (TableStructure) obj;
            if (name.equals(ts.name) && columns.size() == ts.columns.size()) {
                for (ColumnStructure ic : columns) {
                    boolean flag = false;
                    for (ColumnStructure oc : ts.columns) {
                        if (ic.equals(oc)) {
                            flag = true;
                        }
                    }
                    if (!flag) {
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

    @Override
    public Object clone() {
        TableStructure ts = new TableStructure(name);
        if (isValidate()) {
            List<ColumnStructure> cs = new ArrayList<ColumnStructure>(columns.size());
            for (ColumnStructure c : columns) {
                cs.add((ColumnStructure) c.clone());
            }
            ts.columns = cs;
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

    public KeyValue<Structure, Structure> differenceSet(Structure structure) {
        TableStructure s = validateNecessaryUniformity(structure);
        // 验证有差集的必要性
        if (!isValidate() || !s.isValidate()) {
            return new KeyValue<Structure, Structure>(isValidate() ? (Structure) clone() : null, s.isValidate() ? (Structure) s.clone() : null);
        }
        // 克隆原有数据
        TableStructure tss = (TableStructure) clone();
        TableStructure stss = (TableStructure) s.clone();

        // 不想同表数据
        List<ColumnStructure> ds = NameUtils.difference(tss.columns, stss.columns);
        List<ColumnStructure> sds = NameUtils.difference(stss.columns, tss.columns);
        // 移除完全不同的表数据 不参与列比较
        tss.columns.removeAll(ds);
        stss.columns.removeAll(sds);
        // 存放差异表结构
        List<ColumnStructure> css = new ArrayList<ColumnStructure>();
        List<ColumnStructure> scss = new ArrayList<ColumnStructure>();
        // 计算差异表
        for (ColumnStructure t : tss.columns) {
            for (ColumnStructure st : stss.columns) {
                if (t.getName().equals(st.getName())) {
                    KeyValue<Structure, Structure> sskv = t.differenceSet(st);
                    if (sskv.getKey() != null) {
                        css.add((ColumnStructure) sskv.getKey());
                    }
                    if (sskv.getValue() != null) {
                        scss.add((ColumnStructure) sskv.getValue());
                    }
                }
            }
        }
        // 添加完全不相同的集合数据到差异集合中
        css.addAll(ds);
        scss.addAll(sds);
        // 将差异表设置回去
        tss.setColumns(css);
        stss.setColumns(scss);

        if (!tss.isValidate()) {
            tss = null;
        }
        if (!stss.isValidate()) {
            stss = null;
        }

        // 返回差异数据
        return new KeyValue<Structure, Structure>(tss, stss);
    }

    @Override
    public Structure intersection(Structure structure) {
        if (isValidate()) {
            TableStructure ts = validateNecessaryUniformity(structure);
            TableStructure tsc = (TableStructure) clone();
            // 存放需要的数据
            List<ColumnStructure> css = new ArrayList<ColumnStructure>();
            for (ColumnStructure cs : ts.columns) {
                for (ColumnStructure csx : tsc.columns) {
                    if (cs.getName().equals(csx.getName())) {
                        css.add((ColumnStructure) cs.intersection(csx));
                    }
                }

            }
            tsc.columns = css;
            return tsc;
        }
        return null;
    }

    @Override
    public void valueOf(Structure structure) {
        if (isValidate()) {
            TableStructure ms = validateNecessaryUniformity(structure);
            for (ColumnStructure cs : columns) {
                for (ColumnStructure csx : ms.columns) {
                    if (cs.getName().equals(csx.getName())) {
                        cs.valueOf(csx);
                    }
                }
            }
        }
    }

    private TableStructure validateNecessaryUniformity(Structure structure) {
        if (structure instanceof TableStructure) {
            TableStructure s = (TableStructure) structure;
            if (this.getName().equals(s.getName())) {
                return s;
            }
            throw new UnsupportedOperationException("Unsupport Structure differenceSet because name not is same: I:" + this.getName() + "，Con side:" + s.getName());
        }
        throw new UnsupportedOperationException("Unsupport Structure differenceSet because type inconsistency: I:" + this.getClass().getName() + "，Con side:" + structure.getClass().getName());
    }

    public void standardize() {
        if (isValidate()) {
            Map<String, ColumnStructure> std = new HashMap<String, ColumnStructure>();
            for (ColumnStructure c : columns) {
                std.put(c.getName(), c);
            }
            columns.clear();
            columns.addAll(std.values());
        }
    }
}

package com.wzq.sql.structure;

import com.wzq.able.Nameable;
import com.wzq.able.Standardable;
import com.wzq.core.structure.Structure;
import com.wzq.util.KeyValue;
import com.wzq.util.NameUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MappingStructure implements Structure, Nameable, Standardable {

    private String name;

    private List<TableStructure> tables;

    public KeyValue<Structure, Structure> differenceSet(Structure structure) {
        // 验证必要参数正确
        MappingStructure s = validateNecessaryUniformity(structure);
        // 验证有差集的必要性
        if (!isValidate() || !s.isValidate()) {
            return new KeyValue<Structure, Structure>(isValidate() ? (Structure) clone() : null, s.isValidate() ? (Structure) s.clone() : null);
        }
        // 克隆原有数据
        MappingStructure mss = (MappingStructure) clone();
        MappingStructure smss = (MappingStructure) s.clone();

        // 不想同表数据
        List<TableStructure> ds = NameUtils.difference(mss.tables, smss.tables);
        List<TableStructure> sds = NameUtils.difference(smss.tables, mss.tables);
        // 移除完全不同的表数据 不参与列比较
        mss.tables.removeAll(ds);
        smss.tables.removeAll(sds);
        // 存放差异表结构
        List<TableStructure> tss = new ArrayList<TableStructure>();
        List<TableStructure> stss = new ArrayList<TableStructure>();
        // 计算差异表
        for (TableStructure t : mss.tables) {
            for (TableStructure st : smss.tables) {
                if (t.getName().equals(st.getName())) {
                    KeyValue<Structure, Structure> sskv = t.differenceSet(st);
                    if (sskv.getKey() != null) {
                        tss.add((TableStructure) sskv.getKey());
                    }
                    if (sskv.getValue() != null) {
                        stss.add((TableStructure) sskv.getValue());
                    }
                }
            }
        }
        // 添加完全不相同的集合数据到差异集合中
        tss.addAll(ds);
        stss.addAll(sds);
        // 将差异表设置回去
        mss.setTables(tss);
        smss.setTables(stss);

        if (!mss.isValidate()) {
            mss = null;
        }
        if (!smss.isValidate()) {
            smss = null;
        }

        // 返回差异数据
        return new KeyValue<Structure, Structure>(mss, smss);
    }

    @Override
    public Structure intersection(Structure structure) {
        if (isValidate()) {
            MappingStructure ms = validateNecessaryUniformity(structure);
            MappingStructure msc = (MappingStructure) clone();
            // 存放需要的数据
            List<TableStructure> tss = new ArrayList<TableStructure>();
            for (TableStructure ts : msc.tables) {
                for (TableStructure tsx : ms.tables) {
                    if (ts.getName().equals(tsx.getName())) {
                        tss.add((TableStructure) ts.intersection(tsx));
                    }
                }

            }
            msc.tables = tss;
            return msc;
        }
        return null;
    }

    @Override
    public void valueOf(Structure structure) {
        if (isValidate()) {
            MappingStructure ms = validateNecessaryUniformity(structure);
            for (TableStructure ts : tables) {
                for (TableStructure tsx : ms.tables) {
                    if (ts.getName().equals(tsx.getName())) {
                        ts.valueOf(tsx);
                    }
                }
            }
        }
    }

    private MappingStructure validateNecessaryUniformity(Structure structure) {
        if (structure instanceof MappingStructure) {
            MappingStructure s = (MappingStructure) structure;
            if (this.getName().equals(s.getName())) {
                return s;
            }
            throw new UnsupportedOperationException("Unsupport Structure differenceSet because name not is same: I:" + this.getName() + "，Con side:" + s.getName());
        }
        throw new UnsupportedOperationException("Unsupport Structure differenceSet because type inconsistency: I:" + this.getClass().getName() + "，Con side:" + structure.getClass().getName());
    }

    @Override
    public Object clone() {
        MappingStructure ms = new MappingStructure(name);
        if (isValidate()) {
            List<TableStructure> ts = new ArrayList<TableStructure>();
            for (TableStructure t : tables) {
                ts.add((TableStructure) t.clone());
            }
            ms.setTables(ts);
        }
        return ms;
    }

    private boolean isValidate() {
        if (this.tables != null && this.tables.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void standardize() {
        if (isValidate()) {
            Map<String, TableStructure> std = new HashMap<String, TableStructure>();
            for (TableStructure t : tables) {
                TableStructure ts = std.put(t.getName(), t);
                if (ts != null) {
                    t.getColumns().addAll(0, ts.getColumns());
                }
            }
            for (TableStructure ts : std.values()) {
                ts.standardize();
            }
            tables.clear();
            tables.addAll(std.values());
        }
    }

    public MappingStructure() {
    }

    public MappingStructure(String name) {
        this.name = name;
    }

    public MappingStructure(String name, List<TableStructure> tables) {
        this.name = name;
        this.tables = tables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableStructure> getTables() {
        return tables;
    }

    public void setTables(List<TableStructure> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof MappingStructure) {
            MappingStructure ms = (MappingStructure) obj;
            if (this.name.equals(ms.getName())) {
                for (TableStructure t : tables) {
                    boolean flag = false;
                    for (TableStructure st : ms.tables) {
                        if (t.equals(st)) {
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
}

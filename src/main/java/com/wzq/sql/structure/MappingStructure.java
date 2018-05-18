package com.wzq.sql.structure;

import com.wzq.able.Nameable;
import com.wzq.able.Standardable;
import com.wzq.command.SqlGeneratorCommandArgs;
import com.wzq.core.command.Command;
import com.wzq.core.command.CommandArgs;
import com.wzq.core.command.Opreator;
import com.wzq.core.structure.Structure;
import com.wzq.sql.type.Dialect;
import com.wzq.util.KeyValue;
import com.wzq.util.NameUtils;

import java.util.*;


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
        MappingStructure s = cast(structure);
        if (this.getName().equals(s.getName())) {
            return s;
        }
        throw new UnsupportedOperationException("Unsupport Structure differenceSet because name not is same: I:" + this.getName() + "，Con side:" + s.getName());
    }

    public static MappingStructure cast(Structure structure) {
        if (structure instanceof MappingStructure) {
            return (MappingStructure) structure;
        }
        throw new UnsupportedOperationException("Unsupport Structure differenceSet because type inconsistency: I:" + MappingStructure.class.getName() + "，Con side:" + structure.getClass().getName());
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

    public TableStructure findTable(String name) {
        if (isValidate()) {
            for (TableStructure ts : tables) {
                if (ts.getName().equals(name)) {
                    return ts;
                }
            }
        }
        return null;
    }

    public String findTableName(String name) {
        TableStructure t = findTable(name);
        if (t != null) {
            return t.getName();
        }
        return null;
    }

    public String[] findTableAndColumns(String tableName, String[] columns) {
        Set<String> rs = new HashSet<String>();
        if (isValidate()) {
            TableStructure ts = findTable(tableName);
            if (ts != null) {
                List<ColumnStructure> css = ts.getColumns();
                for (ColumnStructure cs : css) {
                    for (String c : columns) {
                        if (cs.getName().equals(c)) {
                            rs.add(cs.getName());
                        }
                    }
                }
            }
        }
        return rs.toArray(new String[rs.size()]);
    }

    public Map<String, Object> findTableAndColumnValues(String tableName, String[] columns) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        if (isValidate()) {
            TableStructure ts = findTable(tableName);
            if (ts != null) {
                List<ColumnStructure> css = ts.getColumns();
                for (ColumnStructure cs : css) {
                    for (String c : columns) {
                        if (cs.getName().equals(c)) {
                            result.put(cs.getName(), cs.getValue());
                        }
                    }
                }
            }
        }
        return result;
    }

    public Command newUpdateCommand(String itName, String[] updateItColumns, String[] whereColumns, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, updateItColumns)) return null;
        return new Command(Opreator.UPDATE, SqlGeneratorCommandArgs.newUpdateArgs(itName, findTableAndColumnValues(itName, updateItColumns), findTableAndColumnValues(itName, whereColumns), otNames));
    }

    public Command newReverseUpdateCommand(String itName, String[] updateItColumns, String[] whereColumns, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, updateItColumns)) return null;
        return new Command(Opreator.REVERSE_UPDATE, SqlGeneratorCommandArgs.newUpdateArgs(itName, findTableAndColumnValues(itName, updateItColumns), findTableAndColumnValues(itName, whereColumns), otNames));
    }

    public Command newInsertCommand(String itName, String[] insertItColumns, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, insertItColumns)) return null;
        return new Command(Opreator.NEW, SqlGeneratorCommandArgs.newInsertArgs(itName, findTableAndColumnValues(itName, insertItColumns), otNames));
    }

    public Command newReverseInsertCommand(String itName, String[] insertItColumns, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, insertItColumns)) return null;
        return new Command(Opreator.REVERSE_NEW, SqlGeneratorCommandArgs.newInsertArgs(itName, findTableAndColumnValues(itName, insertItColumns), otNames));
    }

    public Command newDeleteCommand(String itName, String[] whereColumns, String... otNames) {
        if (!validateNecessaryTable(itName)) return null;
        return new Command(Opreator.DELETE, SqlGeneratorCommandArgs.newDeleteArgs(itName, findTableAndColumnValues(itName, whereColumns), otNames));

    }

    public Command newReverseDeleteCommand(String itName, String[] whereColumns, String... otNames) {
        if (!validateNecessaryTable(itName)) return null;
        return new Command(Opreator.REVERSE_DELETE, SqlGeneratorCommandArgs.newDeleteArgs(itName, findTableAndColumnValues(itName, whereColumns), otNames));
    }

    public Command newSelectCommand(String itName, String[] selectItColumns, String[] whereColumns, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, selectItColumns)) return null;
        return new Command(Opreator.SHOW, SqlGeneratorCommandArgs.newSelectArgs(itName, selectItColumns, findTableAndColumnValues(itName, whereColumns), otNames));
    }

    public Command newReverseSelectCommand(String itName, String[] selectItColumns, String[] whereColumns, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, selectItColumns)) return null;
        return new Command(Opreator.REVERSE_SHOW, SqlGeneratorCommandArgs.newSelectArgs(itName, selectItColumns, findTableAndColumnValues(itName, whereColumns), otNames));
    }

    public Command newCreateCommand(String itName, String[] createItColumns, Dialect dialect, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, createItColumns)) return null;
        return new Command(Opreator.CREATE, SqlGeneratorCommandArgs.newCreateArgs(itName, createItColumns, dialect, otNames));
    }

    public Command newReverseCreateCommand(String itName, String[] createItColumns, Dialect dialect, String... otNames) {
        if (!validateNecessaryTableAndColumns(itName, createItColumns)) return null;
        return new Command(Opreator.REVERSE_CREATE, SqlGeneratorCommandArgs.newCreateArgs(itName, createItColumns, dialect, otNames));
    }

    public Command newDropCommand(String itName, String... otNames) {
        if (!validateNecessaryTable(itName)) return null;
        return new Command(Opreator.DROP, SqlGeneratorCommandArgs.newDropArgs(itName, otNames));
    }

    public Command newReverseDropCommand(String itName, String... otNames) {
        if (!validateNecessaryTable(itName)) return null;
        return new Command(Opreator.REVERSE_DROP, SqlGeneratorCommandArgs.newDropArgs(itName, otNames));
    }

    private boolean validateNecessaryTable(String tableName) {
        TableStructure ts = findTable(tableName);
        if (ts != null) {
            return true;
        }
        return false;
    }

    private boolean validateNecessaryTableAndColumns(String tableName, String[] columnNames) {
        TableStructure ts = findTable(tableName);
        if (ts != null) {
            String[] cs = findTableAndColumns(ts.getName(), columnNames);
            if (cs != null && cs.length > 0) {
                return true;
            }
        }
        return false;
    }
}

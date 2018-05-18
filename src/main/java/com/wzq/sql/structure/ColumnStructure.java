package com.wzq.sql.structure;

import com.wzq.able.Nameable;
import com.wzq.core.structure.Structure;
import com.wzq.util.KeyValue;

public class ColumnStructure implements Structure,Nameable {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object clone() {
        return new ColumnStructure(name, programType, value);
    }

    public KeyValue<Structure, Structure> differenceSet(Structure structure) {
        ColumnStructure scs = validateNecessaryUniformity(structure);
        ColumnStructure cs = null;
        ColumnStructure scsx = null;
        if (!equals(structure)) {
            cs = (ColumnStructure) this.clone();
            scsx = (ColumnStructure) scs.clone();
        }
        return new KeyValue<Structure, Structure>(cs, scsx);
    }

    public Structure intersection(Structure structure) {
        ColumnStructure scs = validateNecessaryUniformity(structure);
        if (equals(scs)) {
            return (Structure) clone();
        }
        return null;
    }

    public void valueOf(Structure structure) {
        ColumnStructure scs = validateNecessaryUniformity(structure);
        if (name.equals(scs.name)) {
            programType = scs.programType;
            value = scs.value;
        }
    }

    private ColumnStructure validateNecessaryUniformity(Structure structure) {
        return cast(structure);
    }

    public static ColumnStructure cast(Structure structure) {
        if (structure instanceof ColumnStructure) {
            return (ColumnStructure) structure;
        }
        throw new UnsupportedOperationException("Unsupport Structure differenceSet because type inconsistency: I:" + ColumnStructure.class.getName() + "，Con side:" + structure.getClass().getName());
    }
}

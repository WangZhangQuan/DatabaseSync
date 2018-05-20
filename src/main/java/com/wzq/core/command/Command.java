package com.wzq.core.command;

import com.wzq.core.structure.Structure;
import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.MappingStructure;

public class Command {
    private Opreator opreator = Opreator.SHOW;

    private Structure structure;

    private Mapping mapping;

    private CommandArgs args;

    public Command(Opreator opreator, CommandArgs args, Structure structure, Mapping mapping) {
        this.opreator = opreator;
        this.args = args;
        this.structure = structure;
        this.mapping = mapping;
    }

    public Command() {

    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Command(Opreator opreator) {
        this.opreator = opreator;
    }

    public Opreator getOpreator() {
        return opreator;
    }

    public void setOpreator(Opreator opreator) {
        this.opreator = opreator;
    }

    public CommandArgs getArgs() {
        return args;
    }

    public void setArgs(CommandArgs args) {
        this.args = args;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }
}

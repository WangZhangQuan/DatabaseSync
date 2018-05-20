package com.wzq.core.connector;

import com.wzq.core.command.Command;
import com.wzq.core.structure.Structure;
import com.wzq.mapping.Mapping;
import com.wzq.sql.type.Dialect;

import java.io.Closeable;

public interface Target extends Closeable {
    Dialect getDialect();
    int getBatch();
    void setBatch(int batch);
    Structure getStructure(String[] tables, Mapping mapping);
    void commit();
    void execCUD(Command command);
    Iterable<Structure> execRead(Command command);
}

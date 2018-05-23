package com.wzq.core.connector;

import com.wzq.core.command.Command;
import com.wzq.core.structure.Structure;
import com.wzq.mapping.Mapping;
import com.wzq.sql.type.Dialect;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicInteger;

public interface Target extends Closeable {
    Dialect getDialect();
    AtomicInteger getBatch();
    AtomicInteger getBatchOffset();
    Structure getStructure(String[] tables, Mapping mapping);
    void commit();
    void execCUD(Command command);
    Iterable<Structure> execRead(Command command);
}

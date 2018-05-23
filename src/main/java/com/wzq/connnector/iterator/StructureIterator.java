package com.wzq.connnector.iterator;

import com.wzq.core.structure.Structure;

import java.util.Iterator;

public interface StructureIterator extends Iterator<Structure> {
    void close() throws Exception;
}

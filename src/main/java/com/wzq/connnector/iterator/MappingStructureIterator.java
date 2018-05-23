package com.wzq.connnector.iterator;

import com.wzq.core.structure.Structure;

public class MappingStructureIterator implements StructureIterator {
    public void close() throws Exception {
        // TODO 未实现的MappingStructureInterator
    }

    public boolean hasNext() {
        return false;
    }

    public Structure next() {
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}

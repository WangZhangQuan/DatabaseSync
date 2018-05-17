package com.wzq.core.structure;

import com.wzq.util.KeyValue;

public interface Structure {
    /**
     * 得到差集
     * @param structure
     * @return key: 己方有对方没有，value: 己方没有对方有
     */
    KeyValue<Structure, Structure> differenceSet(Structure structure);
}

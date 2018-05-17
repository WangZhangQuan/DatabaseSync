package com.wzq.core.structure;

import com.wzq.util.KeyValue;

public interface Structure {
    /**
     * 得到差集
     * @param structure
     * @return key: 己方有对方没有，value: 己方没有对方有
     */
    KeyValue<Structure, Structure> differenceSet(Structure structure);

    /**
     * 得到交集
     * @param structure
     * @return
     */
    Structure intersection(Structure structure);

    /**
     * 从另一个Structure中赋值给自己
     * @param structure
     */
    void valueOf(Structure structure);
}

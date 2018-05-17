package com.wzq.manager;

import com.wzq.core.generator.Generator;
import com.wzq.mapping.Mapping;

import java.util.List;

/**
 * Mapping管理器
 */
public interface MappingManager {
    /**
     * Mapping默认方向的生成
     * @param mappingName
     * @return
     */
    Generator getGenerator(String mappingName);

    /**
     * Mapping反方向的生成
     * @param mappingName
     * @return
     */
    Generator getReverseGenerator(String mappingName);

    String[] getAllMappingNames();

    void setMappings(List<Mapping> mappings);
    List<Mapping> getMappings();
}

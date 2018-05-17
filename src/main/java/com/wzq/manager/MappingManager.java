package com.wzq.manager;

import com.wzq.able.Standardable;
import com.wzq.core.generator.Generator;
import com.wzq.mapping.Mapping;

import java.util.List;

/**
 * Mapping管理器
 */
public interface MappingManager extends Standardable {
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

    List<Mapping> getMappings();
}

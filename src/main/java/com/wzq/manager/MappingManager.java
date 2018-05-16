package com.wzq.manager;

import com.wzq.generator.MappingSqlGenerator;
import com.wzq.mapping.Mapping;

import java.util.List;

/**
 * Mapping管理器
 */
public interface MappingManager {
    /**
     * Mapping默认方向的SQL生成
     * @param mappingName
     * @return
     */
    MappingSqlGenerator getSqlGenerator(String mappingName);

    /**
     * Mapping反方向的SQL生成
     * @param mappingName
     * @return
     */
    MappingSqlGenerator getReverseSqlGenerator(String mappingName);

    void setMappings(List<Mapping> mappings);
    List<Mapping> getMappings();
}

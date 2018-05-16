package com.wzq.manager.impl;

import com.wzq.generator.MappingSqlGenerator;
import com.wzq.generator.impl.SimpleMappingSqlGenerator;
import com.wzq.manager.MappingManager;
import com.wzq.mapping.Mapping;

import java.util.ArrayList;
import java.util.List;

public class SimpleMappingManager implements MappingManager {

    public SimpleMappingManager(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    private List<Mapping> mappings;

    public MappingSqlGenerator getSqlGenerator(String mappingName) {
        return new SimpleMappingSqlGenerator(findMappingAndFusionByName(mappingName));
    }

    public MappingSqlGenerator getReverseSqlGenerator(String mappingName) {
        return new SimpleMappingSqlGenerator(findMappingAndFusionByName(mappingName).generateSwapBothSides());
    }

    /**
     * 通过名称获得Mapping集合
     * @param mappingName
     * @return
     */
    public List<Mapping> findMappingsByName(String mappingName){
        List<Mapping> rs = new ArrayList<Mapping>();
        for (Mapping mapping : this.mappings) {
            if(mapping.getName().equals(mappingName)){
                rs.add(mapping);
            }
        }
        return rs;
    }

    /**
     * 通过名称获得融合的Mapping
     * @param mappingName
     * @return
     */
    public Mapping findMappingAndFusionByName(String mappingName){
        List<Mapping> mappings = findMappingsByName(mappingName);
        return Mapping.fusionMappings(mappings.toArray(new Mapping[mappings.size()]));
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }
}

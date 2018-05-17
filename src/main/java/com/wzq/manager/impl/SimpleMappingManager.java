package com.wzq.manager.impl;

import com.wzq.generator.MappingSqlGenerator;
import com.wzq.generator.impl.SimpleMappingSqlGenerator;
import com.wzq.manager.MappingManager;
import com.wzq.mapping.Mapping;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleMappingManager implements MappingManager {

    private List<Mapping> mappings = new CopyOnWriteArrayList<Mapping>();

    public MappingSqlGenerator getGenerator(String mappingName) {
        return new SimpleMappingSqlGenerator(findMappingAndFusionByName(mappingName));
    }

    public MappingSqlGenerator getReverseGenerator(String mappingName) {
        return new SimpleMappingSqlGenerator(findMappingAndFusionByName(mappingName).generateSwapBothSides());
    }

    /**
     * 通过名称获得Mapping集合
     *
     * @param mappingName
     * @return
     */
    public List<Mapping> findMappingsByName(String mappingName) {
        List<Mapping> rs = new ArrayList<Mapping>();
        for (Mapping mapping : this.mappings) {
            if (mapping.getName().equals(mappingName)) {
                rs.add(mapping);
            }
        }
        return rs;
    }

    /**
     * 通过名称获得融合的Mapping
     *
     * @param mappingName
     * @return
     */
    public Mapping findMappingAndFusionByName(String mappingName) {
        List<Mapping> mappings = findMappingsByName(mappingName);
        return Mapping.fusionMappings(mappings.toArray(new Mapping[mappings.size()]));
    }

    public String[] getAllMappingNames() {
        if (this.mappings != null && this.mappings.size() > 0) {
            Set<String> names = new HashSet<String>();
            for (Mapping mapping : mappings) {
                names.add(mapping.getName());
            }
            return names.toArray(new String[names.size()]);
        }
        return new String[0];
    }

    public void standardize() {
        if (isValidate()) {
            Map<String, Mapping> std = new HashMap<String, Mapping>();
            for (Mapping m : mappings) {
                Mapping mx = std.put(m.getName(), m);
                if (mx != null) {
                    // 进行融合
                    std.put(m.getName(), m.fusion(mx));
                }
            }
            mappings.clear();
            mappings.addAll(std.values());
        }
    }

    public boolean isValidate() {
        if (mappings != null && mappings.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Mapping> getMappings() {
        return mappings;
    }
}

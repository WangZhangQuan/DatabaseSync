package com.wzq.executor;

import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.structure.Attach;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.manager.MappingManager;
import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.CoverOpreater;
import com.wzq.sql.structure.MappingAttach;
import com.wzq.sql.structure.MappingStructure;
import com.wzq.sql.structure.TableStructure;

import java.util.ArrayList;
import java.util.List;

public class SimpleSyncOpreatorExecutor implements SyncOpreatorExecutor {

    public void execute(SyncOpreator syncOpreator, MappingManager mappingManager) {

        MappingAttach ma = canUse(syncOpreator.getAttach());

        List<MappingStructure> mappingStructures = ma.getMappingStructures();
        List<CoverOpreater> coverOpreaters = ma.getCoverOpreaters();

        // 规范化MappingManager
        mappingManager.standardize();
        List<Mapping> mappings = mappingManager.getMappings();

        if (ma.isSyncAllMappings()) {
            mappingStructures = new ArrayList<MappingStructure>();
            coverOpreaters = null;
            for (Mapping m : mappings) {
                mappingStructures.add(m.getIMappingStructure());
            }
            // TODO 同步操作
        }
    }

    private static MappingAttach canUse(Attach attach) {
        MappingAttach ma = null;
        if (attach instanceof MappingAttach) {
            ma = (MappingAttach) attach;
        } else {
            ma = MappingAttach.getInstance();
        }
        return ma;
    }
}

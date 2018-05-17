package com.wzq.executor;

import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.structure.Attach;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.manager.MappingManager;
import com.wzq.sql.structure.MappingAttach;

public class SimpleSyncOpreatorExecutor implements SyncOpreatorExecutor {

    public void execute(SyncOpreator syncOpreator, MappingManager mappingManager) {
        MappingAttach ma = canUse(syncOpreator.getAttach());
        // TODO
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

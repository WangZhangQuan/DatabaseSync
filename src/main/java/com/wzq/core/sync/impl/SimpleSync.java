package com.wzq.core.sync.impl;

import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.sync.Sync;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.manager.MappingManager;

public class SimpleSync extends Sync {

    public void sync(SyncOpreator syncOpreator, SyncOpreatorExecutor syncOpreatorExecutor, MappingManager mappingManager) {
        syncOpreatorExecutor.execute(syncOpreator, mappingManager);
    }
}

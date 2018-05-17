package com.wzq.core.executor;

import com.wzq.core.sync.SyncOpreator;
import com.wzq.manager.MappingManager;

public interface SyncOpreatorExecutor {
    void execute(SyncOpreator syncOpreator, MappingManager mappingManager);
}

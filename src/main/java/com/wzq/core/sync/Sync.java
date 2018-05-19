package com.wzq.core.sync;

import com.wzq.core.connector.Connector;
import com.wzq.core.context.SyncContext;
import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.manager.MappingManager;

public abstract class Sync {
    public abstract void sync(SyncOpreator syncOpreator, SyncContext syncContext);
}

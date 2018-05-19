package com.wzq.core.executor;

import com.wzq.core.context.SyncContext;
import com.wzq.core.sync.SyncOpreator;

public interface SyncOpreatorExecutor {
    void execute(SyncOpreator syncOpreator, SyncContext syncContext);
}

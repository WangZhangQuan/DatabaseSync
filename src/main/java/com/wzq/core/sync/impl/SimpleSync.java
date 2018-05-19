package com.wzq.core.sync.impl;

import com.wzq.core.context.SyncContext;
import com.wzq.core.listener.SyncListener;
import com.wzq.core.sync.Sync;
import com.wzq.core.sync.SyncOpreator;

import java.util.List;

public class SimpleSync extends Sync {

    public void sync(SyncOpreator syncOpreator, SyncContext syncContext) {
        // 触发前置监听器
        handlerSyncListner(syncOpreator, syncContext, true);
        // 执行同步操作
        syncContext.getSyncOpreatorExecutor().execute(syncOpreator, syncContext);
        // 触发后置监听器
        handlerSyncListner(syncOpreator, syncContext, false);
    }

    private void handlerSyncListner(SyncOpreator syncOpreator, SyncContext syncContext, boolean pa) {
        List<SyncListener> syncListeners = syncContext.getSyncListeners();
        for (SyncListener syncListener : syncListeners) {
            if (syncListener != null) {
                if (pa) {
                    syncListener.preSync(syncOpreator, syncContext);
                } else {
                    syncListener.afterSync(syncOpreator, syncContext);
                }
            }
        }
    }
}

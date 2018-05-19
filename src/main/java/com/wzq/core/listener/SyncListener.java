package com.wzq.core.listener;

import com.wzq.core.context.SyncContext;
import com.wzq.core.sync.SyncOpreator;

/**
 * 同步监听器
 */
public interface SyncListener extends Listener {
    /**
     * 同步之前的监听器
     * @param syncOpreator
     * @param syncContext
     * @return
     */
    void preSync(SyncOpreator syncOpreator, SyncContext syncContext);

    /**
     * 同步之后的监听器
     * @param syncOpreator
     * @param syncContext
     */
    void afterSync(SyncOpreator syncOpreator, SyncContext syncContext);
}

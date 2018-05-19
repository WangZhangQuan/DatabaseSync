package com.wzq.core.listener;

import com.wzq.core.context.SyncTableContext;
import com.wzq.core.sync.SyncOpreator;

/**
 * Table的同步监听器
 */
public interface SyncTableListener extends Listener {
    /**
     * 同步之前的监听器
     * @param syncOpreator
     * @param syncContext
     * @return
     */
    void preSync(SyncOpreator syncOpreator, SyncTableContext syncContext);

    /**
     * 同步之后的监听器
     * @param syncOpreator
     * @param syncContext
     */
    void afterSync(SyncOpreator syncOpreator, SyncTableContext syncContext);
}

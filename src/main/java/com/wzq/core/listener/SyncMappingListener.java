package com.wzq.core.listener;

import com.wzq.core.context.SyncMappingContext;
import com.wzq.core.sync.SyncOpreator;

/**
 * Mapping的同步监听器
 */
public interface SyncMappingListener extends Listener {
    /**
     * 同步之前的监听器
     * @param syncOpreator
     * @param syncContext
     * @return
     */
    void preSync(SyncOpreator syncOpreator, SyncMappingContext syncContext);

    /**
     * 同步之后的监听器
     * @param syncOpreator
     * @param syncContext
     */
    void afterSync(SyncOpreator syncOpreator, SyncMappingContext syncContext);
}

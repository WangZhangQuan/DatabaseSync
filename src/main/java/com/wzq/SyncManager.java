package com.wzq;

import com.wzq.core.connector.Connector;
import com.wzq.core.context.SyncContext;
import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.listener.Listener;
import com.wzq.core.sync.Sync;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.core.sync.impl.SimpleSync;
import com.wzq.executor.SimpleSyncOpreatorExecutor;
import com.wzq.manager.MappingManager;
import com.wzq.sql.structure.MappingAttach;

public class SyncManager {

    private SyncContext syncContext;

    public SyncManager(SyncContext syncContext) {
        this.syncContext = syncContext;
    }

    public SyncManager(MappingManager mappingManager, Connector connector) {
        this.syncContext = new SyncContext(mappingManager, connector);
    }

    public void sync() {
        syncContext.getSync().sync(SyncContext.SYNC_OPREATOR, syncContext);
    }

    public void sync(SyncOpreator syncOpreator) {
        syncContext.getSync().sync(syncOpreator, syncContext);
    }

    public void sync(SyncOpreator syncOpreator, MappingManager mappingManager) {
        syncContext.getSync().sync(syncOpreator, syncContext);
    }

    public Sync getSync() {
        return syncContext.getSync();
    }

    public void setSync(Sync sync) {
        syncContext.setSync(sync);
    }

    public SyncOpreatorExecutor getSyncOpreatorExecutor() {
        return syncContext.getSyncOpreatorExecutor();
    }

    public void setSyncOpreatorExecutor(SyncOpreatorExecutor syncOpreatorExecutor) {
        syncContext.setSyncOpreatorExecutor(syncOpreatorExecutor);
    }

    public static SyncOpreator getSyncOpreator() {
        return SyncContext.SYNC_OPREATOR;
    }

    public MappingManager getMappingManager() {
        return syncContext.getMappingManager();
    }

    public void setMappingManager(MappingManager mappingManager) {
        syncContext.setMappingManager(mappingManager);
    }

    public boolean addListener(Listener listener) {
        return syncContext.addListener(listener);
    }
    public boolean removeListener(Listener listener) {
        return syncContext.removeListener(listener);
    }
}

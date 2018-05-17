package com.wzq;

import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.sync.Sync;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.core.sync.impl.SimpleSync;
import com.wzq.executor.SimpleSyncOpreatorExecutor;
import com.wzq.manager.MappingManager;
import com.wzq.sql.structure.MappingAttach;

public class SyncManager {
    private Sync sync = new SimpleSync();
    private SyncOpreatorExecutor syncOpreatorExecutor = new SimpleSyncOpreatorExecutor();
    private static final SyncOpreator SYNC_OPREATOR = new SyncOpreator(0, 1, MappingAttach.getInstance());
    private MappingManager mappingManager;

    public void sync() {
        sync.sync(SYNC_OPREATOR, syncOpreatorExecutor, mappingManager);
    }

    public void sync(SyncOpreator syncOpreator) {
        sync.sync(syncOpreator, syncOpreatorExecutor, mappingManager);
    }

    public void sync(SyncOpreator syncOpreator, MappingManager mappingManager) {
        sync.sync(syncOpreator, syncOpreatorExecutor, mappingManager);
    }

    public Sync getSync() {
        return sync;
    }

    public void setSync(Sync sync) {
        this.sync = sync;
    }

    public SyncOpreatorExecutor getSyncOpreatorExecutor() {
        return syncOpreatorExecutor;
    }

    public void setSyncOpreatorExecutor(SyncOpreatorExecutor syncOpreatorExecutor) {
        this.syncOpreatorExecutor = syncOpreatorExecutor;
    }

    public static SyncOpreator getSyncOpreator() {
        return SYNC_OPREATOR;
    }

    public MappingManager getMappingManager() {
        return mappingManager;
    }

    public void setMappingManager(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }
}

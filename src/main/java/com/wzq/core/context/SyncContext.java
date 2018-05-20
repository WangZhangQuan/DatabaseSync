package com.wzq.core.context;

import com.wzq.core.connector.Connector;
import com.wzq.core.executor.SyncOpreatorExecutor;
import com.wzq.core.listener.Listener;
import com.wzq.core.listener.SyncListener;
import com.wzq.core.listener.SyncMappingListener;
import com.wzq.core.listener.SyncTableListener;
import com.wzq.core.sync.Sync;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.core.sync.impl.SimpleSync;
import com.wzq.executor.SimpleSyncOpreatorExecutor;
import com.wzq.manager.MappingManager;
import com.wzq.sql.structure.MappingAttach;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SyncContext implements Context {
    private Sync sync = new SimpleSync();
    private SyncOpreatorExecutor syncOpreatorExecutor = new SimpleSyncOpreatorExecutor();
    public static final SyncOpreator SYNC_OPREATOR = new SyncOpreator(MappingAttach.getInstance());
    private MappingManager mappingManager;
    private Connector connector;
    private List<SyncListener> syncListeners = new ArrayList<SyncListener>();
    private List<SyncMappingListener> syncMappingListener = new ArrayList<SyncMappingListener>();
    private List<SyncTableListener> syncTableListener = new ArrayList<SyncTableListener>();

    public SyncContext(MappingManager mappingManager, Connector connector) {
        this.mappingManager = mappingManager;
        this.connector = connector;
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

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public boolean addListener(Listener listener) {
        if (listener instanceof SyncListener && !syncListeners.contains(listener)) {
            return syncListeners.add((SyncListener) listener);
        } else if (listener instanceof SyncMappingListener && !syncMappingListener.contains(listener)) {
            return syncMappingListener.add((SyncMappingListener) listener);
        } else if (listener instanceof SyncTableListener && !syncTableListener.contains(listener)) {
            return syncTableListener.add((SyncTableListener) listener);
        } else {
            return false;
        }
    }

    public boolean removeListener(Listener listener) {
        if (listener instanceof SyncListener) {
            return syncListeners.remove((SyncListener) listener);
        } else if (listener instanceof SyncMappingListener) {
            return syncMappingListener.remove((SyncMappingListener) listener);
        } else if (listener instanceof SyncTableListener) {
            return syncTableListener.remove((SyncTableListener) listener);
        } else {
            return false;
        }
    }

    public List<SyncListener> getSyncListeners() {
        return syncListeners;
    }

    public void setSyncListeners(List<SyncListener> syncListeners) {
        this.syncListeners = syncListeners;
    }

    public List<SyncMappingListener> getSyncMappingListener() {
        return syncMappingListener;
    }

    public void setSyncMappingListener(List<SyncMappingListener> syncMappingListener) {
        this.syncMappingListener = syncMappingListener;
    }

    public List<SyncTableListener> getSyncTableListener() {
        return syncTableListener;
    }

    public void setSyncTableListener(List<SyncTableListener> syncTableListener) {
        this.syncTableListener = syncTableListener;
    }

    public void valueOf(Context context) {
        if (context instanceof SyncContext) {
            SyncContext sc = (SyncContext) context;
            sync = sc.sync;
            syncOpreatorExecutor = sc.syncOpreatorExecutor;
            mappingManager = sc.mappingManager;
            connector = sc.connector;
            syncListeners = sc.syncListeners;
            syncMappingListener = sc.syncMappingListener;
            syncTableListener = sc.syncTableListener;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

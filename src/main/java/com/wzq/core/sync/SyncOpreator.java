package com.wzq.core.sync;

import com.wzq.core.structure.Attach;

public class SyncOpreator {

    /**
     * 默认缓存迪卡尔个数
     */
    private int cacheMaxCount = 200000;

    private Attach attach;

    public SyncOpreator() {
    }

    public SyncOpreator(Attach attach) {
        this.attach = attach;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    public int getCacheMaxCount() {
        return cacheMaxCount;
    }

    public void setCacheMaxCount(int cacheMaxCount) {
        this.cacheMaxCount = cacheMaxCount;
    }
}

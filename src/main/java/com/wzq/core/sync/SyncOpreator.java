package com.wzq.core.sync;

import com.wzq.core.structure.Attach;

public class SyncOpreator {
    /**
     * 己方同步级别 级别高的覆盖级别低的
     */
    private int imLevel = 0;
    /**
     * 对方同步级别 级别高的覆盖级别低的
     */
    private int omLevel = 1;

    private Attach attach;

    public SyncOpreator(int imLevel, int omLevel, Attach attach) {
        this.imLevel = imLevel;
        this.omLevel = omLevel;
        this.attach = attach;
    }

    public int getImLevel() {
        return imLevel;
    }

    public void setImLevel(int imLevel) {
        this.imLevel = imLevel;
    }

    public int getOmLevel() {
        return omLevel;
    }

    public void setOmLevel(int omLevel) {
        this.omLevel = omLevel;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }
}

package com.wzq.sql.structure;

import java.util.*;

public class DownTableRelation implements Iterable<DownTableRelation> {

    private TableStructure ts;

    private DownTableRelation parentDtr;

    private List<DownTableRelation> dtrs = new ArrayList<DownTableRelation>();

    private Set<DownTableRelation> allDtrs = new HashSet<DownTableRelation>();

    public DownTableRelation(TableStructure ts) {
        this.ts = ts;
    }

    public TableStructure getTs() {
        return ts;
    }

    public void setTs(TableStructure ts) {
        this.ts = ts;
    }

    public boolean validateAdd(DownTableRelation dtr) {
        if (!allDtrs.contains(dtr)) {
            if (dtr.parentDtr != null) {
                throw new RuntimeException("Already under a certain node: " + dtr);
            }
            dtr.parentDtr = this;
            dtrs.add(dtr);
            allDtrs.add(dtr);
            dtr.allDtrs = allDtrs;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  DownTableRelation) {
            return this.getTs().getName().equals(((DownTableRelation) obj).getTs().getName());
        }
        return super.equals(obj);
    }

    public Iterator<DownTableRelation> iterator() {
        return dtrs.iterator();
    }

    public void clearDowns() {
        for (DownTableRelation dtr : dtrs) {
            dtr.parentDtr = null;
            dtr.clearDowns();
        }
        allDtrs.removeAll(dtrs);
        dtrs.clear();
    }
}

package com.wzq.sql.structure;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
        if (!contains(dtr)) {
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

    public boolean contains(DownTableRelation dtr) {
        for (DownTableRelation allDtr : allDtrs) {
            if (allDtr.getTs().getName().equals(dtr.getTs().getName())) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof  DownTableRelation) {
//            return this.getTs().getName().equals(((DownTableRelation) obj).getTs().getName());
//        }
//        return super.equals(obj);
//    }

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

    public List<DownTableRelation> findRootDownTableRelations(String rootDtrName) {
        List<DownTableRelation> dtrsx = new ArrayList<DownTableRelation>();
        findRootDownTableRelations(rootDtrName, this, dtrsx);
        return dtrsx;
    }

    private static void findRootDownTableRelations(String rootDtrName, DownTableRelation root, List<DownTableRelation> dtrsx) {
        for (DownTableRelation dtr : root.dtrs) {
            if (dtr.ts.getName().equals(rootDtrName)) {
                dtrsx.add(dtr);
            }
        }
        for (DownTableRelation dtr : root.dtrs) {
            findRootDownTableRelations(rootDtrName, dtr, dtrsx);
        }
    }

    public boolean containsDownTableRelation(String dtrName) {
        return containsDownTableRelation(dtrName, this);
    }

    private static boolean containsDownTableRelation(String dtrName, DownTableRelation root) {
        boolean flag = false;
        for (DownTableRelation dtr : root.dtrs) {
            if (dtr.ts.getName().equals(dtrName)) {
                flag = true;
            }
            flag = containsDownTableRelation(dtrName, dtr);
            if (flag) {
                return flag;
            }
        }
        return flag;
    }

    public static List<DownTableRelation> topDownTableRelations(List<DownTableRelation> dtrsx) {
        List<DownTableRelation> dtrs = new CopyOnWriteArrayList<DownTableRelation>(dtrsx);
        for (DownTableRelation dtr : dtrsx) {
            for (DownTableRelation dtrx : dtrs) {
                if (!dtr.ts.getName().equals(dtrx.ts.getName())) {
                    if (containsDownTableRelation(dtrx.ts.getName(), dtr)) {
                        dtrs.remove(dtrx);
                    }
                }
            }
        }
        return dtrs;
    }
}

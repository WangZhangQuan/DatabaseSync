package com.wzq.connnector.tagert.index;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Hash索引，用于提高查询效率。
 */
public class HashIndex implements Index {

    /**
     * 索引结构 {k:[{k:[]}]}
     */
    private Map<Object, Object> indexs = new HashMapIndex<Object, Object>();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private List<ArrayListIndex<Object>> getIndex(Object[] paths) {
        ArrayList<ArrayListIndex<Object>> ls = new ArrayList<ArrayListIndex<Object>>();
        getIndex(paths, 0, paths.length - 1, indexs, ls);
        return ls;
    }

    /**
     * 查找索引集合
     *
     * @param paths
     * @param s
     * @param e
     * @param map
     * @param ls
     */
    private static void getIndex(Object[] paths, int s, int e, Map<Object, Object> map, List<ArrayListIndex<Object>> ls) {
        // 拒绝非法参数
        if (s >= paths.length || s > e) {
            return;
        }

        Object o = map.get(paths[s]);
        // 已经找到索引目的地了
        if (s == e) {
            if (unSoft(o) instanceof ArrayListIndex) {
                ls.add((ArrayListIndex<Object>) unSoft(o));
            }
            return;
        }
        if (unSoft(o) instanceof ArrayListIndex) {
            List<Object> os = (List<Object>) unSoft(o);
            for (Object ox : os) {
                if (unSoft(ox) instanceof HashMapIndex) {
                    getIndex(paths, ++s, e, (Map<Object, Object>) unSoft(ox), ls);
                }
            }
        }
    }

    /**
     * 通过路径和值删除索引数据
     *
     * @param paths
     * @param v
     */
    public void deleteIndexAndByValue(Object[] paths, Object v) {
        readWriteLock.writeLock().lock();
        try {
            List<ArrayListIndex<Object>> s = getIndex(paths);
            ArrayList<Object> os = new ArrayList<Object>();
            for (ArrayListIndex<Object> alio : s) {
                deleteArrayList(alio, v);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * 删除索引
     *
     * @param paths
     */
    public void deleteIndex(Object[] paths) {
        readWriteLock.writeLock().lock();
        try {
            deleteIndex(paths, 0, paths.length - 1, indexs);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private static int deleteIndex(Object[] paths, int s, int e, Map<Object, Object> map) {
        int count = 0;
        // 拒绝非法参数
        if (s >= paths.length || s > e) {
            return count;
        }
        Object o = map.get(paths[s]);
        // 已经找到索引目的地了
        if (s == e) {
            map.remove(paths[s]);
            count++;
            return count;
        }
        if (unSoft(o) instanceof ArrayListIndex) {
            deleteArrayList((List<Object>)unSoft(o), null);
        }
        return count;
    }

    private static int deleteArrayList(List<Object> al, Object filter) {
        int count = 0;
        List<Object> dels = new ArrayList<Object>();
        for (Object o : al) {
            if (!(unSoft(o) instanceof HashMapIndex)) {
                if (filter != null && filter.equals(unSoft(o))) {
                    dels.add(o);
                } else {
                    dels.add(o);
                }
            } else {
                Map<Object, Object> m = (HashMap<Object, Object>) unSoft(o);
                Collection<Object> values = m.values();
                for (Object value : values) {
                    if (unSoft(value) instanceof ArrayListIndex) {
                        count += deleteArrayList((ArrayList) unSoft(value), filter);
                    } else {
                        if (filter != null && filter.equals(unSoft(o))) {
                            dels.add(o);
                        } else {
                            dels.add(o);
                        }
                    }
                }
            }
        }
        count += dels.size();
        al.removeAll(dels);
        return count;
    }

    public void addIndex(Object[] paths, Object o) {
        readWriteLock.writeLock().lock();
        try {
            // 使用软引用包装
            addIndex(paths, 0, paths.length - 1, indexs, o);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private static void addIndex(Object[] paths, int s, int e, Map<Object, Object> map, Object o) {
        // 拒绝非法参数
        if (s >= paths.length) {
            return;
        }
        if (o instanceof HashMapIndex) {
            throw new RuntimeException("HashMapIndex can not be added to the collection");
        }
        Object ox = map.get(paths[s]);
        // 已经找到索引目的地了
        if (s == e) {
            if (unSoft(ox) instanceof ArrayListIndex) {
                ((ArrayList) unSoft(ox)).add(soft(o));
            } else {
                ArrayList al = new ArrayListIndex();
                al.add(soft(o));
                map.put(paths[s], soft(al));
            }
            return;
        }
        if (unSoft(ox) instanceof ArrayListIndex) {
            ArrayList al = (ArrayList) unSoft(ox);
            boolean flag = false;
            for (Object oxx : al) {
                if (unSoft(oxx) instanceof HashMapIndex) {
                    if (((HashMap) unSoft(oxx)).containsKey(paths[s])) {
                        addIndex(paths, ++s, e, ((HashMap) unSoft(oxx)), o);
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                boolean f1 = false;
                if (al.size() > 0) {
                    for (Object o1 : al) {
                        if (unSoft(o1) instanceof HashMapIndex) {
                            addIndex(paths, ++s, e, ((HashMap) unSoft(o1)), o);
                            f1 = true;
                            break;
                        }
                    }
                }
                if (!f1) {
                    HashMap<Object, Object> mapx = new HashMapIndex<Object, Object>();
                    al.add(soft(mapx));
                    map.put(paths[s], soft(al));
                    addIndex(paths, ++s, e, mapx, o);
                }
            }
        } else {
            ArrayList al = new ArrayListIndex();
            HashMap<Object, Object> mapx = new HashMapIndex<Object, Object>();
            al.add(soft(mapx));
            map.put(paths[s], soft(al));
            addIndex(paths, ++s, e, mapx, o);
        }

    }

    public void moveIndex(Object[] pathsf, Object[] pathst) {
        readWriteLock.writeLock().lock();
        try {
            Set<Object> vs = findIndexValues(pathsf);
            if (vs.size() > 0) {
                deleteIndex(pathsf);
                addIndex(pathst, vs);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public Set<Object> findIndexValues(Object[] paths) {
        HashSet<Object> vs = new HashSet<Object>();
        readWriteLock.readLock().lock();
        try {
            findIndexValues(paths, 0, paths.length - 1, indexs, vs);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return vs;
    }

    private static void findIndexValues(Object[] paths, int s, int e, Map<Object, Object> map, HashSet<Object> vs) {
        // 拒绝不合法的参数
        if (s > e || s >= paths.length) {
            return;
        }
        Object o = map.get(paths[s]);
        // 已经找到索引目的地了
        if (s == e) {
            if (unSoft(o) instanceof ArrayListIndex && ((ArrayListIndex) unSoft(o)).size() > 0) {
                ArrayList<Object> alx = (ArrayListIndex) unSoft(o);
//                for (Object a : alx) {
//                    if (!(unSoft(a) instanceof HashMapIndex)) {
//                        vs.add(unSoft(a));
//                    }
//                }
                ergodicArrayList(alx, vs);
            }
        }
        if (unSoft(o) instanceof ArrayListIndex) {
            List<Object> os = (List<Object>) unSoft(o);
            for (Object ox : os) {
                if (unSoft(ox) instanceof HashMapIndex) {
                    findIndexValues(paths, ++s, e, (Map<Object, Object>) unSoft(ox), vs);
                }
            }
        }
    }

    private static void ergodicArrayList(List<Object> al, Set<Object> vs) {
        for (Object o : al) {
            if (!(unSoft(o) instanceof HashMapIndex)) {
                vs.add(unSoft(o));
            } else {
                Map<Object, Object> m = (HashMap<Object, Object>) unSoft(o);
                Collection<Object> values = m.values();
                for (Object value : values) {
                    if (unSoft(value) instanceof ArrayListIndex) {
                        ergodicArrayList((ArrayList) unSoft(value), vs);
                    } else {
                        vs.add(unSoft(value));
                    }
                }
            }
        }
    }

    /**
     * 软应用包装
     *
     * @param o
     * @return
     */
    private static Object soft(Object o) {
        // TODO 防止索引不完整 暂时停止使用软应用
//        return new SoftReference(o);
        return o;
    }

    /**
     * 去掉软应用包装
     *
     * @param o
     * @return
     */
    private static Object unSoft(Object o) {
        // TODO 防止索引不完整 暂时停止使用软应用
//        if (o instanceof SoftReference) {
//            return ((SoftReference) o).get();
//        }
        return o;
    }
}

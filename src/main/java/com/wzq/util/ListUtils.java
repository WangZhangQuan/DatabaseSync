package com.wzq.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils {

    /**
     * 把list中多余的删除
     * @param list
     * @param ks
     * @param <K>
     * @param <V>
     */
    public static <K, V> void listInF(List<KeyValue<K, V>> list, Collection<K> ks) {
        List<KeyValue<K, V>> dels = new ArrayList<KeyValue<K, V>>(list.size() - ks.size());
        for (KeyValue<K, V> kvKeyValue : list) {
            boolean f = false;
            for (K k : ks) {
                if (k.equals(kvKeyValue.getKey())) {
                    f = true;
                }
            }
            if (!f) {
                dels.add(kvKeyValue);
            }
        }
        list.removeAll(dels);
    }

    public static <K, V> List<KeyValue<K, V>> listIn(List<KeyValue<K, V>> list, Collection<K> ks) {
        List<KeyValue<K, V>> keyValues = new ArrayList<KeyValue<K, V>>();
        for (KeyValue<K, V> kvKeyValue : list) {
            for (K k : ks) {
                if (k.equals(kvKeyValue.getKey())) {
                    keyValues.add(new KeyValue<K, V>(k, kvKeyValue.getValue()));
                    break;
                }
            }
        }
        return keyValues;
    }

    public static <K, V> KeyValue<List<K>, List<V>> listInT(List<KeyValue<K, V>> list, Collection<K> ks) {
        KeyValue<List<K>, List<V>> llkv = new KeyValue<List<K>, List<V>>(new ArrayList<K>(), new ArrayList<V>());
        for (KeyValue<K, V> kvKeyValue : list) {
            for (K k : ks) {
                if (k.equals(kvKeyValue.getKey())) {
                    llkv.getKey().add(k);
                    llkv.getValue().add(kvKeyValue.getValue());
                    break;
                }
            }
        }
        return llkv;
    }

    public static <K, V> List<KeyValue<K, V>> listCast(KeyValue<List<K>, List<V>> kvs) {
        List<KeyValue<K, V>> lkv = new ArrayList<KeyValue<K, V>>();

        List<K> keys = kvs.getKey();
        int kz = keys.size();
        List<V> values = kvs.getValue();
        int vz = values.size();
        int len = kz > vz ? vz : kz;

        for (int i = 0; i < len; i++) {
            lkv.add(new KeyValue<K, V>(keys.get(i), values.get(i)));
        }

        return lkv;
    }

    public static <K, V> List<KeyValue<K, V>> listFrom(Collection<K> ks, V v) {
        List<KeyValue<K, V>> result = new ArrayList<KeyValue<K, V>>();
        for (K k : ks) {
            result.add(new KeyValue<K, V>(k, v));
        }
        return result;
    }

    public static <K> List<KeyValue<K, Object>> listFromO(Collection<K> ks, Object v) {
        List<KeyValue<K, Object>> result = new ArrayList<KeyValue<K, Object>>();
        for (K k : ks) {
            result.add(new KeyValue<K, Object>(k, v));
        }
        return result;
    }

    public static <K, V> List<K> splitK(List<KeyValue<K, V>> list) {
        List<K> ks = new ArrayList<K>();
        for (KeyValue<K, V> kvKeyValue : list) {

        }
        return ks;
    }

    public static <V> List<V> newList(int count, V fill) {
        List<V> pvs = new ArrayList<V>(count);
        for (int i = 0; i < count; i++) {
            pvs.add(fill);
        }
        return pvs;
    }

    public static List<Object> newListO(int count, Object fill) {
        List<Object> pvs = new ArrayList<Object>(count);
        for (int i = 0; i < count; i++) {
            pvs.add(fill);
        }
        return pvs;
    }
}

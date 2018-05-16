package com.wzq.util;

import java.util.*;

public class MapUtils {
    public static <K, V> Map<K, V> mapIn(Map<K, V> map, Collection<K> keys) {
        Map<K, V> mapx = new HashMap<K, V>();
        Set<K> set = map.keySet();
        for (K k : set) {
            if (keys.contains(k)) {
                mapx.put(k, map.get(k));
            }
        }
        return mapx;
    }

    public static <K, V> KeyValue<List<K>, List<V>> mapSplit(Map<K, V> map) {
        List<K> ks = new ArrayList<K>();
        List<V> vs = new ArrayList<V>();
        Set<Map.Entry<K, V>> set = map.entrySet();
        for (Map.Entry<K, V> e : set) {
            ks.add(e.getKey());
            vs.add(e.getValue());
        }
        return new KeyValue<List<K>, List<V>>(ks, vs);
    }

    public static <K, V> Map<K, V> mapFrom(List<K> ks, V fillValue){
        Map<K, V> kvm = new HashMap<K, V>();
        for (K k : ks) {
            kvm.put(k, fillValue);
        }
        return kvm;
    }

    public static <K> Map<K, Object> mapFromO(List<K> ks, Object fillValue){
        Map<K, Object> kvm = new HashMap<K, Object>();
        for (K k : ks) {
            kvm.put(k, fillValue);
        }
        return kvm;
    }
}

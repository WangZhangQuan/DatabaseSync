package com.wzq.test;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentTest {

    @Test
    public void t1() {
        List<Integer> dels = new CopyOnWriteArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            dels.add(i);
        }
        for (Integer del : dels) {
            dels.remove(del);
        }
        System.out.println("xx");
    }
}

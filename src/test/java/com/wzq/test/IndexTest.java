package com.wzq.test;

import com.wzq.connnector.tagert.index.HashIndex;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IndexTest {

    private HashIndex hashIndex = null;

    private Random random = null;

    private List<Object[]> indexs = null;

    @Before
    public void b() {
        random = new Random();
        indexs = new ArrayList<Object[]>();
        hashIndex = new HashIndex();
    }

    private Object[] randomIndex(int pathCount) {
        Object[] objects = new Object[pathCount];
        for (int i = 1; i < pathCount; i++) {
            int i1 = random.nextInt(5);
            objects[i] = i1 % i == 0 ? i1 : String.valueOf(i1);
        }
        return objects;
    }

    @Test
    public void t1() {
        List<Set<Object>> sets = new ArrayList<Set<Object>>();

        int len = 100000;
        int sem = 1000;

        Object[][] obx = new Object[len][];

        for (int i = 0; i < obx.length; i++) {
            obx[i] = randomIndex(i % 5 + 1);
        }
        // 存入操作
        long sx = System.currentTimeMillis();
        for (int i = 0; i < len; i++) {
            hashIndex.addIndex(obx[i], i % 5 == 0 ? String.valueOf(i) : i);
            if (i % sem == 0) {
                indexs.add(obx[i]);
            }
        }
        long ex = System.currentTimeMillis();
        // 查询操作
        long s = System.currentTimeMillis();
        for (Object[] index : indexs) {
            sets.add(hashIndex.findIndexValues(index));
        }
        long e = System.currentTimeMillis();

        System.out.println("存入耗时：" + (ex - sx) + "，查询耗时：" + (e - s));
    }

    @Test
    public void t2() {
        hashIndex.addIndex(new Object[]{"索引一", "skmfdks", 555}, "你好");
        hashIndex.addIndex(new Object[]{"索引一", "skmfdks", 555}, "hahas");
        hashIndex.deleteIndexAndByValue(new Object[]{"索引一", "skmfdks", 555}, "hahas");
        hashIndex.deleteIndexAndByValue(new Object[]{"索引一", "skmfdks", 555}, "你好");
        System.out.println("xxxxx");
    }
}

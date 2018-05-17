package com.wzq.util;

import com.wzq.able.Nameable;

import java.util.ArrayList;
import java.util.List;

public class NameUtils {

    public static <T extends Nameable> List<T> difference(List<T> nas, List<T> nasx) {
        List<T> ts = new ArrayList<T>();
        for (T na : nas) {
            boolean flag = false;
            for (T nax : nasx) {
                if (na.getName().equals(nax.getName())) {
                    flag = true;
                }
            }
            if (!flag) {
                ts.add(na);
            }
        }
        return ts;
    }

}

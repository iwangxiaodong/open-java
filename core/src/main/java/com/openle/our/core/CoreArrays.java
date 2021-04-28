package com.openle.our.core;

import java.util.Arrays;

/**
 *
 * @author 168
 */
public class CoreArrays {

    //  按参数顺序合并
    public static <T> T[] marge(T[] target, T newEntry) {
        T[] r = Arrays.copyOf(target, target.length + 1);
        r[r.length - 1] = newEntry;
        return (T[]) r;
    }

    //  按参数顺序合并
    public static <T> T[] marge(T newEntry, T[] target) {
        T[] r = Arrays.copyOf(target, target.length + 1);
        r[0] = newEntry;
        System.arraycopy(target, 0, r, 1, target.length);
        return (T[]) r;
    }

    //  按参数顺序合并
    public static <T> T[] marge(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}

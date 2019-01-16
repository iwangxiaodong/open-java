package com.openle.our.core;

import java.util.Arrays;

/**
 *
 * @author 168
 */
public class CoreCommon {

    public static <T> T[] addSetter(T[] first, T entry) {
        T[] r = Arrays.copyOf(first, first.length + 1);
        r[r.length - 1] = entry;
        return (T[]) r;
    }

    public static <T> T[] addSetterToFirst(T[] first, T entry) {
        T[] r = Arrays.copyOf(first, first.length + 1);
        r[0] = entry;
        System.arraycopy(first, 0, r, 1, first.length);
        return (T[]) r;
    }

    public static <T> T[] margeArrays(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}

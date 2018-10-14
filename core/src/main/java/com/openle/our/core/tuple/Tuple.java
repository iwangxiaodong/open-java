package com.openle.our.core.tuple;

// Tuple2 - Map.entry("k","v").getKey();
import java.util.Map;

// or use Package org.apache.commons.lang3.tuple
public class Tuple {

    public static <T1, T2> Map.Entry<T1, T2> tuple(T1 p1, T2 p2) {
        return Map.<T1, T2>entry(p1, p2);
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3>
            tuple(T1 p1, T2 p2, T3 p3) {
        return new Tuple3<>(p1, p2, p3);
    }

    @Deprecated
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4>
            tuple(T1 p1, T2 p2, T3 p3, T4 p4) {
        return new Tuple4<>(p1, p2, p3, p4);
    }
}

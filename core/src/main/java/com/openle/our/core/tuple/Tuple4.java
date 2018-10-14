package com.openle.our.core.tuple;

public class Tuple4<T1, T2, T3, T4> extends Tuple3<T1, T2, T3> {
    public final T4 v4;

    public Tuple4(T1 p1,T2 p2, T3 p3, T4 p4) {
        super(p1, p2, p3);
        v4 = p4;
    }

    @Override
    public String toString() {
        return "(" + v1 + ", " + v2 + ", " +
                v3 + ", " + v4 + ")";
    }
}
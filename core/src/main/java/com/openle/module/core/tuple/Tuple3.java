package com.openle.module.core.tuple;

public class Tuple3<T1, T2, T3> {

    public final T1 v1;
    public final T2 v2;
    public final T3 v3;

    public Tuple3(T1 p1, T2 p2, T3 p3) {
        v1 = p1;
        v2 = p2;
        v3 = p3;
    }

    @Override
    public String toString() {
        return "(" + v1 + ", " + v2 + ", " + v3 + ")";
    }
}

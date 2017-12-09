package com.openle.module.core.lambda;

import java.io.Serializable;
import java.util.function.Function;

public interface SerializedFunction<T, R> extends Function<T, R>, Serializable {
}

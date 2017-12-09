package com.openle.module.core.lambda;

import java.io.Serializable;
import java.util.function.Supplier;

public interface SerializedSupplier<T> extends Supplier<T>, Serializable {
}

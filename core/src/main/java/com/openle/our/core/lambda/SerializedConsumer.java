package com.openle.our.core.lambda;

import java.io.Serializable;
import java.util.function.Consumer;

public interface SerializedConsumer<T> extends Consumer<T>, Serializable {
}

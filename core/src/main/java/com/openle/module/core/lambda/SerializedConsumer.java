package com.openle.module.core.lambda;

import java.io.Serializable;
import java.util.function.Consumer;

public interface SerializedConsumer<T> extends Consumer<T>, Serializable {
}

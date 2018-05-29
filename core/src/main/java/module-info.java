module com.openle.module.core {
    requires java.logging;
    requires jdk.compiler;

    exports com.openle.module.core;
    exports com.openle.module.core.converter;
    exports com.openle.module.core.io;
    exports com.openle.module.core.lambda;
    exports com.openle.module.core.network;
    exports com.openle.module.core.tuple;

    // for test
    // requires org.junit.jupiter.api;
}

module com.openle.our.core {
    requires java.logging;
    //requires jdk.compiler;  // 考虑新增一个com.openle.our.jdk模块

    exports com.openle.our.core;
    exports com.openle.our.core.collection;
    exports com.openle.our.core.converter;
    exports com.openle.our.core.io;
    exports com.openle.our.core.lambda;
    exports com.openle.our.core.network;
    //exports com.openle.our.core.patch.android;
    exports com.openle.our.core.security;
    exports com.openle.our.core.specification;
    exports com.openle.our.core.tuple;

    // for test
    // requires org.junit.jupiter.api;
}

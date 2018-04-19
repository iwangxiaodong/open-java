module com.openle.module.core {
    requires java.logging;
    requires jdk.compiler;
        
    exports com.openle.module.core;
    exports com.openle.module.core.io;
    exports com.openle.module.core.converter;
    exports com.openle.module.core.lambda;
    
    // for test
   // requires org.junit.jupiter.api;
}

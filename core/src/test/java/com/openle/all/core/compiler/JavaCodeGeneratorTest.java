package com.openle.all.core.compiler;

import com.openle.all.core.compiler.SimpleJavaCompiler;
import java.util.function.Supplier;

import org.junit.jupiter.api.*;

public class JavaCodeGeneratorTest {

    @Test
    public void testCompile() throws IllegalAccessException, InstantiationException {
        Class<? extends Supplier> compiled = SimpleJavaCompiler.compile(Supplier.class, fileCg -> {
            fileCg.generateImport(Supplier.class);

            fileCg.publicClass("Test").implement("Supplier<String>")
                    .build(classCg -> {
                        classCg.publicMethod("String", "get")
                                .build(cg -> cg.print("return ").printQuoted("Hello").println(";"));
                    });
        });
        Supplier supplier = compiled.newInstance();
        Assertions.assertEquals(supplier.get(), "Hello");
    }
}

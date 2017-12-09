package com.openle.module.core.compiler;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author xiaodong
 */
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
        assertThat(supplier.get(), is("Hello"));
    }
}

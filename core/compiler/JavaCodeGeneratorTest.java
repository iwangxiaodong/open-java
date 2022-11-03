package com.openle.all.core.compiler;

import com.openle.our.core.compiler.SimpleJavaCompiler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

public class JavaCodeGeneratorTest {

    @Test
    public void testCompile() {
        Class<? extends Supplier> compiled = SimpleJavaCompiler.compile(Supplier.class, fileCg -> {
            fileCg.generateImport(Supplier.class);

            fileCg.publicClass("Test").implement("Supplier<String>")
                    .build(classCg -> {
                        classCg.publicMethod("String", "get")
                                .build(cg -> cg.print("return ").printQuoted("Hello").println(";"));
                    });
        });
        Supplier supplier = null;
        try {
            supplier = compiled.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            Logger.getLogger(JavaCodeGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Assertions.assertEquals("Hello", supplier.get());
    }

    @Test
    public void testCompileToString() {
        Class<?> compiled = SimpleJavaCompiler.compile(Object.class, fileCg -> {
            //fileCg.generateImport(Supplier.class);

            fileCg.publicClass("Test")//.implement("Supplier<String>")
                    .build(classCg -> {
                        classCg.publicMethod("String", "toString")
                                .build(cg -> cg.print("return ").printQuoted("Hello").println(";"));
                    });
        });
        try {
            Object obj = compiled.getConstructor().newInstance();
            Assertions.assertEquals("Hello", obj.toString());
        } catch (ReflectiveOperationException ex) {
            Logger.getLogger(JavaCodeGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testCompileStatic() {
        Class<?> compiled = SimpleJavaCompiler.compile(Object.class,
                "import java.util.function.Supplier;\n"
                + "public class Hi {\n"
                + "  public static Object main(String[] args) {\n"
                + "return new Object();    //return \"Hello, World!\";\n"
                + "  }\n"
                + "}");

        //  args参数可选
        Optional<Method> om = Arrays.asList(compiled.getMethods()).stream().findFirst();
        if (om.isPresent()) {
            Method m = om.get();
            try {
                System.out.println(m.getParameterCount());
                if (m.getParameterCount() == 0) {
                    Object obj = m.invoke(null);
                    System.out.println("obj - " + String.valueOf(obj));
                } else if (m.getParameterCount() == 1) {
                    Object obj = m.invoke(null, (Object) null);
                    System.out.println("obj - " + String.valueOf(obj));
                }

            } catch (ReflectiveOperationException ex) {
                System.err.println(ex);
            }
        }

//        try {
//            //  args参数可选
//            //Object obj = compiled.getMethod("main").invoke(null);
//            //Assertions.assertEquals("Hello, World!", obj);
//        } catch (ReflectiveOperationException ex) {
//            Logger.getLogger(JavaCodeGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Test
    public void testCompileString() {
        Class<?> compiled = SimpleJavaCompiler.compile(Object.class,
                "import java.util.function.Supplier;\n"
                + "public class Hi {\n"
                + "  public String toString() {\n"
                + "    return \"Hello, World!\"+Supplier.class;\n"
                + "  }\n"
                + "}");
        try {
            Object obj = compiled.getConstructor().newInstance();
            Assertions.assertEquals("Hello, World!interface java.util.function.Supplier", obj.toString());
        } catch (ReflectiveOperationException ex) {
            Logger.getLogger(JavaCodeGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

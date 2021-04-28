package com.openle.our.core.lambda;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

// https://stackoverflow.com/questions/31178103/how-can-i-find-the-target-of-a-java8-method-reference#answers
public class Lambda {

    public static void main(String[] args) {
        String mt = extractConsumer((Lambda t) -> t.toString()).get().getInstantiatedMethodType();
        System.out.println(mt);
        
        System.out.println(MethodType.fromMethodDescriptorString(mt,
                Thread.currentThread().getContextClassLoader()).parameterType(0));
    }

    public static Optional<SerializedLambda> extractConsumer(Consumer<?> lambda) {
        return getSerializedLambda((Serializable) lambda);
    }

    public static <T, R> Optional<SerializedLambda> extractFunction(Function<T, R> lambda) {
        return getSerializedLambda((Serializable) lambda);
    }

    public static Optional<SerializedLambda> getSerializedLambda(Serializable lambda) {
        for (Class<?> cl = lambda.getClass(); cl != null; cl = cl.getSuperclass()) {
            try {
                Method m = cl.getDeclaredMethod("writeReplace");
                m.setAccessible(true);
                Object replacement = m.invoke(lambda);
                if (!(replacement instanceof SerializedLambda)) {
                    break; // custom interface implementation
                }
                SerializedLambda l = (SerializedLambda) replacement;
                return Optional.of(l);
            } catch (NoSuchMethodException e) {
                // do nothing
            } catch (IllegalAccessException | InvocationTargetException e) {
                break;
            }
        }

        return Optional.empty();
    }

}

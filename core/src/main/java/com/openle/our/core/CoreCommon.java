package com.openle.our.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 *
 * @author 168
 */
public class CoreCommon {

    //  设置final and static的Field常量新值 - '--add-opens', "java.base/java.lang.reflect=core", 或 '--add-opens', "java.base/java.lang=ALL-UNNAMED",
    //  setFinalStaticValue(Boolean.class.getField("FALSE"), true);System.out.format(" - %s", false); // " - true"
    public static void setFinalStaticValue(Field field, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);

        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        modifiersField.setAccessible(false);

        field.set(null, newValue);
        field.setAccessible(false);
    }

    public static <T> T[] addSetter(T[] first, T entry) {
        T[] r = Arrays.copyOf(first, first.length + 1);
        r[r.length - 1] = entry;
        return (T[]) r;
    }

    public static <T> T[] addSetterToFirst(T[] first, T entry) {
        T[] r = Arrays.copyOf(first, first.length + 1);
        r[0] = entry;
        System.arraycopy(first, 0, r, 1, first.length);
        return (T[]) r;
    }

    public static <T> T[] margeArrays(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}

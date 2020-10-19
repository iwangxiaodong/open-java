//package com.openle.our.core;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
//// MethodHandle比getMethod调用快很多
//public class ReflectCommon {
//
//    public static Object getEntityProperty(Object obj, String fieldName) {
//        Object result = null;
//        Class objClass = obj.getClass();
//        try {
//            Field field = objClass.getDeclaredField(fieldName);
//
//            // 修改访问控制权限
//            boolean accessFlag = field.canAccess(obj);
//            if (!accessFlag) {
//                field.setAccessible(true);
//            }
//
//            result = field.get(obj);
//            // 恢复访问控制权限
//            field.setAccessible(accessFlag);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            System.err.println("ReflectCommon.getEntityProperty:" + e);
//        }
//        return result;
//    }
//
//    public static void setEntityProperty(Object obj, String fieldName, Object value) {
//        Class objClass = obj.getClass();
//        try {
//            Field field = objClass.getDeclaredField(fieldName);
//
//            // 修改访问控制权限
//            boolean accessFlag = field.canAccess(obj);
//            if (!accessFlag) {
//                field.setAccessible(true);
//            }
//
//            field.set(obj, value);
//            // 恢复访问控制权限
//            field.setAccessible(accessFlag);
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            System.err.println("ReflectCommon.setEntityProperty:" + e);
//        }
//    }
//
//    public static void outputMethodParameter(Method method) {
//        // //////////////方法的参数
//        System.out.println(" paramTypeType: ");
//        Type[] paramTypeList = method.getGenericParameterTypes();// 方法的参数列表
//        for (Type paramType : paramTypeList) {
//            System.out.println("  " + paramType);// 参数类型
//            if (paramType instanceof ParameterizedType)/**//* 如果是泛型类型 */ {
//                Type[] types = ((ParameterizedType) paramType)
//                        .getActualTypeArguments();// 泛型类型列表
//                System.out.println("  TypeArgument: ");
//                for (Type type : types) {
//                    System.out.println("   " + type);
//                }
//            }
//        }
//        // //////////////方法的返回值
//        System.out.println(" returnType: ");
//        Type returnType = method.getGenericReturnType();// 返回类型
//        System.out.println("  " + returnType);
//        if (returnType instanceof ParameterizedType)/**//* 如果是泛型类型 */ {
//            Type[] types = ((ParameterizedType) returnType)
//                    .getActualTypeArguments();// 泛型类型列表
//            System.out.println("  TypeArgument: ");
//            for (Type type : types) {
//                System.out.println("   " + type);
//            }
//        }
//    }
//
//    // toUI为真则界面上显示空字符串
//    public static void ProcessBean(Object e, boolean toUI) throws Exception {
//        Class<?> cls = e.getClass();
//
//        Field[] fields = cls.getDeclaredFields();
//        for (Field field : fields) {
//            // 修改访问控制权限
//            boolean accessFlag = field.canAccess(e);
//            if (!accessFlag) {
//                field.setAccessible(true);
//            }
//            setDefaultValue(e, field, toUI);
//            // 恢复访问控制权限
//            field.setAccessible(accessFlag);
//        }
//    }
//
//    private static void setDefaultValue(Object e, Field field, boolean toUI) {
//        try {
//            Object param = field.get(e);
//            if (field.getType() == String.class) {
//                if (toUI) {
//                    if (param == null) {
//                        field.set(e, "");
//                    }
//                } else {
//                    if (param != null && param.toString().trim().equals("")) {
//                        field.set(e, null);
//                    }
//                }
//            }
//        } catch (IllegalAccessException | IllegalArgumentException ex) {
//            System.out.println(ex);
//        }
//    }
//
//    public static String beanToString(Object o) throws Exception {
//        String printString = "";
//        Class<?> cls = o.getClass();
//        Field[] fields = cls.getDeclaredFields();
//        for (Field field : fields) {
//            Object param = getEntityProperty(o, field.getName());
//            printString = "属性名:" + field.getName() + " 属性值:" + param;
//        }
//
//        return printString;
//    }
//}

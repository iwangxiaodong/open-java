package com.openle.our.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 * @author xiaodong
 */
public class CoreData {

    /**
     * Or use implementation 'org.owasp.encoder:encoder:1.2.2'
     * org.owasp.esapi.Encoder#encodeForSQL(org.owasp.esapi.codecs.Codec,String)
     *
     * @param str sql
     *
     * @return escaped sql
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''") //  StringEscapeUtils.escapeSql(...)实现仅仅替换单引号至双引号而已。
                .replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n");
    }

    //  注意 - 需要引入persistence api
    public static String getTableName(Class c) {

        String tableName = c.getSimpleName();

        for (Annotation a : c.getAnnotations()) {
            //System.out.println(a.annotationType().getName());
            if (a.annotationType().getName().equals("javax.persistence.Table")) {
                try {
                    Method m = a.annotationType().getMethod("name", new Class[]{});
                    Object obj = m.invoke(a, new Object[]{});
                    System.out.println("getTableName Entity " + c.getName() + "|TableName - " + obj);
                    //System.out.println("Table Name = " + obj);
                    tableName = obj.toString();
                } catch (java.lang.ReflectiveOperationException ex) {
                    System.err.println(ex);
                }
            }
        }

        return tableName;
    }
}

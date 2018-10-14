package com.openle.our.core;

/**
 *
 * @author xiaodong
 */
public class DataCommon {

    /**
     *
     * @param str sql
     *
     * @return escaped sql
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''").replace("\\", "\\\\")
                .replace("\r", "\\r").replace("\n", "\\n");
    }
}

package cn.imkarl.urlbuilder;

import java.util.Collection;

/**
 * 工具类
 * @version imkarl 2017-04
 */
class Util {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(Collection arr) {
        return arr == null || arr.isEmpty();
    }

}

package cn.imkarl.urlbuilder;

/**
 * 工具类
 * @version imkarl 2017-04
 */
class Util {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}

package com.kunzhuo.xuechelang.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * @author zhangQ
 * @date 2015-12-17
 * @description 普通字符工具类
 */
public class StringUtils {

    public static int getInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static DecimalFormat df = new DecimalFormat();

    public static String getDouble(double number, String format) {
        df.applyPattern(format);
        return df.format(number);
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static String formatDate(int time) {
        return sdf.format(new Timestamp(time * 1000L));
    }


}

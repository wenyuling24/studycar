package com.kunzhuo.xuechelang.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by waaa on 2016/9/8.
 */
public class DefaultUtils {

    public static String QUSMODEL = "qusmodel"; // 答题模式
    public static String QUSMODELFLAG = "qusmodelflag"; // 答题模式flag
    public static String ISFIRSTMOCK = "isfirstmock";
    public static String ISFIRSTMOCKFLAG = "isfirstmockflag";
    public static String USER = "user";
    public static String USER_JSONMSG = "user_msg";
    public static String USER_ID = "user_id";
    public static String OPEN_ID = "openid";

    /**
     * putShared
     *
     * @param context
     * @param str
     * @param typeName
     * @param editName
     */
    public static void putShared(Context context, String str, String typeName, String editName) {
        SharedPreferences shared = context.getSharedPreferences(typeName, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(editName, str);
        editor.commit();
    }


    /**
     * 得到shared
     *
     * @param context
     * @param typeName
     * @param editName
     * @return
     */
    public static String getShared(Context context, String typeName, String editName) {

        SharedPreferences shared = context.getSharedPreferences(typeName, 0);

        String str = shared.getString(editName, "");

        return str;
    }

    /**
     * putShared
     *
     * @param context
     * @param flag
     * @param typeName
     * @param editName
     */
    public static void putSharedBoolean(Context context, boolean flag, String typeName, String editName) {
        SharedPreferences shared = context.getSharedPreferences(typeName, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(editName, flag);
        editor.commit();
    }

    /**
     * 得到shared
     *
     * @param context
     * @param typeName
     * @param editName
     * @return
     */
    public static boolean getSharedBoolean(Context context, String typeName, String editName) {

        SharedPreferences shared = context.getSharedPreferences(typeName, 0);

        boolean flag = shared.getBoolean(editName, true);

        return flag;
    }


    /**
     * 取消shared
     *
     * @return
     */
    public static void getSharedClear(Context context, String typeName) {

        SharedPreferences shared = context.getSharedPreferences(typeName, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear().commit();

    }
}

package com.kunzhuo.xuechelang.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;

public class NormalUtils {

    public static SimpleDateFormat sdf;
    public final static String FIRSTUPDATE = "首次更新";

    /**
     * 返回格式为：yyyy-MM-dd HH:mm的当前时间
     *
     * @return
     */
    public static String getCurTime() {
        long milliseconds = System.currentTimeMillis();
        Date date = new Date(milliseconds);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String curTime = sdf.format(date);
        return curTime;
    }

    /**
     * 对当前时间yyyy-MM-dd HH:mm进行截取，保留MM-dd HH:mm
     *
     * @param curTime
     * @return
     */
    public static String subTime(String curTime) {
        String updateTime = "";
        // 半闭半开区间
        updateTime = curTime.substring(5, curTime.length());
        return updateTime;
    }

    /**
     * 对当前时间yyyy-MM-dd HH:mm进行截取，获取年份信息
     *
     * @param curTime
     * @return
     */
    public static int subYearFromTime(String curTime) {
        int updateYear;
        // 半闭半开区间
        updateYear = Integer.valueOf(curTime.substring(0, 4));
        return updateYear;
    }

    /**
     * 得到今年年份
     *
     * @return
     */
    public static int getCurYear() {
        long milliseconds = System.currentTimeMillis();
        Date date = new Date(milliseconds);
        sdf = new SimpleDateFormat("yyyy");
        int curYear = Integer.valueOf(sdf.format(date));
        return curYear;
    }

    /**
     * 从文件中得到上次的更新时间，和当前时间进行对比，设置合适的上一次更新时间
     *
     * @param lastUpdateTimeFromFile
     * @return
     */
    public static String getLastUpdateTime(String lastUpdateTimeFromFile) {

        if (!lastUpdateTimeFromFile.equals(FIRSTUPDATE)) {
            // 判断文件里面的更新时间和当前时间是不是一年
            if (getCurYear() > subYearFromTime(lastUpdateTimeFromFile)) {
                return lastUpdateTimeFromFile;
            } else {
                return subTime(lastUpdateTimeFromFile);
            }
        }
        return lastUpdateTimeFromFile;
    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public static int setRandom(int num) {

        Random random = new Random();

        int a = random.nextInt(num);

        if (a == 0) {
            return a + 1;
        } else
            return a;

    }
}

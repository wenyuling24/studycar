package com.kunzhuo.xuechelang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 时间工具类
 *
 * @author Administrator
 */
public class DateUtils {

    public static String FORMATTIMESTR = "yyyy/MM/dd HH:mm:ss"; // 时间格式化格式

    public static String FORMATTIMESTR2 = "yyyy-MM-dd HH:mm:ss"; // 时间格式化格式

    public static String FORMATTIMESTR3 = "yyyy年MM月dd日 HH:mm:ss"; // 时间格式化格式

    public static String FORMATTIMESTR4 = "yyyyMMddHHmmss"; // 时间格式化格式


    public static String FORMATDAY = "yyyy-MM-dd"; // 时间格式化格式
    public static String FORMATMONTH = "yyyy-MM"; // 当前月份

    /**
     * 获取yyyyMMdd格式日期
     *
     * @param time
     * @return
     */
    public static Date getDate(String time) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 返回当前时间戳
     *
     * @return
     */
    public static String getNowTime() {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        return ts;
    }

    public static String formatDate(Context context, long date) {
        @SuppressWarnings("deprecation")
        int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | android.text.format.DateUtils.FORMAT_ABBREV_ALL
                | android.text.format.DateUtils.FORMAT_CAP_AMPM
                | android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_SHOW_TIME;
        return android.text.format.DateUtils.formatDateTime(context, date,
                format_flags);
    }

    /**
     * 返回此时时间
     *
     * @return String: XXX年XX月XX日 XX:XX:XX
     */
    public static String getNowtime() {
        return new SimpleDateFormat(FORMATTIMESTR2).format(new Date());
    }


    /**
     * 返回此时时间
     *
     * @return String: XXXXXXXXXXXXXXXX
     */
    public static String getNowtimeX() {
        return new SimpleDateFormat(FORMATTIMESTR4).format(new Date());
    }


    /**
     * 返回此时日期
     *
     * @return
     */
    public static String getDateString() {
        return new SimpleDateFormat(FORMATDAY).format(new Date());
    }

    /**
     * 格式化输出指定时间点与现在的差
     *
     * @param paramTime 指定的时间点
     * @return 格式化后的时间差，类似 X秒前、X小时前、X年前
     */
    public static String getBetweentime(String paramTime) {
        String returnStr = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATTIMESTR);
        try {
            Date nowData = new Date();
            Date mDate = dateFormat.parse(paramTime);
            long betweenForSec = Math.abs(mDate.getTime() - nowData.getTime()) / 1000; // 秒

            if (mDate.getYear() == nowData.getYear()) { // 同一年

                if (mDate.getMonth() == nowData.getMonth()) { // 同一月

                    if (mDate.getDate() == nowData.getDate()) { // 同一天

                        if (mDate.getHours() == nowData.getHours()) { // 同一个小时

                            if (betweenForSec < 60) {
                                returnStr = betweenForSec + "秒前";
                            } else if (betweenForSec < (60 * 60)) {
                                returnStr = betweenForSec / 60 + "分钟前";
                            }

                        } else {
                            returnStr = paramTime.substring(10, 16);
                        }
                    } else if (mDate.getDate() + 1 == nowData.getDate()) { // 昨天

                        returnStr = "昨天 " + paramTime.substring(10, 16);
                    } else if (mDate.getDate() + 2 == nowData.getDate()) { // 前天

                        returnStr = "前天 " + paramTime.substring(10, 16);
                    } else {
                        returnStr = paramTime.substring(5, 16);
                    }

                } else {
                    returnStr = paramTime.substring(5, 16);
                }

            } else {
                returnStr = paramTime.substring(0, 10);
            }

        } catch (ParseException e) {
            returnStr = "TimeError"; // 错误提示
        }
        return returnStr;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATTIMESTR);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String getTime2(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATTIMESTR2);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }



    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String getTimeStr(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATTIMESTR3);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }


    // 返回某月最后一天
    public static String getLastDayOfMonth(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    // 字符串类型日期转化成date类型
    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);

        return formatter.format(date);
    }


    /**
     * 将时间戳转换成字符串
     *
     * @param time
     * @return
     */
    public static String getDateToString(String time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(FORMATTIMESTR2);
        return sf.format(d);
    }


}

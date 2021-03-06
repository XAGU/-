package com.xiaolian.amigo.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间相关工具类
 *
 * @author zcd
 * @date 17/9/27
 */

public class TimeUtils {
    private static final String TAG = TimeUtils.class.getSimpleName();

    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final DateFormat MY_DATE_FORMAT = new SimpleDateFormat("MM-dd", Locale.getDefault());
    public static final DateFormat MY_DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final DateFormat MY_DATE_FORMAT3 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    public static final DateFormat MY_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final DateFormat MY_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static final DateFormat MY_DATE_MONTH = new SimpleDateFormat("MM/dd", Locale.getDefault());
    public static final DateFormat MY_DATA_MINUTE = new SimpleDateFormat("mm" ,Locale.getDefault());
    public static final DateFormat MY_DATE_YEARMON_FORMAT = new SimpleDateFormat("yyyyMM", Locale.getDefault());
    public static final int SIXTY = 60;

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, DEFAULT_FORMAT);
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为format</p>
     *
     * @param millis 毫秒时间戳
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String millis2String(final long millis, final DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param millis 毫秒时间戳
     * @return Date类型时间
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    /**
     * 获取当前Date
     *
     * @return Date类型时间
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    public static String orderTimestampFormat(long timeStamp) {
        String result = "";
        long curTime = System.currentTimeMillis() / (long) 1000;
        long toDayZero = getStartTimeOfDay(System.currentTimeMillis(), "") / 1000;
//        long time = curTime - timeStamp / 1000;
        long time = timeStamp / 1000 - toDayZero;
        if (time >= 0) {
            result += "今天";
        } else if (time < 0 && Math.abs(time) < 3600 * 24) {
            result += "昨天";
        } else if (time >= 3600 * 24 * 2 && time < 3600 * 24 * 365) {
            result += millis2String(timeStamp, MY_DATE_MONTH) + " ";
        } else {
            result += millis2String(timeStamp, MY_DATE_FORMAT3) + " ";
        }
        return result + millis2String(timeStamp, MY_TIME_FORMAT);
    }


    public static String orderTimestampFormatSocial(long timeStamp) {
        String result = "";
        long curTime = System.currentTimeMillis() / (long) 1000;
        long toDayZero = getStartTimeOfDay(System.currentTimeMillis(), "") / 1000;
//        long time = curTime - timeStamp / 1000;
        long time = timeStamp / 1000 - toDayZero;
        if (time >= 0) {
            result += "今天";
        } else if (time < 0 && Math.abs(time) < 3600 * 24) {
            result += "昨天";
        } else {
            result += millis2String(timeStamp, MY_DATE_MONTH) + " ";
        }
        return result + millis2String(timeStamp, MY_TIME_FORMAT);
    }


    //获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
    public static long getStartTimeOfDay(long now, String timeZone) {
        String tz = TextUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }



    /**
     * 计算剩余时间
     * @param expiredTime  preString 时间前缀
     * @return
     */
    public static String orderBathroomLastTime(long expiredTime, String preString) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = expiredTime / 1000 - curTime;
        if (time > 0) {

            /**  分少于2位数加0  */
            if (time / SIXTY < 10) {
                preString += "0" + time / SIXTY;
            } else {
                preString += time / SIXTY;
            }

            /**   秒少于2位数加0   */
            if (time % SIXTY < 10) {
                preString += ":0" + time % SIXTY;
            } else {
                preString += ":" + time % SIXTY;
            }

        } else {
            preString = "0:00";
        }
        return preString;
    }


    /**
     * 计算剩余时间
     * @param expiredTime  preString 时间前缀
     * @return
     */
    public static String orderBathroomLastTime(long expiredTime, String preString , long diffTime) {
        long time = (expiredTime  - System.currentTimeMillis() - diffTime) / 1000;
        if (time > 0) {

            /**  分少于2位数加0  */
            if (time / SIXTY < 10) {
                preString += "0" + time / SIXTY;
            } else {
                preString += time / SIXTY;
            }

            /**   秒少于2位数加0   */
            if (time % SIXTY < 10) {
                preString += ":0" + time % SIXTY;
            } else {
                preString += ":" + time % SIXTY;
            }

        } else {
            preString = "0:00";
        }
        return preString;
    }

    /**
     * 过期时间与自己时间相比，倒计时的时间
     * @param expiredTime
     * @return
     */
    public static final int intervalTime(long expiredTime ) {
        return (((expiredTime - System.currentTimeMillis()) / 1000)) > 0 ? (int) (((expiredTime - System.currentTimeMillis()) / 1000)) : 0;
    }


    /**
     * 过期时间与自己时间相比，倒计时的时间
     * @param expiredTime
     * @return
     */
    public static final int intervalTime(long expiredTime , long diffTime ) {
        return (((expiredTime - System.currentTimeMillis() - diffTime) / 1000)) > 0 ? (int) (((expiredTime - System.currentTimeMillis() - diffTime) / 1000)) : 0;
    }

    public static String lostAndFoundTimestampFormat(long timeStamp) {
        String result = "";
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp / 1000;
        if (time > 0 && time < 60) {
            return "刚刚发布";
        }
        if (time >= 0 && time < 3600 * 24) {
            result += "今天";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 2) {
            result += "昨天";
        } else if (time >= 3600 * 24 * 2 && time < 3600 * 24 * 365) {
            result += millis2String(timeStamp, MY_DATE_FORMAT) + " ";
        } else {
            result += millis2String(timeStamp, MY_DATE_FORMAT2) + " ";
        }
        return result + millis2String(timeStamp, MY_TIME_FORMAT);
    }

    public static String noticeTimestampFormat(long timeStamp) {
        String result = "";
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp / 1000;
        if (time > 0 && time < 60) {
            return "刚刚发布";
        }
        if (time >= 0 && time < 3600 * 24) {
            result += "今天 ";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 2) {
            result += "昨天 ";
        } else if (time >= 3600 * 24 * 2 && time < 3600 * 24 * 365) {
            result += millis2String(timeStamp, MY_DATE_FORMAT) + " ";
        } else {
            result += millis2String(timeStamp, MY_DATE_FORMAT2) + " ";
        }
        return result + millis2String(timeStamp, MY_TIME_FORMAT);
    }

    public static String convertTimestampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        if (time >= 0 && time < 3600 * 24) {
            return "今天";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 2) {
            return "昨天";
        }
        return millis2String(timeStamp, MY_DATE_FORMAT);
    }

    public static String convertTimestampToAccurateFormat(long timeStamp) {
        String result = "";
        long curTime = System.currentTimeMillis();
        long time = curTime - timeStamp;
        if (time >= 0 && time < 3600 * 24 * 1000) {
            result += "今天 ";
        } else if (time >= 3600 * 24 * 1000 && time < 3600 * 24 * 2 * 1000) {
            result += "昨天 ";
        } else {
            result += millis2String(timeStamp, MY_DATE_FORMAT) + " ";
        }
        return result + millis2String(timeStamp, MY_TIME_FORMAT);
    }


    /**
     * 根据当前时间获取每5分钟时间戳
     * 比如  09：01 -> 09：00
     *      09：08 -> 09:05
     *      09:58 -> 09:55
     * @return
     */
    public static long getCountTimeStamp(){
        //获取当前时间戳所属的最近的五分钟的时间片整的时间戳
        long currentTime1 = System.currentTimeMillis()/1000;//获取距离1970 1 1的标准时间戳秒数
        int fiveMinTimestamps = 5 * 60; //5分钟的秒数
        long currentFiveFragment = currentTime1 - (currentTime1 % fiveMinTimestamps);
        return currentFiveFragment;

    }

    private static long covertStringTolong(String timeString  , DateFormat dateFormat){
        try {
            return (dateFormat.parse(timeString)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0 ;
    }


    /**
     * long 转化成String
     * @param time
     * @return
     */
    public static String covertTimeToString(long time) {

        Date date = new Date(time);
        return MY_TIME_FORMAT.format(date);
    }

    /**
     * 将Date类型转为时间戳
     *
     * @param date Date类型时间
     * @return 毫秒时间戳
     */
    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param date Date类型时间
     * @return 时间字符串
     */
    public static String date2String(final Date date) {
        return date2String(date, DEFAULT_FORMAT);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为format</p>
     *
     * @param date   Date类型时间
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String date2String(final Date date, final DateFormat format) {
        return format.format(date);
    }

    /**
     * 计算两个时间的差值
     * @param currentTime
     * @param beforeTime
     * @return
     */
    public static long  diffTime(long currentTime , long beforeTime){
        if (beforeTime <= 0) return 0 ;
        if (beforeTime > currentTime) return 0 ;
        long diffTime =  currentTime - beforeTime ;
        return diffTime ;
    }

}

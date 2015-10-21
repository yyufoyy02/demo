package com.vk.simpleutil.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类名：TimeUtil.java 类描述：时间处理工具
 */
public class XSimpleTime {
    private XSimpleTime() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_DATE_TIME = "yyyy年MM月dd日 HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 HH:mm";
    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long times = timestamp * 1000;
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - times) / 1000;// 与现在时间相差秒数
        Calendar nowCalendar = Calendar.getInstance();
        Calendar msgCalendar = Calendar.getInstance();
        msgCalendar.setTimeInMillis(times);
        StringBuffer timeStr = new StringBuffer();
        timeStr.setLength(0);
        timeStr.append(getFormatTimeFromTimestamp(timestamp, null));
        if (timeGap > 0) {
            if (timeGap > YEAR) {
//                timeStr.setLength(0);
//                timeStr.append(timeGap / YEAR + "年前");
            } else if (timeGap > MONTH) {
//                timeStr.setLength(0);
//                timeStr.append(timeGap / MONTH + "个月前");
            } else if (timeGap > DAY) {// 1天以上
                long timeDay = timeGap / DAY;
                if (isYesterDay(nowCalendar, msgCalendar)) {
                    timeStr.setLength(0);
                    timeStr.append("昨天 " + getFormatTimeFromTimestamp(timestamp, "HH:mm"));
                } else {
//                    timeStr.setLength(0);
//                    timeStr = timeGap / DAY + "天前";
                }
            } else if (timeGap > HOUR) {// 1小时-24小时
                if (isSameDay(nowCalendar, msgCalendar)) {
                    timeStr.setLength(0);
                    timeStr.append(timeGap / HOUR + "小时前");
                } else {
                    timeStr.setLength(0);
                    timeStr.append("昨天 " + getFormatTimeFromTimestamp(timestamp, "HH:mm"));
                }
            } else if (timeGap > MINUTE) {// 1分钟-59分钟
                timeStr.setLength(0);
                timeStr.append(timeGap / MINUTE + "分钟前");
            } else {// 1秒钟-59秒钟
                timeStr.setLength(0);
                timeStr.append("刚刚");
            }
        }
        return timeStr.toString();
    }

    public static boolean isSameHalfDay(Calendar now, Calendar msg) {
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int msgHOur = msg.get(Calendar.HOUR_OF_DAY);

        if (nowHour <= 12 & msgHOur <= 12) {
            return true;
        } else return nowHour >= 12 & msgHOur >= 12;
    }

    public static boolean isSameDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);
        return nowDay == msgDay;
    }

    public static boolean isYesterDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return (nowDay - msgDay) == 1;
    }

    public static boolean isTheDayBeforeYesterDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return (nowDay - msgDay) == 2;
    }

    public static boolean isSameYear(Calendar now, Calendar msg) {
        int nowYear = now.get(Calendar.YEAR);
        int msgYear = msg.get(Calendar.YEAR);

        return nowYear == msgYear;
    }

    /**
     * 相差天数
     *
     * @param now
     * @param msg
     * @return
     */
    public static int getDayMinusCount(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return Math.abs(nowDay - msgDay);
    }

    /**
     * 根据时间戳获取指定格式的时间，如2011-11-30 08:40
     *
     * @param timestamp 时间戳 单位为秒
     * @param format    指定格式 如果为null或空串则使用默认格式"yyyy-MM-dd HH:mm"
     * @return
     */
    public static String getFormatTimeFromTimestamp(long timestamp,
                                                    String format) {
        long times = timestamp * 1000L;
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.valueOf(sdf.format(new Date(times))
                    .substring(0, 4));
            System.out.println("currentYear: " + currentYear);
            System.out.println("year: " + year);
            if (currentYear == year) {// 如果为今年则不显示年份
                sdf.applyPattern(FORMAT_MONTH_DAY_TIME);
            } else {
                sdf.applyPattern(FORMAT_DATE_TIME);
            }
        } else {
            sdf.applyPattern(format);
        }
        Date date = new Date(times);
        return sdf.format(date);
    }

    /**
     * 根据时间戳获取时间字符串，并根据指定的时间分割数partionSeconds来自动判断返回描述性时间还是指定格式的时间
     *
     * @param timestamp      时间戳 单位是毫秒
     * @param partionSeconds 时间分割线，当现在时间与指定的时间戳的秒数差大于这个分割线时则返回指定格式时间，否则返回描述性时间
     * @param format
     * @return
     */
    public static String getMixTimeFromTimestamp(long timestamp,
                                                 long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        if (timeGap <= partionSeconds) {
            return getDescriptionTimeFromTimestamp(timestamp);
        } else {
            return getFormatTimeFromTimestamp(timestamp, format);
        }
    }

    /**
     * 获取当前日期的指定格式的字符串
     * HH:mm 24小时制  hh:mm 12小时制
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:mm"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    /**
     * 将日期字符串以指定格式转换为Date
     *
     * @param timeStr 日期字符串
     * @param format  指定的日期格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:mm"
     * @return
     */
    public static Date getTimeFromString(String timeStr, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        try {
            return sdf.parse(timeStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 字符串转时间戳 秒
     *
     * @param timeStr 日期字符串
     * @param format  指定的日期格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:mm"
     * @return
     */
    public static long getTimestampFromString(String timeStr, String format) {
        long l = 0;
        Date d;
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        try {
            d = sdf.parse(timeStr);
            l = d.getTime() / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * HH:mm 24小时制  hh:mm 12小时制
     * 将Date以指定格式转换为日期时间字符串
     *
     * @param time   日期
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:mm"
     * @return
     */
    public static String getStringFromTime(Date time, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(time);
    }


    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        return getFormatTimeFromTimestamp(ms1, "MM-dd").equals(getFormatTimeFromTimestamp(ms2, "MM-dd"));
    }


}
package com.xy.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期工具类
 */
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * yyyyMMdd
     */
    public final static String SHORT_FORMAT = "yyyyMMdd";

    /**
     * yyyyMMddHHmmss
     */
    public final static String LONG_FORMAT = "yyyyMMddHHmmss";

    /**
     * yyyy-MM-dd
     */
    public final static String WEB_FORMAT = "yyyy-MM-dd";

    /**
     * HHmmss
     */
    public final static String TIME_FORMAT = "HHmmss";

    /**
     * yyyyMM
     */
    public final static String MONTH_FORMAT = "yyyyMM";

    /**
     * yyyy年MM月dd日
     */
    public final static String CHINA_FORMAT = "yyyy年MM月dd日";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public final static String LONG_WEB_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm
     */
    public final static String LONG_WEB_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";

    /**
     * 验证yyyy-MM-dd，yyyy-MM-dd HH:mm:ss，yyyy-MM-dd HH:mm三种字符串时间格式
     */
    public final static String YYYY_MM_DD_MATCH = "[0-9]{4}-[0-9]{2}-[0-9]{2}\\s{0,1}[0-9]{0,2}:{0,1}[0-9]{0,2}:{0,1}[0-9]{0,2}";

    /**
     * 日期对象解析成日期字符串基础方法，可以据此封装出多种便捷的方法直接使用
     *
     * @param date   待格式化的日期对
     * @param format 输出的格式
     * @return 格式化的字符
     */
    public static String format(Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).format(date);
    }

    /**
     * 格式化当前时间
     *
     * @param format 输出的格式
     * @return
     */
    public static String formatCurrent(String format) {
        if (StringUtils.isBlank(format)) {
            return StringUtils.EMPTY;
        }
        return format(new Date(), format);
    }

    /**
     * 日期字符串解析成日期对象基础方法，可以在此封装出多种便捷的方法直接使用
     *
     * @param dateStr 日期字符
     * @param format  输入的格式
     * @return 日期对象
     * @throws ParseException
     */
    public static Date parse(String dateStr, String format) throws ParseException {
        if (StringUtils.isBlank(format)) {
            throw new ParseException("format can not be null.", 0);
        }
        if (dateStr == null || dateStr.length() < format.length()) {
            throw new ParseException("date string's length is too small.", 0);
        }
        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).parse(dateStr);
    }

    /**
     * 日期字符串格式化基础方法，可以在此封装出多种便捷的方法直接使�?
     *
     * @param dateStr   日期字符
     * @param formatIn  输入的日期字符串的格式
     * @param formatOut 输出日期字符串的格式
     * @return 已经格式化的字符
     * @throws ParseException
     */
    public static String format(String dateStr, String formatIn, String formatOut) throws ParseException {
        Date date = parse(dateStr, formatIn);
        return format(date, formatOut);
    }

    /**
     * 把日期对象按<code>yyyyMMdd</code>格式解析成字符串
     *
     * @param date 待格式化的日期对
     * @return 格式化的字符串
     */
    public static String formatShort(Date date) {
        return format(date, SHORT_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyyMMdd</code>格式，进行格式化
     *
     * @param dateStr  待格式化的日期字符串
     * @param formatIn 输入的日期字符串的格式
     * @return 格式化的字符
     */
    public static String formatShort(String dateStr, String formatIn) throws ParseException {
        return format(dateStr, formatIn, SHORT_FORMAT);
    }

    /**
     * 把日期对象按<code>yyyy-MM-dd</code>格式解析成字符串
     *
     * @param date 待格式化的日期对
     * @return 格式化的字符
     */
    public static String formatWeb(Date date) {
        return format(date, WEB_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyy-MM-dd</code>格式，进行格式化
     *
     * @param dateStr  待格式化的日期字符串
     * @param formatIn 输入的日期字符串的格式
     * @return 格式化的字符
     * @throws ParseException
     */
    public static String formatWeb(String dateStr, String formatIn) throws ParseException {
        return format(dateStr, formatIn, WEB_FORMAT);
    }

    /**
     * 把日期对象按<code>yyyyMM</code>格式解析成字符串
     *
     * @param date 待格式化的日期对
     * @return 格式化的字符
     */
    public static String formatMonth(Date date) {
        return format(date, MONTH_FORMAT);
    }

    /**
     * 把日期对象按<code>HHmmss</code>格式解析成字符串
     *
     * @param date 待格式化的日期对
     * @return 格式化的字符
     */
    public static String formatTime(Date date) {
        return format(date, TIME_FORMAT);
    }

    /**
     * 获取yyyyMMddHHmmss+n位随机数格式的时间戳
     *
     * @param n 随机数位
     * @return
     */
    public static String getTimestamp(int n) {
        return formatCurrent(LONG_FORMAT) + RandomStringUtils.randomNumeric(n);
    }

    /**
     * 根据日期格式返回昨日日期
     *
     * @param format 日期格式
     * @return
     */
    public static String getYesterdayDate(String format) {
        return getDateCompareToday(format, -1, 0);
    }

    /**
     * 把当日日期作为基准，按照格式返回相差定间隔的日期
     *
     * @param format     日期格式
     * @param daysAfter  和当日比相差几天，例如3代表3天后，-1代表1天前
     * @param monthAfter 和当日比相差几月，例如2代表2月后，-3代表3月前
     * @return
     */
    public static String getDateCompareToday(String format, int daysAfter, int monthAfter) {
        Calendar today = Calendar.getInstance();
        if (daysAfter != 0) {
            today.add(Calendar.DATE, daysAfter);
        }
        if (monthAfter != 0) {
            today.add(Calendar.MONTH, monthAfter);
        }
        return format(today.getTime(), format);
    }

    /**
     * 根据日期格式返回上月的日期
     *
     * @param format format
     * @return 一个月之前的日期
     */
    public static String getLastMonth(String format) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH, -1);
        return format(today.getTime(), format);
    }

    /**
     * 平移当前时间，以分为单元，minutes
     *
     * @param minutes
     * @return
     */
    public static Date addMinutes(long minutes) {
        return addMinutes(new Date(), (int) minutes);
    }

    /**
     * 平移当前时间，以秒为单元，minutes
     *
     * @param secs
     * @return
     */
    public static Date addSeconds(long secs) {
        return addSeconds(new Date(), (int) secs);
    }

    /**
     * 得到时间差
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @param type  时间类型
     * @return 天数
     */
    public static long dateMinusDateForDays(String begin, String end, String type) throws ParseException {
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat(type);
        Date date1 = ft.parse(begin);
        Date date2 = ft.parse(end);
        quot = date2.getTime() - date1.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return quot;
    }

    /**
     * 验证传过来的字符串是否是时间格式
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        Pattern pattern = Pattern.compile(YYYY_MM_DD_MATCH);
        return pattern.matcher(str).matches();
    }

    /**
     * 将时间转为cron表达式
     *
     * @param date
     * @return
     */
    public static String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return format(date, dateFormat);
    }

    /**
     * 获取日期星期
     *
     * @param date
     * @return
     */
    public static int getWeekByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date); // 设置时间
        }
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取日期最小时间
     *
     * @param date
     * @return
     */
    public static Date getMinTimeByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date); // 设置时间
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期最大时间
     *
     * @param date
     * @return
     */
    public static Date getMaxTimeByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date); // 设置时间
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

}
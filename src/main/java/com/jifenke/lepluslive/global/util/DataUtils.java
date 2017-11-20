package com.jifenke.lepluslive.global.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 数据处理
 */
public class DataUtils {

    public static final String sdf = "yyyyMMddHHmmss";
    public static final String sdf2 = "yyyyMMdd";
    public static final String sdf3 = "yyyy-MM-dd";
    public static final String sdf4 = "yyyy-MM-dd HH:mm:ss";
    public static final String sdf5 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 计算两个字符串日期相差的天数
     *
     * @param date1  字符串日期1
     * @param date2  字符串日期2
     * @param format 日期格式化方式 默认yyyy-MM-dd
     */
    public static int dayDiff(String date1, String date2, String format) {

        long diff = 0;
        try {
            if (format == null) {
                long d1 = DateUtils.parseDate(date1, sdf3).getTime();
                long d2 = DateUtils.parseDate(date2, sdf3).getTime();
                diff = (Math.abs(d1 - d2) / (1000 * 60 * 60 * 24));
            } else {
                SimpleDateFormat s = new SimpleDateFormat(format);
                long d1 = s.parse(date1).getTime();
                long d2 = s.parse(date2).getTime();
                diff = (Math.abs(d1 - d2) / (1000 * 60 * 60 * 24));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) diff;
    }

    /**
     * 获取星期几
     * 日~一~六 === 1~7
     *
     * @param date 时间
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取N天前后日期
     *
     * @param date   yyyy-MM-dd
     * @param amount 和date相差天数，可为负
     */
    public static String getNextDateByDay(String date, int amount) {
        try {
            Date dateValue = DateUtils.parseDate(date, sdf3);
            return DateFormatUtils.format(DateUtils.addDays(dateValue, amount), sdf3);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    //比较两个日期是否是同一天
    public static boolean isSameDay(Date day1, Date day2) {
        String ds1 = DateFormatUtils.format(day1, sdf3);
        String ds2 = DateFormatUtils.format(day2, sdf3);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    //格式化时间为yyyy-MM-dd
    public static String dateTOString(Date date) {
        return DateFormatUtils.format(date, sdf3);
    }

    /**
     * 获取今日的零点零分
     */
    public static Date getCurrDayBeginDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 格式化时间为yyyyMMddHHmmss
     */
    public static String formatDate(Date date) {
        return DateFormatUtils.format(date, sdf);
    }

    /**
     * 格式化时间为yyyyMMdd
     */
    public static String formatYYYYMMDD(Date date) {
        return DateFormatUtils.format(date, sdf2);
    }

}

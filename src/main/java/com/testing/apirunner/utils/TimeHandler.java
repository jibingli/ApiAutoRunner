package com.testing.apirunner.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHandler {
    private static Logger logger = Logger.getLogger(TimeHandler.class);
    final private static SimpleDateFormat formatWithoutSFM = new SimpleDateFormat(("yyyy-MM-dd"));
    final private static SimpleDateFormat formatWithSFM = new SimpleDateFormat(("yyyy-MM-dd HH:mm:ss"));

    /**
     * 返回不带时分秒格式的日期
     *
     * @param date 待格式化的日期
     * @return
     */
    public static String getFormatWithoutSFM(Date date) {
        return formatWithoutSFM.format(date);
    }

    public static Date formatDateWithoutSFM(Date date) {
        try {
            Date dt = formatWithoutSFM.parse(getFormatWithoutSFM(date));
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 返回带时分秒格式的日期
     *
     * @param date 待格式化的日期
     * @return
     */
    public static String getFormatWithSFM(Date date) {
        return formatWithSFM.format(date);
    }

    /**
     * 返回当前日期前后多少天的日期
     *
     * @param day 距离今天的时间间隔
     * @return
     */
    public static Date getDatetimeOfDay(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        if (day >= 0) {
            return getDateAfter(new Date(), day);
        }
        {
            return getDateBefore(new Date(), Math.abs(day));
        }
    }

    /**
     * @param day 0表示今天
     * @return unixi时间戳 如1551196800000
     */
    public static String getDateTimeStamp(Integer day) {
        try {
            Date dt = formatWithoutSFM.parse(getFormatWithoutSFM(getDatetimeOfDay(day)));
            return String.valueOf(dt.getTime());
        } catch (ParseException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("解析日期格式失败! ");

        }
    }

    public static Date getDateWithoutSFM(Integer day) {
        try {
            Date dt = formatWithoutSFM.parse(getFormatWithoutSFM(getDatetimeOfDay(day)));
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }
}

package com.weilai.jigsawpuzzle.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final String TAG = "DateUtil";

    private static SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat mDateTimeFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");

    public static long getTodayEndUnixTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis() / 1000;
    }

    public static long getOneMonthAgoStartUnixTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static String calendarToDateTimeString(Calendar c){
        return mDateTimeFormat.format(c.getTime());
    }

    public static String calendarToDateString(Calendar c){
        return mDateFormat.format(c.getTime());
    }
    public static String longTimeToDateString(long time){
        return mDateFormat.format(new Date(time));
    }

    public static String unixTimeToDateString(long unixTime) {
        return mDateFormat.format(unixTime * 1000);
    }

    public static String unixTimeToDateTimeString(long unixTime) {
        return mDateTimeFormat.format(unixTime * 1000);
    }

    public static String unixTimeToDateTimeString2(long unixTime) {
        return mDateTimeFormat2.format(unixTime * 1000);
    }

    public static String now() {
        return mDateTimeFormat.format(new Date());
    }
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        return date;
    }
    public static String DateTostr(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }
    public static String dateToStr1(Date date){
        String str = mDateFormat.format(date);
        return str;
    }
    public static int  dateToWeek(long  time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int i =  (c.get(Calendar.DAY_OF_WEEK) -1 + 7) % 7;
        if (i == 0){
            return  7 ;
        }else {
            return  i ;
        }
    }
}

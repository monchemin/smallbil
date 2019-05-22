package com.smallbil.utils;

import android.util.Log;
import android.util.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class DateUtils {

    public static String shortFormat = "yyyy-MM-dd";
    public static String longFormat = "yyyy-MM-dd hh:mm:ss";
    public static String startFormat = " 00:00:00";
    public static String endFormat = " 23:59:59";

    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toISO(Date date) {
        return "";
    }

    public static Pair<Date, Date> getDay(Date day) {
        SimpleDateFormat shortF = new SimpleDateFormat(shortFormat);

        String dayString = shortF.format(day);
        String dayStart = dayString + startFormat;
        String dayEnd = dayString + endFormat;
        return getPair(dayStart, dayEnd);
    }

    public static Pair<Date, Date> getWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        DateFormat df = new SimpleDateFormat(shortFormat, Locale.getDefault());
        String startDate = "", endDate = "";

        startDate = df.format(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        endDate = df.format(calendar.getTime());
        String dayStart = startDate + startFormat;
        String dayEnd = endDate + endFormat;

       return getPair(dayStart, dayEnd);

    }
    public static Pair<Date, Date> getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        DateFormat df = new SimpleDateFormat(shortFormat, Locale.getDefault());
        String startDate = "", endDate = "";
        startDate = df.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df.format(calendar.getTime());
        String dayStart = startDate + startFormat;
        String dayEnd = endDate + endFormat;
        return getPair(dayStart, dayEnd);

    }


    private static Pair<Date, Date> getPair(String startDate, String endDate){
        SimpleDateFormat longF = new SimpleDateFormat(longFormat);
        try {
            return new Pair<>(longF.parse(startDate), longF.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return null;
    }
}

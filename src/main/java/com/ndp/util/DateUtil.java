package com.ndp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
        super();
    }

    public static int dateDiff(Date tgl1, Date tgl2) {
        return tgl2.compareTo(tgl1);
    }

    public static Date firstDate(Date tanggal) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggal);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date lastDate(Date tanggal) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggal);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date strToDate(String tanggal, String format) {
        try {
            return new SimpleDateFormat(format).parse(tanggal);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToStr(Date tanggal, String format) {
        return new SimpleDateFormat(format).format(tanggal);
    }
}

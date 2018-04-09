package com.ttn.bluebell.core.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date minusDays(Date date, int days) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    public static Date localDateToDateConverter(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean dateInBetweenOrEqualTo(LocalDate startDate, LocalDate endDate, LocalDate date) {
        return ((date.isAfter(startDate)) && (date.isBefore(endDate))) || (date.isEqual(startDate) || date.isEqual(endDate));
    }

    public static boolean dateInBetweenOrEqualTo(Date startDate, Date endDate, Date date) {
        return ((date.after(startDate)) && (date.before(endDate))) || (date.compareTo(startDate) == 0 || date.compareTo(endDate) == 0);
    }

}

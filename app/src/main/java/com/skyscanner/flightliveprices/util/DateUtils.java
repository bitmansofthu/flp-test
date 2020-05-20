package com.skyscanner.flightliveprices.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getNextDayOfWeek(Date from, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        while (cal.get(Calendar.DAY_OF_WEEK) != day) {
            cal.add(Calendar.DATE, 1);
        }

        return cal.getTime();
    }

    public static Date getNextDay(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        cal.add(Calendar.DATE, 1);

        return cal.getTime();
    }

}

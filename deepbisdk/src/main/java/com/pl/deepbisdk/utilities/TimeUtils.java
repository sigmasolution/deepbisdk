package com.pl.deepbisdk.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTimeISO8601() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}

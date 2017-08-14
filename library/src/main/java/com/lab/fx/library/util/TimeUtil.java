package com.lab.fx.library.util;

import java.text.SimpleDateFormat;

/**
 * Created by febri on 30/07/17.
 */

public class TimeUtil {
    private static SimpleDateFormat DDMMYY = new SimpleDateFormat("dd/MM/yy HH:mm");
    private static SimpleDateFormat HHMM   = new SimpleDateFormat("HH:mm");
    public static String toDateHour(long p_timemilis) {
        return DDMMYY.format(p_timemilis);
    }
    public static String toHour(long p_timemilis) {
        return HHMM.format(p_timemilis);
    }
}

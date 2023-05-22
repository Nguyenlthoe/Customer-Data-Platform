package bk.edu.utils;

import java.util.Calendar;

public class TimeUtils {
    public static long A_HOUR_IN_MILLISECOND = 1000 * 60 * 60;

    public static long A_MINUTE_IN_MILLISECOND = 1000 * 60;
    public static long A_DAY_IN_MILLISECOND = 24 * A_HOUR_IN_MILLISECOND;

    public static Calendar getYearBefore(int numYear) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        try {
            for (int i = 0; i < numYear; i++) {
                cal.add(Calendar.HOUR, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }
}

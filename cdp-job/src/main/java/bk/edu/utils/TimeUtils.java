package bk.edu.utils;

import java.util.Calendar;

public class TimeUtils {
    public static long A_HOUR_IN_MILLISECOND = 1000 * 60 * 60;

    public static long A_MINUTE_IN_MILLISECOND = 1000 * 60;
    public static long A_DAY_IN_MILLISECOND = 24 * A_HOUR_IN_MILLISECOND;

    public static Long getYearBefore(int numYear) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        try {
            for (int i = 0; i < numYear; i++) {
                cal.add(Calendar.YEAR, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal.getTimeInMillis();
    }

    public static Long getDayBefore(int numDay){
        Calendar cal = Calendar.getInstance();
        for(int i = 0; i < numDay; i++){
            cal.add(Calendar.DATE, -1);
        }
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTimeInMillis();
    }
}

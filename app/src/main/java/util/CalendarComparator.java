package util;

import java.util.Calendar;

public class CalendarComparator {
    public boolean isDayAfter(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        int dayOfYear1 = cal1.get(Calendar.DAY_OF_YEAR);
        int dayOfYear2 = cal2.get(Calendar.DAY_OF_YEAR);

        if (year1 > year2) {
            return true;
        }
        else return dayOfYear1 > dayOfYear2;
    }
}

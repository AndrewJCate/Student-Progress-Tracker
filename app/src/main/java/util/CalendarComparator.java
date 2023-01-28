package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarComparator {
    public boolean isDayAfter(String day1, String day2) {
        Date date1 = null;
        Date date2 = null;

        if (day1.trim().equals("") || day2.trim().equals("")) {
            return false;
        }

        try {
            date1 = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(day1);
            date2 = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(day2);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.after(date2);
    }
}

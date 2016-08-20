package ambiesoft.start.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bryanyhy on 20/8/2016.
 */
public class DateFormatter {

    private static Calendar calendar;
    private static SimpleDateFormat sdf;
//
//    public DateFormatter() {
//        calendar = Calendar.getInstance();
//    }

    public static String getTodayDate() {
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(calendar.getTime());
    }

    public static int getCurrentYear() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int getCurrentDay() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static String getSelectedDateWithLeadingZero(int selectedDay, int selectedMonth, int selectedYear) {
        if (selectedDay < 10 && (selectedMonth + 1) < 10) {
            // add leading 0 to both day and month
            return "0" + selectedDay + "-0" + (selectedMonth + 1) + "-" + selectedYear;
        } else if (selectedDay < 10) {
            // add leading 0 to day
            return "0" + selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
        } else if ((selectedMonth + 1) < 10) {
            // add leading 0 to month
            return selectedDay + "-0" + (selectedMonth + 1) + "-" + selectedYear;
        } else {
            // no leading 0 is needed
            return selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
        }
    }

    public static String getSelectedTimeWithLeadingZero(int selectedHour, int selectedMinute) {
        if (selectedHour < 10 && selectedMinute < 10) {
            // add leading 0 to both hour and minute
            return "0" + selectedHour + ":0" + selectedMinute;
        } else if (selectedHour < 10) {
            // add leading 0 to hour
            return "0" + selectedHour + ":" + selectedMinute;
        } else if (selectedMinute < 10) {
            // add leading 0 to minute
            return selectedHour + ":0" + selectedMinute;
        } else {
            // no leading 0 is needed
            return selectedHour + ":" + selectedMinute;
        }
    }

    public static String getEndingTimeForPerformance(String selectedSTime, int selectedDuration) throws ParseException {
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm");
        Date sTime = sdf.parse(selectedSTime);
        calendar.setTime(sTime);
        calendar.add(Calendar.MINUTE, selectedDuration);
        return sdf.format(calendar.getTime());
    }
}

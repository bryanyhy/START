package ambiesoft.start.model.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bryanyhy on 20/8/2016.
 */
// class that is responsible for all the date or time formatting functions
public class DateFormatter {

    private static Calendar calendar;
    private static SimpleDateFormat sdf;

    // return today's date
    public static String getTodayDate() {
        calendar = Calendar.getInstance();
        // specific format of date
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(calendar.getTime());
    }

    // get current year
    public static int getCurrentYear() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    // get current month
    public static int getCurrentMonth() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    // get current day
    public static int getCurrentDay() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    // get current hour
    public static int getCurrentHour() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    // get current minute
    public static int getCurrentMinute() {
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    // return the selected date, with leading 0 if the day or month is not 2 digit numbers
    public static String getSelectedDateWithLeadingZero(int selectedDay, int selectedMonth, int selectedYear) {
        if (selectedDay < 10 && (selectedMonth + 1) < 10) {
            // add leading 0 to both day and month
            return "0" + selectedDay + "/0" + (selectedMonth + 1) + "/" + selectedYear;
        } else if (selectedDay < 10) {
            // add leading 0 to day
            return "0" + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
        } else if ((selectedMonth + 1) < 10) {
            // add leading 0 to month
            return selectedDay + "/0" + (selectedMonth + 1) + "/" + selectedYear;
        } else {
            // no leading 0 is needed
            return selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
        }
    }

    // return the selected time, with leading 0 if the hour or minute is not 2 digit numbers
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

    // calculate and get the ending time when user create a performance, based on starting time and duration
    public static String getEndingTimeForPerformance(String selectedSTime, int selectedDuration) throws ParseException {
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm");
        Date sTime = sdf.parse(selectedSTime);
        calendar.setTime(sTime);
        calendar.add(Calendar.MINUTE, selectedDuration);
        return sdf.format(calendar.getTime());
    }

    public static boolean checkIfTimeIsInBetween(String cDate, String csTime, String ceTime, String sDate, String sTime, String eTime) throws ParseException {

        // Check Start Time
        java.util.Date checkSTime = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .parse(cDate + " " + csTime);
        Calendar checkSCalendar = Calendar.getInstance();
        checkSCalendar.setTime(checkSTime);

        // Check End Time
        java.util.Date checkETime = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .parse(cDate + " " + ceTime);
        Calendar checkECalendar = Calendar.getInstance();
        checkECalendar.setTime(checkETime);

        // Start Time
        java.util.Date startTime = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .parse(sDate + " " + sTime);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);

        // End Time
        java.util.Date endTime = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .parse(sDate + " " + eTime);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);

        if (checkETime.compareTo(checkSTime) < 0) {
            checkECalendar.add(Calendar.DATE, 1);
            checkETime = checkECalendar.getTime();
            Log.i("System.out", "Check End time changed to: " + endTime);
        }

        if (endTime.compareTo(startTime) < 0) {
            endCalendar.add(Calendar.DATE, 1);
            endTime = endCalendar.getTime();
            Log.i("System.out", "End time changed to: " + endTime);
        }

        Log.i("System.out", "1. " + checkSTime);
        Log.i("System.out", "2. " + checkETime);
        Log.i("System.out", "3. " + startTime);
        Log.i("System.out", "4. " + endTime);

        if (checkSTime.compareTo(startTime) == 0 || checkSTime.compareTo(endTime) == 0
                || checkETime.compareTo(startTime) == 0 || checkETime.compareTo(endTime) == 0) {
            Log.i("System.out", "Time is same");
            return true;
        } else if (checkSTime.before(startTime) && checkETime.before(startTime)) {
            Log.i("System.out", "Both time is before starting time");
            return false;
        } else if (checkSTime.after(endTime) && checkETime.after(endTime)) {
            Log.i("System.out", "Both time is after starting time");
            return false;
        } else {
            Log.i("System.out", "Time is clashed");
            return true;
        }
    }
}


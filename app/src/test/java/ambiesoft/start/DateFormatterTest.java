package ambiesoft.start;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static ambiesoft.start.utility.DateFormatter.getEndingTimeForPerformance;
import static ambiesoft.start.utility.DateFormatter.getSelectedDateWithLeadingZero;
import static ambiesoft.start.utility.DateFormatter.getSelectedTimeWithLeadingZero;
import static ambiesoft.start.utility.DateFormatter.getTodayDate;
import static org.junit.Assert.*;

/**
 * Created by Bryanyhy on 21/8/2016.
 */
// Testing class for methods in DateFormatter class
public class DateFormatterTest {

    // Test if getTodayDate methods returns correct today's date in String format
    @Test
    public void testGetTodayDateShouldReturnCorrectString() throws Exception {
        // There are 2 methods for generating the expected output:
        // 1. getting today's date by calendar methods.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String expectedOutput = sdf.format(calendar.getTime());
        // 2. hard code today's date, and tester have to change it if they test on different date.
//        String expectedOutput = "21-08-2016";
        String actualOutput = getTodayDate();
        assertEquals(expectedOutput, actualOutput);
    }

    // Test if we can get the correct date, with leading zero if necessary
    @Test
    public void testGetSelectedDateWithLeadingZero() {
        String expectedOutput1 = "03-09-2016";
        String expectedOutput2 = "03-10-2016";
        String expectedOutput3 = "13-09-2016";
        String expectedOutput4 = "13-10-2016";
        // Date picker only return int, without leading zero,
        // so it is better to add leading zero for better formatting
        // As the int value of month we retrieved from date picker is always (actual month - 1),
        // we have to make a minor change to simulate the actual data we will get in datePicker
        String actualOutput1 = getSelectedDateWithLeadingZero(3, (9 - 1), 2016);
        String actualOutput2 = getSelectedDateWithLeadingZero(3, (10 - 1), 2016);
        String actualOutput3 = getSelectedDateWithLeadingZero(13, (9 - 1), 2016);
        String actualOutput4 = getSelectedDateWithLeadingZero(13, (10 - 1), 2016);
        assertEquals(expectedOutput1, actualOutput1);
        assertEquals(expectedOutput2, actualOutput2);
        assertEquals(expectedOutput3, actualOutput3);
        assertEquals(expectedOutput4, actualOutput4);
    }

    // Test if we can get the correct Time, with leading zero if necessary
    @Test
    public void testGetSelectedTimeWithLeadingZero() {
        String expectedOutput1 = "09:05";
        String expectedOutput2 = "09:15";
        String expectedOutput3 = "19:05";
        String expectedOutput4 = "19:15";
        // TimePicker only return int, without leading zero,
        // so it is better to add leading zero for better formatting
        String actualOutput1 = getSelectedTimeWithLeadingZero(9, 5);
        String actualOutput2 = getSelectedTimeWithLeadingZero(9, 15);
        String actualOutput3 = getSelectedTimeWithLeadingZero(19, 5);
        String actualOutput4 = getSelectedTimeWithLeadingZero(19, 15);
        assertEquals(expectedOutput1, actualOutput1);
        assertEquals(expectedOutput2, actualOutput2);
        assertEquals(expectedOutput3, actualOutput3);
        assertEquals(expectedOutput4, actualOutput4);
    }

    // Test if the ending time of the performance is correctly calculated
    @Test
    public void testGetCorrectEndingTime() throws ParseException {
        String expectedOutput1 = "23:05";
        // First input parameter is starting time, second is the duration in minutes
        String actualOutput1 = getEndingTimeForPerformance("21:30", 95);
        assertEquals(expectedOutput1, actualOutput1);
    }
}

package ambiesoft.start;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ambiesoft.start.model.dataclass.Performance;

import static ambiesoft.start.model.utility.DateFormatter.checkIfTimeIsInBetween;
import static ambiesoft.start.model.utility.DateFormatter.getEndingTimeForPerformance;
import static ambiesoft.start.model.utility.DateFormatter.getSelectedDateWithLeadingZero;
import static ambiesoft.start.model.utility.DateFormatter.getSelectedTimeWithLeadingZero;
import static ambiesoft.start.model.utility.DateFormatter.getTodayDate;
import static ambiesoft.start.model.utility.DateFormatter.sortPerformanceListByDateTimeAndDuration;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String expectedOutput = sdf.format(calendar.getTime());
        // 2. hard code today's date, and tester have to change it if they test on different date.
//        String expectedOutput = "21/08/2016";
        String actualOutput = getTodayDate();
        assertEquals(expectedOutput, actualOutput);
    }

    // Test if we can get the correct date, with leading zero if necessary
    @Test
    public void testGetSelectedDateWithLeadingZero() {
        String expectedOutput1 = "03/09/2016";
        String expectedOutput2 = "03/10/2016";
        String expectedOutput3 = "13/09/2016";
        String expectedOutput4 = "13/10/2016";
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

    // return false if the desired time is not in between or same as existing performance's time
    @Test
    public void testIfPerformanceTimeIsNotInBetweenShouldReturnFalse() throws ParseException {
        // six parameters for input: date, start and end time we want to check, and date, start and end time on existing performance
        // test on different date
        boolean expectedOutput1 = checkIfTimeIsInBetween("03/09/2016", "17:00", "18:00", "04/09/2016", "17:00", "18:00");
        boolean expectedOutput2 = checkIfTimeIsInBetween("03/09/2016", "17:00", "18:00", "02/09/2016", "17:00", "18:00");
        // test on different time period
        boolean expectedOutput3 = checkIfTimeIsInBetween("03/09/2016", "16:00", "16:59", "03/09/2016", "17:00", "18:00");
        boolean expectedOutput4 = checkIfTimeIsInBetween("03/09/2016", "18:01", "19:00", "03/09/2016", "17:00", "18:00");
        // test on date and time involving 2 dates
        boolean expectedOutput5 = checkIfTimeIsInBetween("04/09/2016", "00:31", "00:40", "03/09/2016", "23:00", "00:30");
        boolean expectedOutput6 = checkIfTimeIsInBetween("03/09/2016", "23:00", "00:30", "04/09/2016", "00:31", "00:40");
        assertEquals(false, expectedOutput1);
        assertEquals(false, expectedOutput2);
        assertEquals(false, expectedOutput3);
        assertEquals(false, expectedOutput4);
        assertEquals(false, expectedOutput5);
        assertEquals(false, expectedOutput6);
    }

    // return true if the desired time is in between or same as existing performance's time
    @Test
    public void testIfPerformanceTimeIsInBetweenShouldReturnTrue() throws ParseException {
        // test on same starting or ending time
        boolean expectedOutput1 = checkIfTimeIsInBetween("03/09/2016", "16:00", "16:59", "03/09/2016", "16:59", "18:00");
        boolean expectedOutput2 = checkIfTimeIsInBetween("03/09/2016", "18:01", "19:00", "03/09/2016", "17:00", "18:01");
        // test on time in between on same date
        boolean expectedOutput3 = checkIfTimeIsInBetween("03/09/2016", "16:00", "16:59", "03/09/2016", "15:00", "17:00");
        boolean expectedOutput4 = checkIfTimeIsInBetween("03/09/2016", "14:00", "15:30", "03/09/2016", "15:00", "17:00");
        boolean expectedOutput5 = checkIfTimeIsInBetween("03/09/2016", "16:30", "17:30", "03/09/2016", "15:00", "17:00");
        // test on date and time involving 2 dates, and having same starting or ending time
        boolean expectedOutput6 = checkIfTimeIsInBetween("04/09/2016", "00:30", "00:40", "03/09/2016", "23:00", "00:30");
        boolean expectedOutput7 = checkIfTimeIsInBetween("03/09/2016", "23:50", "00:50", "04/09/2016", "00:50", "01:00");
        // test on date and time involving 2 dates, and is in between starting and ending time
        boolean expectedOutput8 = checkIfTimeIsInBetween("04/09/2016", "00:30", "00:40", "03/09/2016", "23:00", "01:00");
        boolean expectedOutput9 = checkIfTimeIsInBetween("03/09/2016", "23:50", "00:50", "03/09/2016", "23:00", "01:00");
        assertEquals(true, expectedOutput1);
        assertEquals(true, expectedOutput2);
        assertEquals(true, expectedOutput3);
        assertEquals(true, expectedOutput4);
        assertEquals(true, expectedOutput5);
        assertEquals(true, expectedOutput6);
        assertEquals(true, expectedOutput7);
        assertEquals(true, expectedOutput8);
        assertEquals(true, expectedOutput9);
    }

    @Test
    public void testIfPerformanceInDifferentDateWillBeCorrectlySorted() {
        // 3 performance on different date
        Performance per1 = new Performance("05/09/2016", "16:00", 30);
        Performance per2 = new Performance("03/09/2016", "16:00", 30);
        Performance per3 = new Performance("04/09/2016", "16:00", 30);
        // actual result arraylist
        ArrayList<Performance> performances = new ArrayList<>();
        // expected result arraylist
        ArrayList<Performance> expectedPerformances = new ArrayList<>();
        // testing
        performances.add(per1);
        performances.add(per2);
        performances.add(per3);
        // expected result
        expectedPerformances.add(per2);
        expectedPerformances.add(per3);
        expectedPerformances.add(per1);
        // method to test
        sortPerformanceListByDateTimeAndDuration(performances);
        assertEquals(expectedPerformances, performances);
    }

    @Test
    public void testIfPerformanceInDifferentTimeWillBeCorrectlySorted() {
        // 3 performance on different time
        Performance per1 = new Performance("05/09/2016", "17:00", 30);
        Performance per2 = new Performance("05/09/2016", "16:00", 30);
        Performance per3 = new Performance("05/09/2016", "15:00", 30);
        // actual result arraylist
        ArrayList<Performance> performances = new ArrayList<>();
        // expected result arraylist
        ArrayList<Performance> expectedPerformances = new ArrayList<>();
        // testing
        performances.add(per1);
        performances.add(per2);
        performances.add(per3);
        // expected result
        expectedPerformances.add(per3);
        expectedPerformances.add(per2);
        expectedPerformances.add(per1);
        // method to test
        sortPerformanceListByDateTimeAndDuration(performances);
        assertEquals(expectedPerformances, performances);
    }

    @Test
    public void testIfPerformanceInDifferentDurationWillBeCorrectlySorted() {
        // 3 performance on different time
        Performance per1 = new Performance("05/09/2016", "17:00", 75);
        Performance per2 = new Performance("05/09/2016", "17:00", 30);
        Performance per3 = new Performance("05/09/2016", "17:00", 120);
        // actual result arraylist
        ArrayList<Performance> performances = new ArrayList<>();
        // expected result arraylist
        ArrayList<Performance> expectedPerformances = new ArrayList<>();
        // testing
        performances.add(per1);
        performances.add(per2);
        performances.add(per3);
        // expected result
        expectedPerformances.add(per2);
        expectedPerformances.add(per1);
        expectedPerformances.add(per3);
        // method to test
        sortPerformanceListByDateTimeAndDuration(performances);
        assertEquals(expectedPerformances, performances);
    }
}

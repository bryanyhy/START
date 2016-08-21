package ambiesoft.start;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static ambiesoft.start.utility.DateFormatter.getTodayDate;
import static org.junit.Assert.*;

/**
 * Created by Bryanyhy on 21/8/2016.
 */
public class DateFormatterTest {

    // Test if getTodayDate methods returns correct today's date in String format
    @Test
    public void testGetTodayDateShouldReturnCorrectString() throws Exception {
        // There are 2 methods for generating the expected output:
        // 1. getting today's date by calendar methods.
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        String expectedOutput = sdf.format(calendar.getTime());
        // 2. hard code today's date, and tester have to change it if they test on different date.
        String expectedOutput = "21-08-2016";
        String actualOutput = getTodayDate();
        assertEquals(expectedOutput, actualOutput);
    }
}

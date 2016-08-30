package ambiesoft.start.model.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ambiesoft.start.model.dataclass.Performance;

/**
 * Created by Bryanyhy on 19/8/2016.
 */
// class for filtering result functions
public class FilterResult {

    private static ArrayList<Performance> filteredPerformances;

    // Method to do advanced filtering on keyword, category and time
    public static ArrayList<Performance> advancedFilteringOnPerformanceList(ArrayList<Performance> resultList, String filterKeyword, String filterCategory, String filterTime) throws ParseException {
        // initialize the filtered result ArrayList
        filteredPerformances = new ArrayList<>();
        // for every performance object in the list we retrieved from FirebaseUtility
        for (Performance performance: resultList) {
            boolean valid = true;
            // check if there is filter keyword
            if (filterKeyword != null) {
                // check if keyword doesn't match performance's name
                if (performance.getName().toLowerCase().indexOf(filterKeyword.toLowerCase()) == -1) {
                    // if true, the performance object is not valid for the filtering result
                    valid = false;
                }
            }
            // check if there is filter category
            if (filterCategory != null) {
                // check if category doesn't match performance's category
                if (!performance.getCategory().matches(filterCategory)) {
                    // if true, the performance object is not valid for the filtering result
                    valid = false;
                }
            }
            // check if there is filter time
            if (filterTime != null) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                // get the ending time of the performance, and the filter date in Date format
                Date perETime = df.parse(performance.geteTime());
                Date filTime = df.parse(filterTime);
                // check if filter date is before or equal to the ending time
                if (perETime.before(filTime) || perETime.equals(filTime)) {
                    // if any of them is true, the performance object is not valid for the filtering result
                    valid = false;
                }
            }
            // check if the perforamnce object is valid after filtering
            if (valid) {
                // if all the filter options match the performance object, add it into ArrayList of result
                filteredPerformances.add(performance);
            }
        }
        // return the filtered performance result in ArrayList
        return filteredPerformances;
    }
}

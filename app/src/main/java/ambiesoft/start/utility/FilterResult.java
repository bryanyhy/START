package ambiesoft.start.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ambiesoft.start.dataclass.Performance;

/**
 * Created by Bryanyhy on 19/8/2016.
 */
public class FilterResult {

    private static ArrayList<Performance> filteredPerformances;

    public static ArrayList<Performance> advancedFilteringOnPerformanceList(ArrayList<Performance> resultList, String filterKeyword, String filterCategory, String filterTime) throws ParseException {
        filteredPerformances = new ArrayList<>();
        for (Performance performance: resultList) {
            boolean valid = true;
            if (filterKeyword != null) {
                if (performance.getName().toLowerCase().indexOf(filterKeyword.toLowerCase()) == -1) {
                    valid = false;
                }
            }
            if (filterCategory != null) {
                if (!performance.getCategory().matches(filterCategory)) {
                    valid = false;
                }
            }
            if (filterTime != null) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Date perSTime = df.parse(performance.getsTime());
                Date perETime = df.parse(performance.geteTime());
                Date filTime = df.parse(filterTime);
                if (perETime.before(filTime) || perETime.equals(filTime)) {
                    valid = false;
                }
//                else if (!(perSTime.before(filTime) && perETime.after(filTime))) {
//                    valid = false;
//                }
            }
            if (valid) {
                filteredPerformances.add(performance);
            }
        }
        return filteredPerformances;
    }
}

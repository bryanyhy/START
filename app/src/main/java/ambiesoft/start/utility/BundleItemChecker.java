package ambiesoft.start.utility;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.DateFormatter.getTodayDate;

/**
 * Created by Bryanyhy on 20/8/2016.
 */
public class BundleItemChecker {


    public static String getFilterDateFromBundle(Bundle bundle) {
        if (bundle.containsKey("dateFromFilter")) {
            return bundle.getString("dateFromFilter");
        } else if (bundle.containsKey("dateFromPreviousFragment")) {
            return bundle.getString("dateFromPreviousFragment");
        } else  {
            return getTodayDate();
        }
    }

    public static String getFilterKeywordFromBundle(Bundle bundle) {
        if (bundle.containsKey("keywordFromFilter")) {
            return bundle.getString("keywordFromFilter");
        } else {
            return null;
        }
    }

    public static String getFilterCategoryFromBundle(Bundle bundle) {
        if (bundle.containsKey("categoryFromFilter")) {
            return bundle.getString("categoryFromFilter");
        } else {
            return null;
        }
    }

    public static String getFilterTimeFromBundle(Bundle bundle) {
        if (bundle.containsKey("timeFromFilter")) {
            return bundle.getString("timeFromFilter");
        } else {
            return null;
        }
    }

    public static Performance getSelectedPerformanceFromBundle(Bundle bundle) {
        if (bundle.containsKey("performancesDetailFromPreviousFragment")) {
            return bundle.getParcelable("performancesDetailFromPreviousFragment");
        } else {
            return null;
        }
    }

    public static int getPreviousFragmentIDFromBundle(Bundle bundle) {
        if (bundle.containsKey("previousFragmentID")) {
            return bundle.getInt("previousFragmentID");
        } else {
            return -1;
        }
    }

}

package ambiesoft.start.utility;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ambiesoft.start.dataclass.Artwork;
import ambiesoft.start.dataclass.Performance;

import static ambiesoft.start.utility.DateFormatter.getTodayDate;

/**
 * Created by Bryanyhy on 20/8/2016.
 */
// Class used to do bundle items checking and retrieving afterwards
public class BundleItemChecker {

    // check if there is date in bundle
    public static String getFilterDateFromBundle(Bundle bundle) {
        if (bundle.containsKey("dateFromFilter")) {
            // return date from bundle
            return bundle.getString("dateFromFilter");
        } else  {
            // if no date is found in bundle, return today's date instead
            return getTodayDate();
        }
    }

    // check if there is keyword in bundle
    public static String getFilterKeywordFromBundle(Bundle bundle) {
        if (bundle.containsKey("keywordFromFilter")) {
            // return keyword from bundle
            return bundle.getString("keywordFromFilter");
        } else {
            // return null if no keyword in bundle
            return null;
        }
    }

    // check if there is category in bundle
    public static String getFilterCategoryFromBundle(Bundle bundle) {
        if (bundle.containsKey("categoryFromFilter")) {
            // return category from bundle
            return bundle.getString("categoryFromFilter");
        } else {
            // return null if no category is found
            return null;
        }
    }

    // check if there is time in bundle
    public static String getFilterTimeFromBundle(Bundle bundle) {
        if (bundle.containsKey("timeFromFilter")) {
            // return time from bundle
            return bundle.getString("timeFromFilter");
        } else {
            // return null if no time is found
            return null;
        }
    }

    // check if there is performance object in bundle
    public static Performance getSelectedPerformanceFromBundle(Bundle bundle) {
        if (bundle.containsKey("performancesDetailFromPreviousFragment")) {
            // return the object from bundle
            return bundle.getParcelable("performancesDetailFromPreviousFragment");
        } else {
            // return null if no object in bundle
            return null;
        }
    }

    // check if there is previous fragment ID in bundle
    public static int getPreviousFragmentIDFromBundle(Bundle bundle) {
        if (bundle.containsKey("previousFragmentID")) {
            // return the ID from bundle
            return bundle.getInt("previousFragmentID");
        } else {
            // return -1 if no ID in bundle
            return -1;
        }
    }

    // check if there is artwork object in bundle
    public static Artwork getSelectedArtworkFromBundle(Bundle bundle) {
        if (bundle.containsKey("artworkDetailFromPreviousFragment")) {
            // return the object from bundle
            return bundle.getParcelable("artworkDetailFromPreviousFragment");
        } else {
            // return null if no object in bundle
            return null;
        }
    }

}

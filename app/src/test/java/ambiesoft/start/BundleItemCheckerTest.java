package ambiesoft.start;

import android.os.Bundle;

import org.junit.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ambiesoft.start.model.dataclass.Performance;

import static ambiesoft.start.model.utility.BundleItemChecker.getFilterCategoryFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterDateFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterKeywordFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getFilterTimeFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getPreviousFragmentIDFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedArtworkFromBundle;
import static ambiesoft.start.model.utility.BundleItemChecker.getSelectedPerformanceFromBundle;
import static org.junit.Assert.*;

/**
 * Created by Bryanyhy on 21/8/2016.
 */
public class BundleItemCheckerTest {

    private Bundle emptyBundle;
    private static Bundle fullBundle;
    private Performance performance = new Performance();

    public BundleItemCheckerTest() {
        emptyBundle = Mockito.mock(Bundle.class);
        fullBundle = Mockito.mock(Bundle.class);
        Mockito.when(fullBundle.getString("dateFromFilter")).thenReturn("03-09-2016");
        Mockito.doReturn("03-09-2016").when(fullBundle).getString("dateFromFilter");
    }

    @Test
    public void testIfCorrectReturnValueIfNoItemsInBundle() {
        // get today's date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String expectedDateOutput = sdf.format(calendar.getTime());
        // if no filter date is found in bundle, it should return today's date
        assertEquals(expectedDateOutput, getFilterDateFromBundle(emptyBundle));
        // return null if the bundle has no specific key
        assertEquals(null, getFilterKeywordFromBundle(emptyBundle));
        assertEquals(null, getFilterCategoryFromBundle(emptyBundle));
        assertEquals(null, getFilterTimeFromBundle(emptyBundle));
        assertEquals(null, getSelectedPerformanceFromBundle(emptyBundle));
        assertEquals(null, getSelectedArtworkFromBundle(emptyBundle));
        // return -1 if the bundle has no fragmentID
        assertEquals(-1, getPreviousFragmentIDFromBundle(emptyBundle));
    }
}

package ambiesoft.start.utility;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Bryanyhy on 21/8/2016.
 */
public class SoftKeyboard {
//
//    public static void hideSoftKeyboard(Context context) {
//        InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
//    }

    public static void hideSoftKeyboard(Context context, IBinder windowToken) {
        InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }

}

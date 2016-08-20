package ambiesoft.start.utility;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Bryanyhy on 19/8/2016.
 */
// class for functions on the progress loading dialog
public class ProgressLoadingDialog {

    private static ProgressDialog progress;

    // Show the progress dialog
    public static void showProgressDialog(Context context) {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    // dismiss the dialog
    public static void dismissProgressDialog() {
        progress.dismiss();
    }

}

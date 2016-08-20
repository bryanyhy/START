package ambiesoft.start.utility;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Bryanyhy on 19/8/2016.
 */
public class ProgressLoadingDialog {

    private static ProgressDialog progress;

    public static void showProgressDialog(Context context) {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    public static void dismissProgressDialog() {
        progress.dismiss();
    }

}

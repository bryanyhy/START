package ambiesoft.start.model.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Spanned;

/**
 * Created by Bryanyhy on 13/8/2016.
 */

// class that is for alertBox function
public class AlertBox {

    // show the alertBox
    public static void showAlertBox(String title, String output, Activity activity) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(output);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // show the alertBox with underline
    public static void showAlertBoxWithUnderline(String title, Spanned output, Activity activity) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(output);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

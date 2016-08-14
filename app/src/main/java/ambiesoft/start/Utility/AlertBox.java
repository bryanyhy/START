package ambiesoft.start.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Bryanyhy on 13/8/2016.
 */
public class AlertBox {

    public static void showAlertBox(String output, Activity activity) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
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

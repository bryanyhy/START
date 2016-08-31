package ambiesoft.start.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import ambiesoft.start.R;
import ambiesoft.start.view.fragment.HomeFragment;

/**
 * Created by Zelta on 30/08/16.
 */
public class WelcomeActivity extends Activity {

    private Handler handler;

    private long delay = 5000;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        timer.schedule(task, delay);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            Intent intent = new Intent().setClass(WelcomeActivity.this,
                    MainActivity.class).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    };

}

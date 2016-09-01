package ambiesoft.start.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ambiesoft.start.R;

/**
 * Created by Zelta on 31/08/16.
 */
public class LogOnActivity extends AppCompatActivity {

        public EditText etEmail, etPwd;
        public TextView tvRegister, tvSkip;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_logon);

                etEmail = (EditText) findViewById(R.id.loginEmail);
                tvRegister = (TextView) findViewById(R.id.tvTurnToRegister);
            tvSkip = (TextView) findViewById(R.id.tvLoginSkip);
            tvRegister = (TextView) findViewById(R.id.tvTurnToRegister);

            tvRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LogOnActivity.this, RegisterActivity.class));
                }
            });

            tvSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LogOnActivity.this, MainActivity.class));
                }
            });
        }

}

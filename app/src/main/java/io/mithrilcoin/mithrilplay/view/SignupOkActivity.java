package io.mithrilcoin.mithrilplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;

public class SignupOkActivity extends AppCompatActivity {

    private Button btnMoreInfo, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_ok);

        btnMoreInfo = (Button) findViewById(R.id.btn_more_info);
        btnSkip = (Button) findViewById(R.id.btn_skip);

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMoreInfoScreen();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginScreen();
            }
        });
    }

    private void launchMoreInfoScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SignupOkActivity.this, MoreInfoActivity.class));
        finish();
    }

    private void launchLoginScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SignupOkActivity.this, LoginActivity.class));
        finish();
    }
}

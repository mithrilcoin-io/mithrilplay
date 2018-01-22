package io.mithrilcoin.mithrilplay.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

public class VerifyEmailActivity extends ActivityBase {

    private Button btnCheckVerify, btnResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        btnCheckVerify = (Button) findViewById(R.id.btn_verify_email);
        btnResend = (Button) findViewById(R.id.btn_resend_email);

        btnCheckVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignupOkScreen();
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO HENRY 인증메일을 재발송 합니다~
            }
        });

    }

    private void launchSignupOkScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(VerifyEmailActivity.this, SignupOkActivity.class));
        finish();
    }
}

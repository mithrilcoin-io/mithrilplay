package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.network.RequestCommon;
import io.mithrilcoin.mithrilplay.network.RequestSendEmailAuth;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

public class VerifyEmailActivity extends ActivityBase {

    private Activity mActivity = null;

    private Button btnCheckVerify, btnResend;

    private String mAuthId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mActivity = VerifyEmailActivity.this;

        Intent intent = getIntent();
        mAuthId = intent.getStringExtra(Constant.TAG_AUTH_EMAIL_ID);

        if(!TextUtils.isEmpty(mAuthId)){
            Log.d("mithril", "mAuthId =" + mAuthId );
            sendEmailCall(mAuthId);
        }

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

                sendEmailCall(mAuthId);


            }
        });

    }

    private void sendEmailCall(String mId){

        RequestSendEmailAuth requestSendEmailAuth = new RequestSendEmailAuth(mActivity, mId);
        requestSendEmailAuth.post(new RequestSendEmailAuth.ApiLogoutResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {





            }

            @Override
            public void onFail() {

            }
        });

    }


}

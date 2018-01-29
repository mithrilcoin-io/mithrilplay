package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.CommonApplication;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestCommon;
import io.mithrilcoin.mithrilplay.network.RequestSendEmailAuth;
import io.mithrilcoin.mithrilplay.network.RequestUserInfo;
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

        if(intent != null){
            Log.e("mithril", "[VerifyEmailActivity] launch_uri=[" + intent.getDataString() + "]");
        }

        if(!TextUtils.isEmpty(mAuthId)){
            Log.d("mithril", "mAuthId =" + mAuthId );
            sendEmailCall(mAuthId);
        }

        btnCheckVerify = (Button) findViewById(R.id.btn_verify_email);
        btnResend = (Button) findViewById(R.id.btn_resend_email);

        btnCheckVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mAuthId)){
                    String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);
                    mAuthId = mId;
                }
                getUserinfo(mAuthId);

            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO HENRY 인증메일을 재발송 합니다~
                if(TextUtils.isEmpty(mAuthId)){
                    String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);
                    mAuthId = mId;
                }
                sendEmailCall(mAuthId);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mithril", "VerifyEmailActivity onResume");
    }

    private void sendEmailCall(String mId){

        RequestSendEmailAuth requestSendEmailAuth = new RequestSendEmailAuth(mActivity, mId);
        requestSendEmailAuth.post(new RequestSendEmailAuth.ApiSendEmailAuthtListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

//                Toast.makeText(mActivity, getString(R.string.verify_email_resend), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFail() {

            }
        });

    }

    private void getUserinfo(String mId){

        RequestUserInfo requestUserInfo = new RequestUserInfo(mActivity, mId);
        requestUserInfo.post(new RequestUserInfo.ApiGetUserinfoListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){ // 미인증

                    //TODO 인증 미완료시 문구 확인
                    Toast.makeText(mActivity, getString(R.string.verify_fail), Toast.LENGTH_SHORT).show();

                }else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){  // 정상
//                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_DATE, item.getUserInfo().getAuthdate());

                    launchSignupOkScreen();

                }else{


                }

                if(item.getUserInfo().getAuthdate() != null){
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_DATE, item.getUserInfo().getAuthdate());
                }

            }

            @Override
            public void onFail() {

            }
        });

    }



}

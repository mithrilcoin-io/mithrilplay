package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.HashingUtil;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.network.RequestMemberJoin;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

public class SignupActivity extends ActivityBase implements View.OnClickListener {

    private Activity mActivity = null;

    private TextInputLayout layout_signup_id, layout_signup_pw;
    private EditText et_signup_id, et_signup_pw;
    private Button btnDoSignup;

    private Animation shake;

    // data
    private String mId, mPasswd, mDeviceId, mModel, mBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mActivity = SignupActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.member_join);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        viewInit();

    }

    private void viewInit(){

        layout_signup_id = (TextInputLayout) findViewById(R.id.layout_signup_id);
        layout_signup_pw = (TextInputLayout) findViewById(R.id.layout_signup_pw);

        et_signup_id = (EditText) findViewById(R.id.et_signup_id);
        et_signup_pw = (EditText) findViewById(R.id.et_signup_pw);

        btnDoSignup = (Button) findViewById(R.id.btn_do_signup);
        btnDoSignup.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_do_signup:

                //TODO 비밀번호 정책 확인
                // 비밀번호 규칙 8자~20자, 1개이상 대문자, 1개이상 특수기호(!@#$), 숫자포함

                hideKeyboard();

                layout_signup_id.setErrorEnabled(true);
                layout_signup_pw.setErrorEnabled(true);

                mId = et_signup_id.getText().toString();
                mPasswd = et_signup_pw.getText().toString();

                boolean isIdvaile = false;
                boolean isPdvaile = false;

                if (TextUtils.isEmpty(mId)) {
                    isIdvaile = false;
                    layout_signup_id.setError(getString(R.string.email_null));
                    layout_signup_id.startAnimation(shake);
                }else{
                    if(HashingUtil.checkEmail(mId)){
                        isIdvaile = true;
                        layout_signup_id.setErrorEnabled(false);
                    }else{
                        isIdvaile = false;
                        layout_signup_id.setError(getString(R.string.email_not_valid));
                        layout_signup_id.startAnimation(shake);
                    }
                }

                if (TextUtils.isEmpty(mPasswd)) {
                    isPdvaile = false;
                    layout_signup_pw.setError(getString(R.string.password_null));
                    layout_signup_pw.startAnimation(shake);
                }else{
                    if(HashingUtil.checkPassword(mPasswd)){
                        isPdvaile = true;
                        layout_signup_pw.setErrorEnabled(false);
                    }else{
                        isPdvaile = false;
                        layout_signup_pw.setError(getString(R.string.password_check));
                        layout_signup_pw.startAnimation(shake);
                    }
                }

                if(isIdvaile && isPdvaile){
                    memberJoinCall();
                }

                break;
        }

    }


    private void memberJoinCall(){

        mId = et_signup_id.getText().toString();
        mPasswd = et_signup_pw.getText().toString();
        mDeviceId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_ANDROD_ID);
        mModel = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_MODEL);
        mBrand = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_BRAND);

        MemberJoinRequest memberJoinRequest = new MemberJoinRequest(mId, mPasswd, mDeviceId, mModel, mBrand);

        RequestMemberJoin requestMemberJoin = new RequestMemberJoin(mActivity,memberJoinRequest);
        requestMemberJoin.post(new RequestMemberJoin.ApiMemberJoinResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){ // 미인증

                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    if(!TextUtils.isEmpty(item.getUserInfo().getId())){
                        MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                        MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, false);

                        // 이메일 인증으로 이동
                        launchVerifyScreen(item.getUserInfo().getId());
                    }

                }else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){  // 정상
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();

                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);

                    // 회원가입 완료 페이지로 이동
                    launchSignupOkScreen();

                }else{


                }

                // 이메일 임시 저장
                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_EMAIL, mId);

            }

            @Override
            public void onFail() {

            }
        });

    }


}

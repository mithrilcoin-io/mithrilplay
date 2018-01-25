package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestLogin;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

public class LoginActivity extends ActivityBase implements View.OnClickListener {

    private Activity mActivity = null;

    private TextInputLayout layout_user_id, layout_pw_id;
    private EditText et_user_id, et_user_pw;
    private Button btnSignin, btnSignup;

    private Animation shake;

    // data
    private String mId, mPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 18) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mActivity = LoginActivity.this;

        // making notification bar transparent
        changeStatusBarColor();

        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        viewInit();
    }

    private void viewInit(){

        layout_user_id = (TextInputLayout) findViewById(R.id.til_user_id);
        layout_pw_id = (TextInputLayout) findViewById(R.id.til_pw_id);
        et_user_id = (EditText) findViewById(R.id.et_user_id);
        et_user_pw = (EditText) findViewById(R.id.et_user_pw);

        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnSignup = (Button) findViewById(R.id.btn_signup);

        btnSignin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_signin:

                hideKeyboard();

                layout_user_id.setErrorEnabled(true);
                layout_pw_id.setErrorEnabled(true);

                mId = et_user_id.getText().toString();
                mPasswd = et_user_pw.getText().toString();

                if (TextUtils.isEmpty(mId) && TextUtils.isEmpty(mPasswd)) {
                    layout_user_id.setError(getString(R.string.email_null));
                    layout_user_id.startAnimation(shake);
                    layout_pw_id.setError(getString(R.string.password_null));
                    layout_pw_id.startAnimation(shake);
                } else if (TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mPasswd)) {
                    layout_user_id.setError(getString(R.string.email_null));
                    layout_user_id.startAnimation(shake);
                    layout_pw_id.setErrorEnabled(false);
                } else if (!TextUtils.isEmpty(mId) && TextUtils.isEmpty(mPasswd)) {
                    layout_user_id.setErrorEnabled(false);
                    layout_pw_id.setError(getString(R.string.password_null));
                    layout_pw_id.startAnimation(shake);
                }else{
                    layout_user_id.setErrorEnabled(false);
                    layout_pw_id.setErrorEnabled(false);
                    loginCall();
                }

                break;

            case R.id.btn_signup:

                launchSignupScreen();

                break;


        }

    }

    private void loginCall(){

        LoginRequest loginRequest = new LoginRequest(mId, mPasswd);

        RequestLogin requestLogin = new RequestLogin(mActivity,loginRequest);
        requestLogin.post(new RequestLogin.ApiLoginResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){ // 미인증

                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, false);

                    // 이메일 인증으로 이동
                    launchVerifyScreen(item.getUserInfo().getId());

                }else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){  // 이메일 인증까지 완료
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);

                    // 메인으로 이동
                    launchHomeScreen();

                }else if(item.getUserInfo().getState().equals(Constant.USER_AUTH_PLUS_PROFILE)){  // 추가정보 입력완료
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);

                    // 메인으로 이동
                    launchHomeScreen();
                }

                // 이메일 임시 저장
                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_EMAIL, mId);


            }

            @Override
            public void onFail() {

            }
        });

    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}

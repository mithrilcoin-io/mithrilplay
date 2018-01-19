package io.mithrilcoin.mithrilplay.view;

import android.app.Activity;
import android.content.Intent;
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
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestLogin;
import io.mithrilcoin.mithrilplay.network.RequestMemberJoin;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinResponse;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

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
            public void onSuccess(MemberJoinResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody(), Toast.LENGTH_SHORT).show();
                    return;
                }


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

    private void launchWelcomeScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        finish();
    }

    private void launchSignupScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//        finish();
    }
}

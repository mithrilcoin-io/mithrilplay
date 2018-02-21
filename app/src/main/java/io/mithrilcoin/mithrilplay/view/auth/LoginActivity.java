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
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestLogin;
import io.mithrilcoin.mithrilplay.network.eosobj.DeviceInfo;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 * login
 */
public class LoginActivity extends ActivityBase {

    private Activity mActivity = null;

    private TextInputLayout layout_user_id, layout_pw_id;
    private EditText et_user_id, et_user_pw;
    private String mId, mPasswd;

    private Animation shake;

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

        findViewById(R.id.btn_signin).setOnClickListener(v -> {

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

        });

        findViewById(R.id.btn_signup).setOnClickListener(v ->

                launchSignupScreen()

        );

    }

    private void loginCall(){

        String mDeviceId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_ANDROD_ID);
        String mPushId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_PUSH_ID);
        LoginRequest loginRequest = new LoginRequest(mId, mPasswd, mDeviceId, mPushId, Build.VERSION.RELEASE);

        RequestLogin requestLogin = new RequestLogin(mActivity,loginRequest);
        requestLogin.post(new RequestLogin.ApiLoginResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_EMAIL, mId);
                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_DATE, item.getUserInfo().getAuthdate());

                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setEmail(mId);
                deviceInfo.setModel(MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_MODEL));
                deviceInfo.setBrand(MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_BRAND));
                deviceInfo.setOsversion(MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_OS_VERSION));
                // TODO: EOS SmartContract _ (3.deviceinfo) When you sign up
                setEosDeviceInfo(deviceInfo, item.getUserInfo().getState());



            }

            @Override
            public void onFail() {

            }
        });

    }

    private void setEosDeviceInfo(DeviceInfo deviceInfo, String userStatus){
        // TODO: Function "loginAfterMove(userStatus)" call when smartcontract completes






        loginAfterMove(userStatus);
    }

    private void loginAfterMove(String userStatus){
        if(userStatus.equals(Constant.USER_STATUS_NOT_AUTH)){
            MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, false);
            launchVerifyScreen( MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID));

        }else if(userStatus.equals(Constant.USER_STATUS_AUTH_ON)){
            MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
            launchHomeScreen();

        }else if(userStatus.equals(Constant.USER_AUTH_PLUS_PROFILE)){
            MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
            launchHomeScreen();
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}

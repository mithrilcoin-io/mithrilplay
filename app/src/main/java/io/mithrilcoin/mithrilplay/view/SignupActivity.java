package io.mithrilcoin.mithrilplay.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestMemberJoin;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinResponse;

public class SignupActivity extends BaseActivity implements View.OnClickListener {

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

    private void launchVerifyScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SignupActivity.this, VerifyEmailActivity.class));
        finish();
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

                hideKeyboard();

                layout_signup_id.setErrorEnabled(true);
                layout_signup_pw.setErrorEnabled(true);

                mId = et_signup_id.getText().toString();
                mPasswd = et_signup_pw.getText().toString();

                if (TextUtils.isEmpty(mId) && TextUtils.isEmpty(mPasswd)) {
                    layout_signup_id.setError(getString(R.string.email_null));
                    layout_signup_id.startAnimation(shake);
                    layout_signup_pw.setError(getString(R.string.password_null));
                    layout_signup_pw.startAnimation(shake);
                } else if (TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mPasswd)) {
                    layout_signup_id.setError(getString(R.string.email_null));
                    layout_signup_id.startAnimation(shake);
                    layout_signup_pw.setErrorEnabled(false);
                } else if (!TextUtils.isEmpty(mId) && TextUtils.isEmpty(mPasswd)) {
                    layout_signup_id.setErrorEnabled(false);
                    layout_signup_pw.setError(getString(R.string.password_null));
                    layout_signup_pw.startAnimation(shake);
                }else{
                    layout_signup_id.setErrorEnabled(false);
                    layout_signup_pw.setErrorEnabled(false);
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
            public void onSuccess(MemberJoinResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(item.getUserInfo().getState().equals("M001001")){ // 미인증

                    Toast.makeText(mActivity, item.getBody(), Toast.LENGTH_SHORT).show();

                    // 이메일 인증으로 이동
                    launchVerifyScreen();


                }else if(item.getUserInfo().getState().equals("M001002")){  // 정상
                    Toast.makeText(mActivity, item.getBody(), Toast.LENGTH_SHORT).show();

                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());

                }else{


                }

            }

            @Override
            public void onFail() {

            }
        });

    }


}

package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.CommonApplication;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.HashingUtil;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.db.DBdataAccess;
import io.mithrilcoin.mithrilplay.db.entity.Device;
import io.mithrilcoin.mithrilplay.db.entity.UserData;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.network.RequestMemberJoin;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.network.vo.Membersocial;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 *  Sign Up
 */
public class SignupActivity extends ActivityBase implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Activity mActivity = null;

    private TextInputLayout layout_signup_id, layout_signup_pw;
    private EditText et_signup_id, et_signup_pw;
    private Button btnDoSignup;

    private Animation shake;

    private MemberJoinRequest memberJoinRequest;

    private String mId, mPasswd, mDeviceId, mModel, mBrand, mPushId, mOsVersion, mGoogleTokenId;

    // google login
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    GoogleApiClient mGoogleApiClient;

    private boolean isGoogleLogin = false;

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "sign in");

//                    android.util.Log.d(TAG, "user.getEmail() =" + user.getEmail());
//                    android.util.Log.d(TAG, "user.getDisplayName() =" + user.getDisplayName());
//                    android.util.Log.d(TAG, "user.getPhoneNumber() =" + user.getPhoneNumber());
//                    android.util.Log.d(TAG, "user.getProviderId() =" + user.getProviderId());
//                    android.util.Log.d(TAG, "user.getMetadata() =" + user.getMetadata());
//                    android.util.Log.d(TAG, "user.getUid() =" + user.getUid());

                }else{
                    Log.d(TAG, "sign out");
                }

            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        /*
         *  Google Login
         */
        findViewById(R.id.sign_in_google_button).setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, Constant.REQUEST_SIGN_UP_GOOGLE_SIGN_IN);
        });

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
    protected void onStop() {
        super.onStop();
        if ( mFirebaseAuthListener != null )
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);

        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == Constant.REQUEST_SIGN_UP_GOOGLE_SIGN_IN ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if ( result.isSuccess() ) {
                String token = result.getSignInAccount().getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
                mFirebaseAuth.signInWithCredential(credential);

                String getId = result.getSignInAccount().getId();
                String getDisplayName = result.getSignInAccount().getDisplayName();
                String getEmail = result.getSignInAccount().getEmail();
                String getFamilyName = result.getSignInAccount().getFamilyName();
                String getGivenName = result.getSignInAccount().getGivenName();
                String getServerAuthCode = result.getSignInAccount().getServerAuthCode();
                String getPhotoUrl = result.getSignInAccount().getPhotoUrl().toString();
                String getAccount = result.getSignInAccount().getAccount().toString();

                Log.d(TAG, "token =" + token);
                Log.d(TAG, "getId =" + getId);
                Log.d(TAG, "getDisplayName =" + getDisplayName);
                Log.d(TAG, "getEmail =" + getEmail);
                Log.d(TAG, "getFamilyName =" + getFamilyName);
                Log.d(TAG, "getGivenName =" + getGivenName);
                Log.d(TAG, "getServerAuthCode =" + getServerAuthCode);
                Log.d(TAG, "getPhotoUrl =" + getPhotoUrl);
                Log.d(TAG, "getAccount =" + getAccount);

                // google login succeed
                isGoogleLogin = true;
                mId = getEmail;
                mGoogleTokenId = getId;
                layout_signup_id.setVisibility(View.INVISIBLE);

            }
            else {
                Log.d(TAG, "Google Login Failed." + result.getStatus());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_do_signup:

                // Id rules       : email type
                // Password rules : 8 to 20 characters, one or more uppercase letters, one or more special symbols (! @ # $), Including numbers

                hideKeyboard();

                layout_signup_id.setErrorEnabled(true);
                layout_signup_pw.setErrorEnabled(true);

                if(!isGoogleLogin){
                    mId = et_signup_id.getText().toString();
                }
                mPasswd = et_signup_pw.getText().toString();

                boolean isIdvaile = false;
                boolean isPdvaile = false;

                if(isGoogleLogin){
                    isIdvaile = true;
                }else{
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

        Membersocial membersocial = null;

        if(!isGoogleLogin){
            mId = et_signup_id.getText().toString();
        }else{
            membersocial = new Membersocial();
            membersocial.setSnscode(Constant.LOGIN_GOOGLE_TYPE);
            membersocial.setTokenid(mGoogleTokenId);
        }
        mPasswd = et_signup_pw.getText().toString();
        mDeviceId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_ANDROD_ID);
        mModel = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_MODEL);
        mBrand = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_BRAND);
        mPushId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_PUSH_ID);
        mOsVersion = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_OS_VERSION);

        memberJoinRequest = new MemberJoinRequest(mId, mPasswd, mDeviceId, mModel, mBrand, mPushId, mOsVersion);
        if(isGoogleLogin){
            memberJoinRequest.setMembersocial(membersocial);
        }

        RequestMemberJoin requestMemberJoin = new RequestMemberJoin(mActivity,memberJoinRequest);
        requestMemberJoin.post(new RequestMemberJoin.ApiMemberJoinResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){

                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    if(!TextUtils.isEmpty(item.getUserInfo().getId())){
                        MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                        MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, false);
                        launchVerifyScreen(item.getUserInfo().getId());
                    }

                }else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){  // 정상

                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
                    launchSignupOkScreen();

                }else{


                }

                // email save
                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_EMAIL, mId);

                // google account save
                if(isGoogleLogin){
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_GOOGLE_EMAIL, mId);
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_GOOGLE_ID, mGoogleTokenId);
                }

                // DB access
                if(item.getUserInfo() != null){
                    DBdataAccess.memberDataDBaccess(item.getUserInfo(), mId);
                }

            }

            @Override
            public void onFail() {

            }
        });

    }


}

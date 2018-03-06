package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.db.DBdataAccess;
import io.mithrilcoin.mithrilplay.network.RequestLogin;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.network.vo.Membersocial;
import io.mithrilcoin.mithrilplay.view.ActivityBase;


/**
 * login
 */
public class LoginActivity extends ActivityBase implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Activity mActivity = null;

    private TextInputLayout layout_user_id, layout_pw_id;
    private EditText et_user_id, et_user_pw;
    private Button btnSignin, btnSignup;
    private String mId, mPasswd, mGoogleTokenId;
    SignInButton mSigninGoogleButton;

    private Animation shake;

    // google login
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    GoogleApiClient mGoogleApiClient;

    private boolean isGoogleLogin = false;

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "sign in");

//                    Log.d(TAG, "user.getEmail() =" + user.getEmail());
//                    Log.d(TAG, "user.getDisplayName() =" + user.getDisplayName());
//                    Log.d(TAG, "user.getPhoneNumber() =" + user.getPhoneNumber());
//                    Log.d(TAG, "user.getProviderId() =" + user.getProviderId());
//                    Log.d(TAG, "user.getMetadata() =" + user.getMetadata());
//                    Log.d(TAG, "user.getUid() =" + user.getUid());

                }else{
                    Log.d(TAG, "sign out");
                }

            }
        };

        /*
         *  Google Login
         */
        mSigninGoogleButton = (SignInButton) findViewById(R.id.sign_in_google_button);
        mSigninGoogleButton.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, Constant.REQUEST_LOGIN_GOOGLE_SIGN_IN);
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

        Membersocial membersocial = null;

        if(!isGoogleLogin){
            mId = et_user_id.getText().toString();
        }else{
            membersocial = new Membersocial();
            membersocial.setSnscode(Constant.LOGIN_GOOGLE_TYPE);
            membersocial.setTokenid(mGoogleTokenId);
            mPasswd = "";
        }

        String mDeviceId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_ANDROD_ID);
        String mPushId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_PUSH_ID);
        String mModel = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_MODEL);
        String mBrand = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_BRAND);
        String mOsVersion = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_OS_VERSION);

        LoginRequest loginRequest = new LoginRequest(mId, mPasswd, mDeviceId, mModel, mBrand, mPushId, mOsVersion);
        if(isGoogleLogin){
            loginRequest.setMembersocial(membersocial);
        }

        RequestLogin requestLogin = new RequestLogin(mActivity,loginRequest);
        requestLogin.post(new RequestLogin.ApiLoginResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                isGoogleLogin = false;

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_EMAIL, mId);

                // google account save
                if(isGoogleLogin){
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_GOOGLE_EMAIL, mId);
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_GOOGLE_ID, mGoogleTokenId);
                }

                Log.d("mithril", "item.getUserInfo().getAuthdate() = " + item.getUserInfo().getAuthdate());

                if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, false);
                    launchVerifyScreen(item.getUserInfo().getId());

                }else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_DATE, item.getUserInfo().getAuthdate());
                    launchHomeScreen();

                }else if(item.getUserInfo().getState().equals(Constant.USER_AUTH_PLUS_PROFILE)){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_ID, item.getUserInfo().getId());
                    MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
                    MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_DATE, item.getUserInfo().getAuthdate());
                    launchHomeScreen();
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

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
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

        if ( requestCode == Constant.REQUEST_LOGIN_GOOGLE_SIGN_IN ) {
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

                loginCall();

            }
            else {
                Log.d(TAG, "Google Login Failed." + result.getStatus());
            }
        }
    }

}

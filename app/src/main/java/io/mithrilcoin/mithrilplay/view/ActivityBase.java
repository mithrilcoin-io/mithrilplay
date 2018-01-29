package io.mithrilcoin.mithrilplay.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog.CommonDialogListener;
import io.mithrilcoin.mithrilplay.view.auth.LoginActivity;
import io.mithrilcoin.mithrilplay.view.auth.SignupActivity;
import io.mithrilcoin.mithrilplay.view.auth.SignupOkActivity;
import io.mithrilcoin.mithrilplay.view.auth.VerifyEmailActivity;
import io.mithrilcoin.mithrilplay.view.auth.WelcomeActivity;


public class ActivityBase extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName() + ": ";

    private Activity mActivity = null;
    public static ActivityBase instance = null;

    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    // fcm 관련
//    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mActivity = ActivityBase.this;
        instance = ActivityBase.this;

        activityList.add(this);
    }

    public boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(mobile != null){
            if (mobile.isConnected() || wifi.isConnected()) {
                return true;
            }
        }else{
            if (wifi.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public void kill() {

        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
//        finish();
//        ActivityConstant.applicationKill(this);
        System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 회원가입후 id가 있을 경우
    public boolean isLogin() {
        String mAuthId = MithrilPreferences.getString(this, MithrilPreferences.TAG_AUTH_ID);
        return !TextUtils.isEmpty(mAuthId);
    }

    // email 인증 여부
    public boolean isEmailAuth() {
        boolean mEmailAuth = MithrilPreferences.getBoolean(this, MithrilPreferences.TAG_EMAIL_AUTH);
        return mEmailAuth;
    }

    public void logout(){
        MithrilPreferences.putString(this, MithrilPreferences.TAG_AUTH_ID, "");
        MithrilPreferences.putBoolean(this, MithrilPreferences.TAG_EMAIL_AUTH, false);
        MithrilPreferences.putString(this, MithrilPreferences.TAG_EMAIL, "");
        MithrilPreferences.putString(this, MithrilPreferences.TAG_AUTH_DATE, "");
    }

    public void logoutInlogin(){
        Log.e("mithril","다른 기기에서 로그인했습니다." );
        logout();
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
        launchLoginScreen();
    }

    public String getAppVersion() {
        String appVersion = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersion;
    }

    public String getAndroidID() {

        String androidId = "" + android.provider.Settings.Secure.getString(getBaseContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Log.d("mithril","androidId =" + androidId);

        return androidId;
    }

    private String getLauncherClassName(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(getPackageName());

        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent,0);
        if(resolveInfoList != null && resolveInfoList.size() > 0){
            return resolveInfoList.get(0).activityInfo.name;
        }
        return "";
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //********** 화면 이동 ********//

    // 이메일 인증 페이지로 이동
    public void launchVerifyScreen(String authId) {
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constant.TAG_AUTH_EMAIL_ID, authId);
        startActivity(intent);
    }

    // 환영페이지로 이동
    public void launchWelcomeScreen() {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    // 회원가입으로 이동
    public void launchSignupScreen() {
        startActivity(new Intent(this, SignupActivity.class));
//        finish();
    }

    // 회원가입완료로 이동
    public void launchSignupOkScreen() {
        Intent intent = new Intent(this, SignupOkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // 홈으로 이동
    public void launchHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // 셋팅으로 이동
    public void launchSettingScreen() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    // 로그인 화면으로 이동
    public void launchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


    //********** 공통 dialog ********//
    public void showDialogOneButton(String title, String message, String positiveButtonName, CommonDialogListener listener) {
        CommonDialog dialog = new CommonDialog(this, listener);
        dialog.showDialogOneButton(title, message, positiveButtonName);
    }

    public void showWebViewDialogTwoButton(String title, String message, String negativeButtonName, String positiveButtonName, CommonDialogListener listener) {
        CommonDialog dialog = new CommonDialog(this, listener);
        dialog.showDialogTwoButton(title, message, negativeButtonName, positiveButtonName);
    }


}

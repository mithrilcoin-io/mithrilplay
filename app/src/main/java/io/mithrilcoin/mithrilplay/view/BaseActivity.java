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

import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.dialog.PermissionDialog;
import io.mithrilcoin.mithrilplay.dialog.PermissionDialog.PermissionConfirmListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName() + ": ";
    protected final String CHAR_SET = "UTF-8";

    private Activity mActivity;

    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    // fcm 관련
//    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mActivity = BaseActivity.this;

        activityList.add(this);
    }

    // 앱접근 권한 안내
    public void showPermissionDialog(PermissionConfirmListener listener) {
        PermissionDialog permissionDialog = new PermissionDialog(this, listener);
        permissionDialog.showDialogOneButton();
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


    public boolean isLogin() {
        String mAuthId = MithrilPreferences.getString(this, MithrilPreferences.TAG_AUTH_ID);
        return !TextUtils.isEmpty(mAuthId);
    }

    public void moveWebActivity(String url) {
//        Intent intent = new Intent(this, WebActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra(Constant.TAG_URL, url);
//        startActivityForResult(intent, Constant.REQUEST_CODE_INTRO_LOGIN_ACTIVITY);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    public String getTodayTime(){

        String time = "";
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시", Locale.KOREA);
        Date currentTime = new Date();
        time = mSimpleDateFormat.format ( currentTime );

        return time;
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



}

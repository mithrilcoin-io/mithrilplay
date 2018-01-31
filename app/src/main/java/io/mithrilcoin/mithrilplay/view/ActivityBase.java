package io.mithrilcoin.mithrilplay.view;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

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

/**
 *  BaseActivity
 */
public class ActivityBase extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName() + ": ";

    private Activity mActivity = null;
    public static ActivityBase instance = null;
    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = ActivityBase.this;
        instance = ActivityBase.this;

        activityList.add(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Login Check
    public boolean isLogin() {
        String mAuthId = MithrilPreferences.getString(this, MithrilPreferences.TAG_AUTH_ID);
        return !TextUtils.isEmpty(mAuthId);
    }

    // email authentication check
    public boolean isEmailAuth() {
        boolean mEmailAuth = MithrilPreferences.getBoolean(this, MithrilPreferences.TAG_EMAIL_AUTH);
        return mEmailAuth;
    }

    // logout
    public void logout(){
        MithrilPreferences.putString(this, MithrilPreferences.TAG_AUTH_ID, "");
        MithrilPreferences.putBoolean(this, MithrilPreferences.TAG_EMAIL_AUTH, false);
        MithrilPreferences.putString(this, MithrilPreferences.TAG_EMAIL, "");
        MithrilPreferences.putString(this, MithrilPreferences.TAG_AUTH_DATE, "");
    }

    // move login
    public void logoutInlogin(){
        logout();
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
        launchLoginScreen();
    }

    public String getAndroidID() {

        String androidId = "" + android.provider.Settings.Secure.getString(getBaseContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Log.d(TAG,"androidId =" + androidId);

        return androidId;
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // move Email auth
    public void launchVerifyScreen(String authId) {
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constant.TAG_AUTH_EMAIL_ID, authId);
        startActivity(intent);
    }

    // move welcome page
    public void launchWelcomeScreen() {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    // move sign up
    public void launchSignupScreen() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    // move sign up complete
    public void launchSignupOkScreen() {
        Intent intent = new Intent(this, SignupOkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // move home
    public void launchHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // move setting
    public void launchSettingScreen() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    // move login
    public void launchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // Common Dialog one button
    public void showDialogOneButton(String title, String message, String positiveButtonName, CommonDialogListener listener) {
        CommonDialog dialog = new CommonDialog(this, listener);
        dialog.showDialogOneButton(title, message, positiveButtonName);
    }

    // Common Dialog two button
    public void showWebViewDialogTwoButton(String title, String message, String negativeButtonName, String positiveButtonName, CommonDialogListener listener) {
        CommonDialog dialog = new CommonDialog(this, listener);
        dialog.showDialogTwoButton(title, message, negativeButtonName, positiveButtonName);
    }

    // Usage Stats Permission check
    public boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

}

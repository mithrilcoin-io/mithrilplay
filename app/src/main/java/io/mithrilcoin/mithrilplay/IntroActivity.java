package io.mithrilcoin.mithrilplay;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import java.io.File;

import io.mithrilcoin.mithrilplay.common.CommonApplication;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestUserInfo;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import io.mithrilcoin.mithrilplay.view.auth.DataAccessInfoPermissionActivity;
import io.mithrilcoin.mithrilplay.view.auth.LoginActivity;
import io.mithrilcoin.mithrilplay.view.auth.VerifyEmailActivity;

/**
 *  Intro page
 */
public class IntroActivity extends ActivityBase {

	private Activity mActivity = null;

	public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
	public static final String ROOTING_PATH_1 = "/system/bin/su";
	public static final String ROOTING_PATH_2 = "/system/xbin/su";
	public static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
	public static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";

	public String[] RootFilesPath = new String[]{
			ROOT_PATH + ROOTING_PATH_1 ,
			ROOT_PATH + ROOTING_PATH_2 ,
			ROOT_PATH + ROOTING_PATH_3 ,
			ROOT_PATH + ROOTING_PATH_4
	};

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@SuppressLint("WrongConstant")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		mActivity = IntroActivity.this;

		// init setting
		MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_ANDROD_ID, getAndroidID());
		MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_MODEL, Build.MODEL);
		MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_BRAND, Build.BRAND);

		// routing check
		boolean isRootingFlag = false;
		try {
			Runtime.getRuntime().exec("su");
			isRootingFlag = true;
		} catch ( Exception e) {
			isRootingFlag = false;
		}
		if(!isRootingFlag){
			isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
		}
		Log.e(TAG, "isRooting = " + isRootingFlag);

		if(isRootingFlag){

			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage(getString(R.string.is_rooting_app_end));
			builder.setPositiveButton(getString(R.string.confirm),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.show();

		}else {

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {

					if (hasPermission()){
						checkRoute();
					}else{
						Intent intent = new Intent(mActivity, DataAccessInfoPermissionActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivityForResult(intent, Constant.REQUEST_CODE_INTRO_USE_INFO);
					}

				}
			}, 1000);

		}

	}

	/**
	 * Routing files Creates files with suspicious paths.
	 */
	private File[] createFiles(String[] sfiles){
		File[] rootingFiles = new File[sfiles.length];
		for(int i=0 ; i < sfiles.length; i++){
			rootingFiles[i] = new File(sfiles[i]);
		}
		return rootingFiles;
	}

	/**
	 * Check whether it is a routing file.
	 */
	private boolean checkRootingFiles(File... file){
		boolean result = false;
		for(File f : file){
			if(f != null && f.exists() && f.isFile()){
				result = true;
				break;
			}else{
				result = false;
			}
		}
		return result;
	}

	private void checkRoute(){

		if(isLogin()){
			if(isEmailAuth()){
				launchHomeScreen();
				String ad = MithrilPreferences.getString(CommonApplication.getApplication(), MithrilPreferences.TAG_AUTH_DATE);
//				Log.e(TAG, "ad = " + ad );
			}else{
				String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);
				getUserinfo(mId);
			}
		}else{
			Intent intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}

	}

	private void getUserinfo(String mId){

		RequestUserInfo requestUserInfo = new RequestUserInfo(mActivity, mId);
		requestUserInfo.post(new RequestUserInfo.ApiGetUserinfoListener() {
			@Override
			public void onSuccess(MemberResponse item) {

				if(item == null || item.getUserInfo() == null){
					Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
					return;
				}

				if(!item.getBody().getCode().equals(Constant.SUCCESS)){
					return;
				}

				if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){
					Intent intent = new Intent(mActivity, VerifyEmailActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivityForResult(intent, Constant.REQUEST_INTRO_EMAILAUTH);
				}else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){
					MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
					launchHomeScreen();
				}else if(item.getUserInfo().getState().equals(Constant.USER_AUTH_PLUS_PROFILE)){
					MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
					launchHomeScreen();
				}else{


				}

				if(item.getUserInfo().getAuthdate() != null){
					MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_AUTH_DATE, item.getUserInfo().getAuthdate());
				}

			}

			@Override
			public void onFail() {

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK) {
            finish();
			return;
		}

		if(requestCode == Constant.REQUEST_CODE_INTRO_USE_INFO){
			checkRoute();
			return;
		}else if(requestCode == Constant.REQUEST_INTRO_EMAILAUTH){

			return;
		}

	}

}

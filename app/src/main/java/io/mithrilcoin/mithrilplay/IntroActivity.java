package io.mithrilcoin.mithrilplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestUserInfo;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import io.mithrilcoin.mithrilplay.view.auth.LoginActivity;
import io.mithrilcoin.mithrilplay.view.auth.PermissionActivity;
import io.mithrilcoin.mithrilplay.view.auth.VerifyEmailActivity;

public class IntroActivity extends ActivityBase {

	private Activity mActivity = null;

    private ImageView intro = null;

	// 루팅 체크 관련
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

	@SuppressLint("WrongConstant")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		mActivity = IntroActivity.this;

		intro = (ImageView) findViewById(R.id.intro);

		// 초기 기본 정보 셋팅
		MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_ANDROD_ID, getAndroidID());
		MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_MODEL, Build.MODEL);
		MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_BRAND, Build.BRAND);

		// 루팅 체크
		boolean isRootingFlag = false;
		try {
			Runtime.getRuntime().exec("su");
			isRootingFlag = true;
		} catch ( Exception e) {
			// Exception 나면 루팅 false;
			isRootingFlag = false;
		}
		if(!isRootingFlag){
			isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
		}
		Log.e("mithril", "isRootingFlag = " + isRootingFlag);

		if(isRootingFlag){

			// 루팅 안내 팝업
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

			// 안드로이드 퍼미션 권한 안내 한번만 호출
			boolean isPLoaded = MithrilPreferences.getBoolean(mActivity, MithrilPreferences.TAG_PERMISSION);
			if(!isPLoaded){

				Intent intent = new Intent(this, PermissionActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent, Constant.REQUEST_CODE_INTRO_PERMISSION);

			}else{
				if(isLogin()){
					if(isEmailAuth()){
						launchHomeScreen();
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

		}

/*
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"io.mithrilcoin.mithrilplay",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("jhs","hash="+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
*/
	}

	public void checkIntent(){

		if (getIntent().getData() != null) {
//            if(getIntent().getData().toString().startsWith("mithril")) {
//                checkWebIntent(getIntent().getData().toString());
//            }else{
//                networkCheckIntro();
//            }
//			networkCheckIntro();
		}else{
//            networkCheckIntro();
		}
	}

	/**
	 * 루팅파일 의심 Path를 가진 파일들을 생성 한다.
	 */
	private File[] createFiles(String[] sfiles){
		File[] rootingFiles = new File[sfiles.length];
		for(int i=0 ; i < sfiles.length; i++){
			rootingFiles[i] = new File(sfiles[i]);
		}
		return rootingFiles;
	}

	/**
	 * 루팅파일 여부를 확인 한다.
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

	private void getUserinfo(String mId){

		RequestUserInfo requestUserInfo = new RequestUserInfo(mActivity, mId);
		requestUserInfo.post(new RequestUserInfo.ApiGetUserinfoListener() {
			@Override
			public void onSuccess(MemberResponse item) {

				if(item == null || item.getUserInfo() == null){
					Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
					return;
				}

				if(!item.getBody().getCode().equals("SUCCESS")){
					return;
				}

				if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){ // 미인증
					Log.d("mithril", "이메일 미인증");
					Intent intent = new Intent(mActivity, VerifyEmailActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivityForResult(intent, Constant.REQUEST_INTRO_EMAILAUTH);

				}else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){  // 정상
					Log.d("mithril", "이메인 인증까지 완료");
					MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
					launchHomeScreen();
				}else if(item.getUserInfo().getState().equals(Constant.USER_AUTH_PLUS_PROFILE)){  // 추가정보 입력완료
					Log.d("mithril", "추가정보 입력완료");
					MithrilPreferences.putBoolean(mActivity, MithrilPreferences.TAG_EMAIL_AUTH, true);
					launchHomeScreen();
				}else{


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

        Log.d("mithril", ": intro onActivityResult");

		if (resultCode != RESULT_OK) {
            finish();
			return;
		}

		if(requestCode == Constant.REQUEST_CODE_INTRO_PERMISSION){
			Log.d("mithril", ": REQUEST_CODE_INTRO_PERMISSION");

			if(isLogin()){
				launchHomeScreen();
			}else{
				Intent intent = new Intent(this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
			}

			return;
		}else if(requestCode == Constant.REQUEST_INTRO_EMAILAUTH){
			Log.d("mithril", ": REQUEST_INTRO_EMAILAUTH");


			return;
		}

	}

}

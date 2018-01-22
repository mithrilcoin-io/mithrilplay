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
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import io.mithrilcoin.mithrilplay.view.auth.LoginActivity;
import io.mithrilcoin.mithrilplay.view.auth.PermissionActivity;

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

	private PackageManager pm;
	private List<ApplicationInfo> mAppList = null;

	@SuppressLint("WrongConstant")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		mActivity = IntroActivity.this;

		intro = (ImageView) findViewById(R.id.intro);

		// 설치된 어플리케이션 목록 가져오기
		pm = IntroActivity.this.getPackageManager();
		mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);
		Log.d("mithril", "mAppList.size() = " + mAppList.size());
		for(ApplicationInfo app : mAppList){
//			Log.d("mithril", "packageName = " + app.packageName);
		}

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
				startActivity(intent);
				finish();

			}else{

				if(isLogin()){
					launchHomeScreen();
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
//            if(getIntent().getData().toString().startsWith("ssocioweshare")) {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        Log.d("mithril", ": ************ intro onActivityResult");

		if (resultCode != RESULT_OK) {
            finish();
			return;
		}

	}

}

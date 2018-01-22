package io.mithrilcoin.mithrilplay.common;

import android.content.Context;

public class MithrilPreferences {

	public static final String PREF_KEY			= "key";
	public static final String TAG_PREFERENCES	= "ProjectMithrilPlay";

	public static final String TAG_PERMISSION = "permission"; 		// boolean, 안드로이드 권한 정보 팝업 앱실행시 한번
	public static final String TAG_INTRO_SLIDE = "intro_slide"; 	// boolean, 환영페이지, 앱실행시 회원가입이나 로그인이후 홈화면 진입시에 한번
	public static final String TAG_AUTH_ID = "auth_id"; 		// string, 회원가입이나 로그인시 서버에서 내려주는 ID (유무로 로그인 체크)
	public static final String TAG_EMAIL_AUTH = "email_auth"; 	// boolean, 이메일 인증 여부

	public static final String TAG_ANDROD_ID = "android"; 		// string, android ID
	public static final String TAG_MODEL = "model"; 		// string, 핸드폰 모델
	public static final String TAG_BRAND = "brand"; 		// string, 핸드폰 브랜드




    private static MithrilPreferenceManager prefManager = null;

    public static synchronized MithrilPreferenceManager getInstance(Context context) {
        if (null == prefManager) {
            prefManager = new MithrilPreferenceManager(context);
        }
        return prefManager;
    }

	public static void putInt(Context context, String key, int value) {
        getInstance(context).putInt(key, value);
	}
	
	public static void putString(Context context, String key, String value) {
        getInstance(context).putString(key, value);
	}


	public static void putBoolean(Context context, String key, boolean value) {
        getInstance(context).putBoolean(key, value);
	}
	
	public static void putLong(Context context, String key, long value) {
        getInstance(context).putLong(key, value);
	}
	
	public static int getInt(Context context, String key) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  -1;
		return getInstance(context).getInt(key, -1);
	}

	public static int getInt(Context context, String key, int defaultValue) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  -1;

		return getInstance(context).getInt(key, defaultValue);
	}

	public static String getString(Context context, String key) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  "";

		return getInstance(context).getString(key, "");
	}

	public static String getString(Context context, String key, String defaultValue) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  "";

		return getInstance(context).getString(key, defaultValue);
	}

	public static boolean getBoolean(Context context, String key) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  false;

		return getBoolean(context,key, false);
	}
	public static boolean getBoolean(Context context, String key , boolean defaultState) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  false;

		return getInstance(context).getBoolean(key, defaultState);
	}
	
	public static long getLong(Context context, String key) {
//		SharedPreferences pref = getSharedPreferences(context);
		if(context == null)
			return  0;

		return getInstance(context).getLong(key, 0);
	}
}

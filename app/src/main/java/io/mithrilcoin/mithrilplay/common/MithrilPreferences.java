package io.mithrilcoin.mithrilplay.common;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MithrilPreferences {

	public static final String PREF_KEY			= "key";
	public static final String TAG_PREFERENCES	= "ProjectMithrilPlay";

	private static MithrilPreferenceManager prefManager = null;

	public static final String TAG_INTRO_SLIDE = "intro_slide"; 	// boolean, Welcome page, when you launch your app
	public static final String TAG_AUTH_ID = "auth_id"; 		// string, ID that you give to the server at the time of registration or login (check login with or without)
	public static final String TAG_EMAIL_AUTH = "email_auth"; 	// boolean, Whether email is verified
	public static final String TAG_AUTH_DATE = "auth_date"; 	// string, Save when email is verified

	public static final String TAG_EMAIL = "email"; 			// string, Save Email Temporary
	public static final String TAG_ANDROD_ID = "android_id"; 		// string, android ID
	public static final String TAG_MODEL = "model"; 		// string, Mobile phone model
	public static final String TAG_BRAND = "brand"; 		// string, Mobile phone brand
	public static final String TAG_OS_VERSION = "os_version"; 		// string, Mobile os_version
	public static final String TAG_APP_VERSION = "app_version"; 		// string, Mobile app_version

	public static final String TAG_PUSH_ID = "push_id"; 		// string, push_id

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
		if(context == null)
			return  -1;
		return getInstance(context).getInt(key, -1);
	}

	public static int getInt(Context context, String key, int defaultValue) {
		if(context == null)
			return  -1;

		return getInstance(context).getInt(key, defaultValue);
	}

	public static String getString(Context context, String key) {
		if(context == null)
			return  "";

		return getInstance(context).getString(key, "");
	}

	public static String getString(Context context, String key, String defaultValue) {
		if(context == null)
			return  "";

		return getInstance(context).getString(key, defaultValue);
	}

	public static boolean getBoolean(Context context, String key) {
		if(context == null)
			return  false;

		return getBoolean(context,key, false);
	}
	public static boolean getBoolean(Context context, String key , boolean defaultState) {
		if(context == null)
			return  false;

		return getInstance(context).getBoolean(key, defaultState);
	}
	
	public static long getLong(Context context, String key) {
		if(context == null)
			return  0;

		return getInstance(context).getLong(key, 0);
	}

	public static void saveMap(Context context, String key, Map<String,String> inputMap){
		SharedPreferences pSharedPref = context.getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE);
		if (pSharedPref != null){
			JSONObject jsonObject = new JSONObject(inputMap);
			String jsonString = jsonObject.toString();
			SharedPreferences.Editor editor = pSharedPref.edit();
			editor.remove(key).commit();
			editor.putString(key, jsonString);
			editor.commit();
		}
	}

	public static Map<String,String> loadMap(Context context, String key){
		Map<String,String> outputMap = new HashMap<String,String>();
		SharedPreferences pSharedPref = context.getSharedPreferences(TAG_PREFERENCES, Context.MODE_PRIVATE);
		try{
			if (pSharedPref != null){
				String jsonString = pSharedPref.getString(key, (new JSONObject()).toString());
				JSONObject jsonObject = new JSONObject(jsonString);
				Iterator<String> keysItr = jsonObject.keys();
				while(keysItr.hasNext()) {
					String k = keysItr.next();
					String v = (String) jsonObject.get(k);
					outputMap.put(k,v);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return outputMap;
	}


}

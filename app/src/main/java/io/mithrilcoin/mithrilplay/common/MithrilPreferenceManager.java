package io.mithrilcoin.mithrilplay.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MithrilPreferenceManager {
	
	private String TAG = "MithrilPreferenceManager";
	
	private static final String CRYPTOKEY = "308b4ba610cf3f1eeac517d98a534932cc70c28f0da6036b2e7091ccf80f42dd";

	private static final String PREF_FILE = "MithrilPlay_prefs";
	private Map<String, Object> mPreferenceInfo;
	private SharedPreferences pref;
	private AesCipher aesCipher;

	private Context context;
	
	private static MithrilPreferenceManager _instance = null;

	public static MithrilPreferenceManager getInstance(Context context) {
		synchronized (MithrilPreferenceManager.class) {
			_instance = new MithrilPreferenceManager();
		}
		return _instance;
	}

	public MithrilPreferenceManager() {
		mPreferenceInfo = new HashMap<String, Object>();
		pref = CommonApplication.getApplication().getSharedPreferences(MithrilPreferences.TAG_PREFERENCES, Context.MODE_MULTI_PROCESS);
		initializeAesCipher();
	}

	public MithrilPreferenceManager(Context context) {
		this.context = context.getApplicationContext();
		mPreferenceInfo = new HashMap<String, Object>();
		pref = context.getSharedPreferences(MithrilPreferences.TAG_PREFERENCES, Context.MODE_MULTI_PROCESS);
		initializeAesCipher();
	}

	private SharedPreferences getPreferences(int mode) {
		return context.getSharedPreferences(PREF_FILE, mode);
	}

	private boolean checkEncryptKey(String key) {

//        암호화 하지 않을 값들은 여기에 추가한다
//        if (MithrilPreferences.PREF_MSISDN.equals(key)) {
//            return false;
//        }

//        Log.d("jhsAES","checkEncryptKey = " + key);

		return true;
	}

	private boolean initializeAesCipher() {
		aesCipher = null;
		byte[] aesKey = AesCipher.getAESKey(CRYPTOKEY);
		byte[] initializedVector = AesCipher.getInitializationVector(CRYPTOKEY);
		if (aesKey != null && initializedVector != null) {
			try {
				aesCipher = new AesCipher(aesKey, initializedVector);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	private String encrypt(String plain) {
		if (aesCipher == null) {
			return plain;
		}
		byte[] temp = aesCipher.encrypt(plain.getBytes());
		if (temp == null) {
			return plain;
		}
		return AesCipher.getHexString(temp);
	}

	private String decrypt(String byteText) {

		if (aesCipher == null) {
			return byteText;
		}
		byte[] temp = AesCipher.hexToByteArray(byteText);
		if (temp == null) {
			return byteText;
		}

		byte[] decryptByte = aesCipher.decrypt(temp);
		if (decryptByte == null) {
			return byteText;
		}
		return new String(decryptByte);
	}

	public Set<String> getAll() {
		pref = getPreferences(Context.MODE_PRIVATE);
		return pref.getAll().keySet();
	}

	public synchronized String getString(String key, String defValue) {
		if (key == null) {
			return defValue;
		}

		String value = defValue;
		if (checkEncryptKey(key)) {
			// 1. memory에 있는지 확인한다.
			if (mPreferenceInfo.containsKey(key)) {
				return (String) mPreferenceInfo.get(key);
			}
			// 2-1. 암호화 시킨 key값으로 preference가 있는지 확인한다.
			String encryptKey = encrypt(key);
			if (!TextUtils.isEmpty(encryptKey) && pref.contains(encryptKey)) {
				String temp = pref.getString(encryptKey, defValue);
				if (!TextUtils.isEmpty(temp)) {
					value = decrypt(temp);
				}
                mPreferenceInfo.put(key, value);
			} else if (pref.contains(key)) { // 2-2. 암호화 안된 key값으로 preference를 확인한다.
				String temp = pref.getString(key, defValue);
				value = temp;
				if (!TextUtils.isEmpty(temp)) {
					String encryptValue = encrypt(value);
					if (!TextUtils.isEmpty(encryptKey) && !TextUtils.isEmpty(encryptValue)) {
						if (pref.edit().putString(encryptKey, encryptValue).commit()) {
							pref.edit().remove(key).commit();
						}
					}
				}
                mPreferenceInfo.put(key, value);
			}

		} else {
			if (pref.contains(key)) {
				try {
					value = pref.getString(key, defValue);
				} catch (Exception e) {
					Log.e("Error occured while trying to get string from key", getClass().getSimpleName());
				}
			}
		}
//        Log.d("jhsAES","getString value =" + value);
		return value;
	}

	public synchronized void putString(String key, String value) {
		if (key == null) {
			return;
		}

		if (value == null) {
			value = "";
		}

		if (checkEncryptKey(key)) {
			String encryptKey = encrypt(key);
			String encryptValue = encrypt(value);
			putStringInternal(encryptKey, encryptValue);
			mPreferenceInfo.put(key, value);

            // 기존에 암호화 안된 preference는 삭제
            if (pref.contains(key)) {
                pref.edit().remove(key).commit();
            }

		} else {
			putStringInternal(key, value);
		}
	}

	private void putStringInternal(String key, String value) {
		pref.edit().putString(key, value).apply();
	}

	public synchronized Boolean getBoolean(String key, Boolean defValue) {
		if (key == null) {
			return defValue;
		}
        if (mPreferenceInfo.containsKey(key)) {
            if (CommonApplication.DEBUG_MODE) {
                Log.i("getBoolean - key : " + key + ", value : " +
                                Boolean.toString((Boolean) mPreferenceInfo.get(key)),
                        getClass().getSimpleName());
            }
//            Log.d("jhsAES","getBoolean value =" + (Boolean) mPreferenceInfo.get(key));
            return (Boolean) mPreferenceInfo.get(key);
        } else {
            String encryptKey = encrypt(key);
            Boolean value = defValue;
			// 암호화 시킨 key값으로 preference가 있는지 확인한다.
            if (!TextUtils.isEmpty(encryptKey) && pref.contains(encryptKey)) {
                value = pref.getBoolean(encryptKey, defValue);
                mPreferenceInfo.put(key, value);
            } else if(pref.contains(key)){ // 	암호화 안된 key값으로 preference를 확인한다.
				Boolean temp = pref.getBoolean(key, defValue);
				value = temp;
				if (temp != null) {
					if (pref.edit().putBoolean(encryptKey, value).commit()) {
                        pref.edit().remove(key).commit();
                    }
				}
                mPreferenceInfo.put(key, value);
			}
            if (CommonApplication.DEBUG_MODE) {
                Log.i("getBoolean - key : " + key + ", value : " +
                        Boolean.toString(value), getClass().getSimpleName());
            }
//            Log.d("jhsAES","getBoolean value =" + value);
            return value;
        }
//		Boolean value = pref.getBoolean(key, defValue);
		// if( ChatONLogWriter.D_FLAG ){
		// ChatONLogWriter.dlog("getBoolean - key : " + key + ", value : " +
		// Boolean.toString(value), getClass().getSimpleName());
		// }
//		return value;
	}

	public synchronized void putBoolean(String key, Boolean value) {
		if (key == null) {
			return;
		}
		if (value == null) {
			return;
		}
        mPreferenceInfo.put(key, value);
        putBooleanInternal(encrypt(key), value);

        // 기존에 암호화 안된 preference는 삭제
        if (pref.contains(key)) {
            pref.edit().remove(key).commit();
        }

        if (CommonApplication.DEBUG_MODE) {
            Log.i("putBoolean - key : " + key + ", value : " +
                    Boolean.toString(value), getClass().getSimpleName());
        }
//		putBooleanInternal(key, value);
	}

	private void putBooleanInternal(String key, Boolean value) {
		pref.edit().putBoolean(key, value).apply();
	}

	public synchronized long getLong(String key, long defValue) {
		if (key == null) {
			return defValue;
		}
        if (mPreferenceInfo.containsKey(key)) {
            if (CommonApplication.DEBUG_MODE) {
                Log.i("getLong - key : " + key + ", value : " +
                                Long.toString((Long) mPreferenceInfo.get(key)),
                        getClass().getSimpleName());
            }
            return (Long) mPreferenceInfo.get(key);
        } else {
            String encryptKey = encrypt(key);
            Long value = defValue;
            // 암호화 시킨 key값으로 preference가 있는지 확인한다.
            if (!TextUtils.isEmpty(encryptKey) && pref.contains(encryptKey)) {
                value = pref.getLong(encryptKey, defValue);
                mPreferenceInfo.put(key, value);
            } else if(pref.contains(key)) { // 	암호화 안된 key값으로 preference를 확인한다.
                Long temp = pref.getLong(key, defValue);
                value = temp;
                if (temp != null) {
                    if (pref.edit().putLong(encryptKey, value).commit()) {
                        pref.edit().remove(key).commit();
                    }
                }
                mPreferenceInfo.put(key, value);
            }
            if (CommonApplication.DEBUG_MODE) {
                Log.i("getLong - key : " + key + ", value : " +
                        Long.toString(value), getClass().getSimpleName());
            }
            return value;
        }
//		Long value = pref.getLong(key, defValue);
		// if( ChatONLogWriter.D_FLAG ){
		// ChatONLogWriter.dlog("getLong - key : " + key + ", value : " +
		// Long.toString(value), getClass().getSimpleName());
		// }
//		return value;
	}

	public synchronized void putLong(String key, Long value) {
		if (key == null) {
			return;
		}
		if (value == null) {
			return;
		}

        mPreferenceInfo.put(key, value);
        putLongInternal(encrypt(key), value);

        // 기존에 암호화 안된 preference는 삭제
        if (pref.contains(key)) {
            pref.edit().remove(key).commit();
        }

        if (CommonApplication.DEBUG_MODE) {
            Log.i("putLong - key : " + key + ", value : " +
                    Long.toString(value), getClass().getSimpleName());
        }
//		putLongInternal(key, value);
	}

	public void putLongInternal(String key, Long value) {
			pref.edit().putLong(key, value).apply();
	}

	public synchronized Integer getInt(String key, Integer defValue) {
		if (key == null) {
			return defValue;
		}
        if (mPreferenceInfo.containsKey(key)) {
            if (CommonApplication.DEBUG_MODE) {
                Log.i("getInt - key : " + key + ", value : " +
                                Integer.toString((Integer) mPreferenceInfo.get(key)),
                        getClass().getSimpleName());
            }
//            Log.d("jhsAES","getInt value =" + (Integer) mPreferenceInfo.get(key));
            return (Integer) mPreferenceInfo.get(key);
        } else {
            String encryptKey = encrypt(key);
            int value = defValue;
            // 암호화 시킨 key값으로 preference가 있는지 확인한다.
            if (!TextUtils.isEmpty(encryptKey) && pref.contains(encryptKey)) {
                value = pref.getInt(encryptKey, defValue);
                mPreferenceInfo.put(key, value);
            } else if(pref.contains(key)) { // 	암호화 안된 key값으로 preference를 확인한다.
                int temp = pref.getInt(key, defValue);
                value = temp;
                if (pref.edit().putInt(encryptKey, value).commit()) {
                    pref.edit().remove(key).commit();
                }
                mPreferenceInfo.put(key, value);
            }

            if (CommonApplication.DEBUG_MODE) {
                Log.i("getInt - key : " + key + ", value : " +
                        Integer.toString(value), getClass().getSimpleName());
            }
//            Log.d("jhsAES","getInt value =" + value);
            return value;
        }
//		int value = pref.getInt(key, defValue);
//        if (SsocioApplication.DEBUG_MODE) {
//            Log.i("getInt - key : " + key + ", value : " +
//                    Integer.toString(value), getClass().getSimpleName());
//        }
//        return value;
	}

	public synchronized void putInt(String key, Integer value) {
		if (key == null) {
			return;
		}
		if (value == null) {
			return;
		}
        mPreferenceInfo.put(key, value);
        putIntInternal(encrypt(key), value);

        // 기존에 암호화 안된 preference는 삭제
        if (pref.contains(key)) {
            pref.edit().remove(key).commit();
        }

        if (CommonApplication.DEBUG_MODE) {
            Log.i("putInt - key : " + key + ", value : " + Integer.toString(value), getClass().getSimpleName());
        }
//		putIntInternal(key, value);
	}

	public void putIntInternal(String key, Integer value) {
		pref.edit().putInt(key, value).apply();
	}

	public synchronized void remove(String key) {
		if (key == null) {
			return;
		}
        if (CommonApplication.DEBUG_MODE) {
            Log.i("remove - key : " + key, getClass().getSimpleName());
        }
        if (checkEncryptKey(key)) {
			mPreferenceInfo.remove(key);
			if (pref.edit().remove(encrypt(key)).commit()) {
                if (CommonApplication.DEBUG_MODE) {
                    Log.i("remove success - key : " + key, getClass().getSimpleName());
                }
            }
		} else {
			removeInternal(key);
		}
	}

	private void removeInternal(String key) {
			pref.edit().remove(key).apply();
	}

	public synchronized void clear() {
		mPreferenceInfo.clear();
		clearInternal();
        if (CommonApplication.DEBUG_MODE) {
            Log.i("clear - count : " + mPreferenceInfo.size(), getClass().getSimpleName());
        }
    }

	private void clearInternal() {
			pref.edit().clear().apply();
	}

	public synchronized boolean contains(String key) {
		if (key == null) {
			return false;
		}

		if (checkEncryptKey(key)) {
			String encryptKey = encrypt(key);
			if (mPreferenceInfo.containsKey(key) || pref.contains(key) || (!TextUtils.isEmpty(encryptKey) && pref.contains(encryptKey))) {
				 if(CommonApplication.DEBUG_MODE){
                     Log.i("contain - key : " + key + " - true", getClass().getSimpleName());
				 }
				return true;
			}
		} else {
			if (mPreferenceInfo.containsKey(key) || pref.contains(key)) {
                if(CommonApplication.DEBUG_MODE){
                    Log.i("contain - key : " + key + " - true", getClass().getSimpleName());
				 }
				return true;
			}
		}
		return false;
	}

}

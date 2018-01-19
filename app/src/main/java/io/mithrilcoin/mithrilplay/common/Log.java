package io.mithrilcoin.mithrilplay.common;


public class Log {
	
    public static void e(String tag, String message) {
        if (CommonApplication.LOG_E) {
            android.util.Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable tr) {
        if (CommonApplication.LOG_E) {
            android.util.Log.e(tag, message, tr);
        }
    }
    
    public static void d(String tag, String message) {
        if (CommonApplication.LOG_D) {
            android.util.Log.d(tag, message);
        }
    }

    public static void v(String tag, String message) {
    	if (CommonApplication.LOG_V) {
			android.util.Log.d(tag, message);
    	}
    }

    public static void i(String tag, String message) {
        if (CommonApplication.LOG_I) {
            android.util.Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (CommonApplication.LOG_W) {
            android.util.Log.w(tag, message);
        }
    }

}

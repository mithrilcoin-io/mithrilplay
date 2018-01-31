package io.mithrilcoin.mithrilplay.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;

public class CommonApplication extends Application {

    private static volatile CommonApplication instance = null;

    // Handles the log output of the logcat in development mode
    public static final int DEVELOPMENT_MODE = 2;
    public static final int PRODUCT_MODE = 0;
    public static int BUILD_MODE = 0;
    public static boolean LOG_E = false;
    public static boolean LOG_D = false;
    public static boolean LOG_V = false;
    public static boolean LOG_I = false;
    public static boolean LOG_W = false;
    public static boolean DEBUG_MODE = false;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // log setting
        initLogSetting();

    }

    /**
     * Returns the build information of the current application
     * 0 : Release Mode (Debuggering impossible)
     * 2 : Development Mode (Debuggering possible)
     * @param context
     * @return
     */
    public static int getBuildMode(Context context){
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    private void initLogSetting(){
        BUILD_MODE = getBuildMode(getApplicationContext());

        LOG_E = (BUILD_MODE == DEVELOPMENT_MODE) ? true : false;
        LOG_D = (BUILD_MODE == DEVELOPMENT_MODE) ? true : false;
        LOG_V = (BUILD_MODE == DEVELOPMENT_MODE) ? true : false;
        LOG_I = (BUILD_MODE == DEVELOPMENT_MODE) ? true : false;
        LOG_W = (BUILD_MODE == DEVELOPMENT_MODE) ? true : false;

        DEBUG_MODE = (BUILD_MODE == DEVELOPMENT_MODE) ? true : false;
    }

    public static Resources getResource() {
        return instance.getResources();
    }

    public static CommonApplication getApplication() {
        return instance;
    }

}

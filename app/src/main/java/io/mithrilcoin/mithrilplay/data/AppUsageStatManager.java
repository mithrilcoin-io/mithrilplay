package io.mithrilcoin.mithrilplay.data;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.Map;

import io.mithrilcoin.mithrilplay.common.MithrilPreferences;

/**
 * UsageStatsManager
 */
public class AppUsageStatManager {

    public static final String TAG = "mithril";

    public static Context mContext;

    public static long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = 0;
        String mAuthDate = MithrilPreferences.getString(mContext, MithrilPreferences.TAG_AUTH_DATE);

        long lAuthDate = 0;

        if(!TextUtils.isEmpty(mAuthDate)){
            lAuthDate = Long.parseLong(mAuthDate);
        }

        if(lAuthDate > calendar.getTimeInMillis()){
            startTime = lAuthDate;
        }else{
            startTime = calendar.getTimeInMillis();
        }

//        Log.d(TAG, "getStartTime today start calendar:" + startTime);

        return startTime;
    }

    /**
     * Get app usage list from 00:00 till today
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Map<String, UsageStats> getTodayUsageStatList(Context context){
        mContext = context;
        UsageStatsManager usm = getUsageStatsManager(context);
        Map<String, UsageStats> usageStats = usm.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis());
        return usageStats;
    }

    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        return usm;
    }

}

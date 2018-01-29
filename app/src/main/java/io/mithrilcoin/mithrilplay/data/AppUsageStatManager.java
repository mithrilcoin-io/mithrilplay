package io.mithrilcoin.mithrilplay.data;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

/**
 */
public class AppUsageStatManager {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-M-d HH:mm:ss");

    public static final String TAG = "mithril";

    public static Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("ResourceType")
    public static void getStats(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_YEARLY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usm.queryEvents(startTime,endTime);
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null){
                Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
            }
        }
    }


    public static long getStartTime() {
//        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
//        Calendar calendar = GregorianCalendar.getInstance();
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

        Log.d("mithriltime", "getStartTime today start calendar:" + startTime);

//        TimeZone utc = TimeZone.getTimeZone("UTC");
//        dateFormat2.setTimeZone(utc);
//        Log.d("mithriltime", "getStartTime today start dateFormat2:" + dateFormat2.format(calendar.getTimeInMillis()));
//        Log.d("mithriltime", "getStartTime System.currentTimeMillis():" + System.currentTimeMillis());
//        Log.d("mithriltime", "getStartTime System.currentTimeMillis() dateFormat2:" + dateFormat2.format(System.currentTimeMillis()));

        return startTime;
    }

    /**
     *  오늘 00시부터 현재 시간까지 앱사용리스트 가져오기
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Map<String, UsageStats> getTodayUsageStatList(Context context){
        mContext = context;
        UsageStatsManager usm = getUsageStatsManager(context);

        Log.d("mithriltime", "System.currentTimeMillis():" + System.currentTimeMillis());

        Map<String, UsageStats> usageStats = queryAndAggregateUsageStatsDaily(getStartTime(), System.currentTimeMillis());
//        Map<String, UsageStats> usageStats = queryAndAggregateUsageStatsDaily(startTime, endTime);
//        Map<String, UsageStats> usageStats = queryAndAggregateUsageStatsDaily(20180126, 20180127);
//        Map<String, UsageStats> usageStats = queryAndAggregateUsageStatsDaily(System.currentTimeMillis() - (20 * 60 * 1000), System.currentTimeMillis());

//        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
//        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, getStartTime()  , System.currentTimeMillis());
//        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0  , System.currentTimeMillis());

//        for (UsageStats us : queryUsageStats) {
//            Log.d("mithriltime", us.getPackageName() + " = " + us.getTotalTimeInForeground());
//        }

        return usageStats;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Map<String, UsageStats> queryAndAggregateUsageStatsDaily(long beginTime, long endTime) {

        UsageStatsManager usm = getUsageStatsManager(mContext);
        List<UsageStats> stats = usm.queryUsageStats(INTERVAL_DAILY, beginTime, endTime);
        if (stats.isEmpty()) {
            return Collections.emptyMap();
        }

        ArrayMap<String, UsageStats> aggregatedStats = new ArrayMap<>();
        final int statCount = stats.size();
        for (int i = 0; i < statCount; i++) {
            UsageStats newStat = stats.get(i);
            UsageStats existingStat = aggregatedStats.get(newStat.getPackageName());
            if (existingStat == null) {
                aggregatedStats.put(newStat.getPackageName(), newStat);
            } else {
                existingStat.add(newStat);
            }
        }
        return aggregatedStats;
    }

    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }

}

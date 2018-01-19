package io.mithrilcoin.mithrilplay.data;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.common.Log;

/**
 * Created by User on 3/2/15.
 */
public class UStatManager {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
//    public static final String TAG = UStatManager.class.getSimpleName();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

//        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,getStartTime2(),endTime);
        return usageStatsList;
    }

    public static long getStartTime() {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.DATE);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Log.d(TAG, "today start:" + dateFormat2.format(calendar.getTimeInMillis()) );

        return calendar.getTimeInMillis();
    }

    public static long getStartTime2() {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.DATE);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Log.d(TAG, "today start:" + dateFormat2.format(System.currentTimeMillis() -100000) );

        return System.currentTimeMillis() -100000;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getUsageStatsList2(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
//        Calendar calendar = Calendar.getInstance();
//        long endTime = calendar.getTimeInMillis();
//        calendar.add(Calendar.DATE, -1);
//        long startTime = calendar.getTimeInMillis();
//
//        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
//        Log.d(TAG, "Range end:" + dateFormat.format(endTime));
//        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);

        Log.d(TAG, "current time:" + dateFormat2.format(System.currentTimeMillis()));
        Map<String, UsageStats> usageStats = usm.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis());

        Log.d(TAG, "usageStats.size():" + usageStats.size());

        for(int i = 0 ; i<usageStats.size() ; i++){


        }

        List<UsageStats> stats = new ArrayList<>();
        stats.addAll(usageStats.values());


        return stats;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void printUsageStats(List<UsageStats> usageStatsList){
        Log.d(TAG, "usageStatsList.size():" + usageStatsList.size());
        for (UsageStats u : usageStatsList){
//            Log.d(TAG, "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: " + u.getTotalTimeInForeground()) ;
            if(u.getPackageName().equals("com.ketchapp.rider")) {
                Log.d(TAG, "Pkg: " + u.getPackageName() + "\t"
                        + "getFirstTimeStamp: " +  dateFormat2.format(u.getFirstTimeStamp()) + "\t"
                        + "getLastTimeStamp: " + dateFormat2.format(u.getLastTimeStamp())  + "\t"
                        + "getTotalTimeInForeground: " + u.getTotalTimeInForeground() + "\t"
                        + "게임시간 : " + getTime(u.getTotalTimeInForeground())
                        + "getLastTimeUsed: " + dateFormat2.format(u.getLastTimeUsed())
                );

                Toast.makeText(mContext, "게임시간 : " + getTime(u.getTotalTimeInForeground()), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void printCurrentUsageStatus(Context context){

        mContext = context;

        printUsageStats(getUsageStatsList2(context));
    }

    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }

    public static String getTime(long sTime){
        String time = "";
        int hours   = (int) ((sTime / (1000*60*60)) % 24);  //시
        int minutes = (int) ((sTime / (1000*60)) % 60);     //분
        int seconds = (int) (sTime / 1000) % 60 ;           //초

        time = hours + "시간 " + minutes + "분 " + seconds + "초";
        Log.d(TAG, "게임시간 :" + time);
        return time;
    }

}

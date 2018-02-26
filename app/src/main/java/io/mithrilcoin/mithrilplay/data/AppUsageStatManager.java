package io.mithrilcoin.mithrilplay.data;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.common.CommonApplication;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.db.entity.PlayData;
import io.mithrilcoin.mithrilplay.network.vo.GamePlayDataRequest;


/**
 *  App Game data Manager _ UsageStatsManager
 */
public class AppUsageStatManager {

    public static final String TAG = "mithril";

    public static Context mContext;

    /**
     * Get game start point timestamp
     * @return long type timestamp
     */
    public static long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = 0;
        String mAuthDate = MithrilPreferences.getString(CommonApplication.getApplication(), MithrilPreferences.TAG_AUTH_DATE);

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
     * Get game start point timestamp
     * Determined in the DB during the last game end time or today's start date
     * @return long type timestamp
     */
    public static long getTodayOrLastGameStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = 0;
        String mAuthDate = MithrilPreferences.getString(CommonApplication.getApplication(), MithrilPreferences.TAG_AUTH_DATE);

        long lAuthDate = 0;

        if(!TextUtils.isEmpty(mAuthDate)){
            lAuthDate = Long.parseLong(mAuthDate);
        }

        if(lAuthDate > calendar.getTimeInMillis()){
            startTime = lAuthDate;
        }else{
            startTime = calendar.getTimeInMillis();
        }

        String getLastEndtime = CommonApplication.getApplication().getDB().playDataDao().getLastEndtime();
        Log.d("mithril", "getLastEndtime =" + getLastEndtime);
        if(getLastEndtime != null && Long.parseLong(getLastEndtime) > startTime){
            startTime = Long.parseLong(getLastEndtime);
        }
//        Log.d(TAG, "getStartTime today start calendar:" + startTime);
        Log.d("mithril", "startTime =" + startTime);
        return startTime;
    }

    public static List<GamePlayDataRequest> getTodayPlayData(){
        String email = MithrilPreferences.getString(CommonApplication.getApplication(), MithrilPreferences.TAG_EMAIL);
        return CommonApplication.getApplication().getDB().playDataDao().getTodayGameList(getStartTime(), email);
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

    /**
     * push get query
     * @return String
     */
    public static String getQueryResults(String query) {

        JSONArray resultSet = new JSONArray();
        Cursor cursor = CommonApplication.getApplication().getDB().query(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d(TAG, cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d(TAG, resultSet.toString());
        return resultSet.toString();
    }


}

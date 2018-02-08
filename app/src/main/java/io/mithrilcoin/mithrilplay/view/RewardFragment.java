package io.mithrilcoin.mithrilplay.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.common.TimeUtil;
import io.mithrilcoin.mithrilplay.data.AppUsageStatManager;
import io.mithrilcoin.mithrilplay.network.RequestGameRewardOrder;
import io.mithrilcoin.mithrilplay.network.RequestTodayGameList;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamedataRewardResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.view.adapter.EventVO;
import io.mithrilcoin.mithrilplay.view.adapter.RewardAdapter;
import io.mithrilcoin.mithrilplay.view.auth.DataAccessInfoPermissionActivity;

/**
 *  Today Game list
 */
public class RewardFragment extends Fragment {

    public RewardFragment() {
    }

    public static final String TAG = "mithril";

    private HomeActivity mActivity = null;
    public static RewardFragment instance = null;
    private RewardAdapter mAdapter = null;
    private LinearLayoutManager mLayoutManager = null;

    private TextView tv_today_reward_info = null;
    private ImageView tv_today_reward_one, tv_today_reward_two, tv_today_reward_three = null;
    private RecyclerView mRecyclerView = null;
    private RelativeLayout mEmptyTodayReward = null;

    private PackageManager pm;
    private LinkedHashMap<String, AppGameBody> mGameList = new LinkedHashMap<String, AppGameBody>();

    private int isGetRewardCnt = 0;
    private String mTodaytime;      // Today's date to send to server (ex 2018-01-03)
    private String mGameValidTime;  // Game time for rewards

    public GameRewardCallListener gameRewardCallListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reward, container, false);

        mActivity = (HomeActivity) getActivity();
        instance = RewardFragment.this;
        pm = mActivity.getPackageManager();

        setLayout(rootView);

        return rootView;
    }

    private void setLayout(View v) {

        tv_today_reward_info = (TextView) v.findViewById(R.id.tv_today_reward_info);
        tv_today_reward_one = (ImageView) v.findViewById(R.id.tv_today_reward_one);
        tv_today_reward_two = (ImageView) v.findViewById(R.id.tv_today_reward_two);
        tv_today_reward_three = (ImageView) v.findViewById(R.id.tv_today_reward_three);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_today_reward);
        mEmptyTodayReward = (RelativeLayout) v.findViewById(R.id.empty_today_reward);

        gameRewardCallListener = new GameRewardCallListener() {
            @Override
            public void onGameReward(AppGameBody item) {
                getGameReward(item);
            }
        };

        setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mActivity.hasPermission()) {
            Intent intent = new Intent(mActivity, DataAccessInfoPermissionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mActivity.startActivityForResult(intent, Constant.REQUEST_CODE_HOME_USE_INFO);
        }else{
            getTodayGameAppList();
        }
    }

    private void setRewardcnt(int cnt){

        ImageView[] mRview= {tv_today_reward_one, tv_today_reward_two, tv_today_reward_three};
        for(int i = 0 ; i < 3 ; i++){
            if(i < cnt){
                mRview[i].setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            }else{
                mRview[i].setColorFilter(ContextCompat.getColor(mActivity, R.color.colorGray), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }

        try{
            if(isGetRewardCnt == 3){
                tv_today_reward_info.setText(getString(R.string.today_reward_end));
            }else{
                tv_today_reward_info.setText(String.format(getString(R.string.today_reward_remain), (3-isGetRewardCnt) + ""));
            }
        }catch (NumberFormatException e){

        };

    }

    private void setAdapter(){

        mLayoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new RewardAdapter(mActivity, mGameList, gameRewardCallListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    public void getTodayGameAppList(){

        mTodaytime = TimeUtil.getTodayUtcTime();

        // Email authentication completion time
        String mAuthDate = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_DATE);
        long emailAuthTime = 0;
        if(!TextUtils.isEmpty(mAuthDate)){
            emailAuthTime = Long.parseLong(mAuthDate);
        }

        // user id
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        // Get all apps list installed on your phone
        List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        // List of apps launched today (all apps)
        Map<String, UsageStats> todayAppUsage = AppUsageStatManager.getTodayUsageStatList(mActivity);

        ArrayList<EventVO> list = new ArrayList<EventVO>();
        HashMap<String, Long> eventTime = new HashMap<String, Long>();

        // Get events by app
        UsageStatsManager usm = (UsageStatsManager) mActivity.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents uEvents = usm.queryEvents(AppUsageStatManager.getStartTime(), System.currentTimeMillis());
        while (uEvents.hasNextEvent()){
            UsageEvents.Event event = new UsageEvents.Event();
            uEvents.getNextEvent(event);
            if (event != null){
//                Log.d(TAG, "Event: " + event.getPackageName() + "__" +  event.getTimeStamp() + "__" +  event.getEventType() );
                EventVO eventVO = new EventVO();
                eventVO.setPackagename(event.getPackageName());
                eventVO.setState(event.getEventType() + "");
                eventVO.setUtcTime(event.getTimeStamp() + "");
                if(event.getEventType() == 7){
                    continue;
                }
                list.add(eventVO);
            }
        }
        eventTime = sortList(list);

        // Setting the app list value to be sent to the server
        List<AppRequest> appRequestList = new ArrayList<AppRequest>();
        for(ApplicationInfo app : mAppList){
            AppRequest appRequest = new AppRequest();
            appRequest.setPackagename(app.packageName);
            appRequest.setPlaydate(mTodaytime);

            // App usage time setting
            if(todayAppUsage.containsKey(app.packageName)){
                if(eventTime.containsKey(app.packageName)){
                    appRequest.setPlaytime(eventTime.get(app.packageName).toString());
                }else{
                    appRequest.setPlaytime(todayAppUsage.get(app.packageName).getTotalTimeInForeground()+"");
                }
            }else{
                appRequest.setPlaytime("0");
            }

            if(todayAppUsage.containsKey(app.packageName)){
                if((long)todayAppUsage.get(app.packageName).getLastTimeUsed() > emailAuthTime){
                    if((long)todayAppUsage.get(app.packageName).getLastTimeUsed() > AppUsageStatManager.getStartTime()) {
                        appRequestList.add(appRequest);
                    }
                }
            }else{
                appRequestList.add(appRequest);
            }

        }

        RequestTodayGameList requestTodayGameList = new RequestTodayGameList(mActivity, mId, appRequestList);
        requestTodayGameList.post(new RequestTodayGameList.ApiTodayGameListListener() {
            @Override
            public void onSuccess(AppGameListResponse item) {

                if(item.getBody() == null || item.getBody().size() == 0){
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyTodayReward.setVisibility(View.VISIBLE);

                    try{
                        tv_today_reward_info.setText(String.format(getString(R.string.today_reward_remain), (3-isGetRewardCnt) + ""));
                        setRewardcnt(isGetRewardCnt);
                    }catch (NumberFormatException e){

                    };

                }else{

                    if(item.getUserInfo() != null && item.getUserInfo().getValidtime() != null){
                        mGameValidTime = item.getUserInfo().getValidtime();
                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyTodayReward.setVisibility(View.GONE);
                    List<AppGameBody> installGames = item.getBody();
                    gameAppFilltering(installGames);
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    private  HashMap<String, Long> sortList(ArrayList<EventVO> list){
        HashMap<String, ArrayList<EventVO>> maplist = new HashMap<>();
        HashMap<String, Long> maplist2 = new HashMap<>();
        AscendingObj abd = new AscendingObj();
        for(EventVO pack : list){
            if(pack.getPackagename().equals(mActivity.getPackageName())){
                continue;
            }

            if( maplist.containsKey(pack.getPackagename())){
                maplist.get(pack.getPackagename()).add(pack);
            }else{
                ArrayList<EventVO> tempList = new ArrayList<>();
                tempList.add(pack);
                maplist.put(pack.getPackagename(),  tempList);
            }
        }

        Iterator<String> keys = maplist.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            ArrayList<EventVO> tempList =  maplist.get(key);

            Collections.sort(tempList,abd);

            long sum = 0;
            int length = tempList.size() / 2;
            int idx = 0;
            for( int i = 0; i < length; i++){
                long diff = Long.parseLong(tempList.get(idx).getUtcTime()) -  Long.parseLong(tempList.get(idx + 1).getUtcTime());
                sum += diff;
                idx += 2;
            }
            maplist2.put(key, sum);
        }

        return maplist2;
    }

    public class AscendingObj implements Comparator<EventVO> {

        @Override
        public int compare(EventVO o1, EventVO o2) {
            return o2.getUtcTime().compareTo(o1.getUtcTime());
        }

    }

    // list adapter data setting
    private void gameAppFilltering(List<AppGameBody> gameList){

        isGetRewardCnt = 0;
        for(AppGameBody appGameBody : gameList){
            String gPackagename =  appGameBody.getPackagename();
            if(!appGameBody.getReward().equals("0") ){
                ++isGetRewardCnt;
            }

            if(!appGameBody.getPlaytime().equals("0")){
                mGameList.put(gPackagename, appGameBody);
            }
        }

//        Log.d(TAG, "isGetRewardCnt = " + isGetRewardCnt );

        if(mGameList.size() > 0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTodayReward.setVisibility(View.GONE);
            mAdapter.setItemData(mGameList);
            mAdapter.setValidTime(Long.parseLong(mGameValidTime));
            mAdapter.notifyDataSetChanged();
        }else{
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTodayReward.setVisibility(View.VISIBLE);
        }

        setRewardcnt(isGetRewardCnt);

    }

    // Receive game rewards
    private void getGameReward(AppGameBody appGameBody){

        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        RequestGameRewardOrder requestGameRewardOrder = new RequestGameRewardOrder(mActivity, mId, appGameBody);
        requestGameRewardOrder.post(new RequestGameRewardOrder.ApiGameRewardOrderListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(AppGamedataRewardResponse item) {

                if(item.getBody() != null){
                    getTodayGameAppList();
                }

            }

            @Override
            public void onFail() {

            }
        });

    }

    public interface GameRewardCallListener {
        void onGameReward(AppGameBody item);
    }

}

package io.mithrilcoin.mithrilplay.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
import io.mithrilcoin.mithrilplay.common.CommonApplication;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.common.TimeUtil;
import io.mithrilcoin.mithrilplay.data.AppUsageStatManager;
import io.mithrilcoin.mithrilplay.db.entity.PlayData;
import io.mithrilcoin.mithrilplay.network.RequestGamePackageList;
import io.mithrilcoin.mithrilplay.network.RequestGameRewardOrder;
import io.mithrilcoin.mithrilplay.network.RequestTodayDBGameList;
import io.mithrilcoin.mithrilplay.network.RequestTodayGameList;
import io.mithrilcoin.mithrilplay.network.vo.AppBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamedataRewardResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.network.vo.GamePlayDataRequest;
import io.mithrilcoin.mithrilplay.view.adapter.EventVO;
import io.mithrilcoin.mithrilplay.view.adapter.RewardAdapter;
import io.mithrilcoin.mithrilplay.view.auth.DataAccessInfoPermissionActivity;
import io.mithrilcoin.mithrilplay.view.listner.GameRewardCallListener;

/**
 *  Today Game list _ Local Database store
 */
public class RewardTodayFragment extends Fragment {

    public RewardTodayFragment() {
    }

    public static final String TAG = "mithril";

    private HomeActivity mActivity = null;
    public static RewardTodayFragment instance = null;
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
        instance = RewardTodayFragment.this;
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
            getAppGamePackage();
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
    public void getAppGamePackage(){

        // user id
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        // Get all apps list installed on your phone
        List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        List<AppBody> appBodyList = new ArrayList<AppBody>();
        for(ApplicationInfo app : mAppList) {
            AppBody appBody = new AppBody();
            appBody.setPackagename(app.packageName);
            appBodyList.add(appBody);
        }

        // game Package get
        RequestGamePackageList requestGamePackageList = new RequestGamePackageList(mActivity, mId, appBodyList);
        requestGamePackageList.post(new RequestGamePackageList.ApiGamePackageListListener() {
            @Override
            public void onSuccess(AppGamePackageListResponse item) {

                if(item.getBody() != null || item.getBody().size() > 0){
                    List<String> appGamePackage = new ArrayList<String>();
                    for(AppGamePackageBody app : item.getBody()){
                        appGamePackage.add(app.getPackagename());
                    }
                    gameSort(appGamePackage);
                }
            }

            @Override
            public void onFail() {

            }
        });

        // push query example
//        String qData = AppUsageStatManager.getQueryResults("select * from playdata where idx < 10");
//        Log.d(TAG, "qData =" +qData);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void gameSort(List<String> appGamePackage){

        // Games played today
        ArrayList<EventVO> list = new ArrayList<EventVO>();

        // Get events by app
        UsageStatsManager usm = (UsageStatsManager) mActivity.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents uEvents = usm.queryEvents(AppUsageStatManager.getTodayOrLastGameStartTime(), System.currentTimeMillis());
        while (uEvents.hasNextEvent()){
            UsageEvents.Event event = new UsageEvents.Event();
            uEvents.getNextEvent(event);
            if (event != null){
                EventVO eventVO = new EventVO();
                eventVO.setPackagename(event.getPackageName());
                eventVO.setState(event.getEventType() + "");
                eventVO.setUtcTime(event.getTimeStamp() + "");
                if(event.getEventType() == 1 || event.getEventType() == 2){
                    list.add(eventVO);
                }
            }
        }

        // Data to put in Local DB
        List<PlayData> playDataList = new ArrayList<PlayData>();

        String mEmail = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_EMAIL);
        String mAppversion = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_APP_VERSION);

        HashMap<String, ArrayList<EventVO>> maplist = new HashMap<>();
        for(EventVO pack : list){

            if(pack.getPackagename().equals(mActivity.getPackageName())){
                continue;
            }
            if(!appGamePackage.contains(pack.getPackagename())){
                continue;
            }

            if(maplist.containsKey(pack.getPackagename())){
                maplist.get(pack.getPackagename()).add(pack);
            }else{
                ArrayList<EventVO> tempList = new ArrayList<>();
                tempList.add(pack);
                maplist.put(pack.getPackagename(), tempList);
            }
        }

        // sort
        Ascending ascending = new Ascending();
        Iterator<String> keys = maplist.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            ArrayList<EventVO> tempList =  maplist.get(key);
            Collections.sort(tempList,ascending);
            int length = tempList.size() / 2;
            int idx = 0;
            for( int i = 0; i < length; i++){
                long diff = Long.parseLong(tempList.get(idx + 1).getUtcTime()) -  Long.parseLong(tempList.get(idx).getUtcTime());
                PlayData data = new PlayData(mEmail,tempList.get(idx).getPackagename(), tempList.get(idx).getUtcTime(), tempList.get(idx + 1).getUtcTime(), diff+"", mAppversion);
                idx += 2;
                playDataList.add(data);
            }
        }

        // Store in LocalDB
        CommonApplication.getApplication().getDB().playDataDao().insertAll(playDataList);


        // today game list
        List<GamePlayDataRequest> todayGameDataList = new ArrayList<GamePlayDataRequest>();
        todayGameDataList = AppUsageStatManager.getTodayPlayData();
        Log.d("mithril", "todayGameDataList.size() =" + todayGameDataList.size());

        // alttitle setting
        for(GamePlayDataRequest app : todayGameDataList){
            try {
                app.setAlttitle((String) pm.getApplicationLabel(pm.getApplicationInfo(app.getPackagename(), PackageManager.GET_UNINSTALLED_PACKAGES)));
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        // user id
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        RequestTodayDBGameList requestTodayDBGameList = new RequestTodayDBGameList(mActivity, mId, todayGameDataList);
        requestTodayDBGameList.post(new RequestTodayDBGameList.ApiTodayGameListListener() {
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

    public class Ascending implements Comparator<EventVO> {

        @Override
        public int compare(EventVO o1, EventVO o2) {
            return o1.getUtcTime().compareTo(o2.getUtcTime());
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
            @Override
            public void onSuccess(AppGamedataRewardResponse item) {

                if(item.getBody() != null){
                    getAppGamePackage();
                }

            }

            @Override
            public void onFail() {

            }
        });

    }

}

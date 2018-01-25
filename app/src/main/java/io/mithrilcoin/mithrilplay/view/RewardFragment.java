package io.mithrilcoin.mithrilplay.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.data.AppUsageStatManager;
import io.mithrilcoin.mithrilplay.network.RequestGamePackageList;
import io.mithrilcoin.mithrilplay.network.RequestGameRewardOrder;
import io.mithrilcoin.mithrilplay.network.RequestTodayGameList;
import io.mithrilcoin.mithrilplay.network.vo.AppBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamedataRewardResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.view.adapter.RewardAdapter;
import io.mithrilcoin.mithrilplay.view.adapter.RewardGameItem;


public class RewardFragment extends Fragment {

    public RewardFragment() {
    }

    private Activity mActivity = null;
    private RewardAdapter mAdapter = null;
    private LinearLayoutManager mLayoutManager = null;

    private TextView tv_today_reward_info = null;
    private ImageView tv_today_reward_one, tv_today_reward_two, tv_today_reward_three = null;
    private RecyclerView mRecyclerView = null;
    private TextView empty_data = null;


    private PackageManager pm;
    private List<ApplicationInfo> mAppList = null;
    private LinkedHashMap<String, AppGameBody> mGameList = new LinkedHashMap<String, AppGameBody>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String mTodaytime; // 서버에 전송할 오늘 날짜 (예 2018-01-03)

    public GameRewardCallListener gameRewardCallListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reward, container, false);

        mActivity = (Activity) getActivity();
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
        empty_data = (TextView) v.findViewById(R.id.empty_data);

        gameRewardCallListener = new GameRewardCallListener() {
            @Override
            public void onGameReward(AppGameBody item) {

                Log.d("mithril", "Rewardfragment onGameReward");
                getGameReward(item);

            }
        };

        setAdapter();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();

        mTodaytime = getTodayTime();

        // Start time
        long startTime = System.currentTimeMillis();

//        getAppData();
//        getData();
//        getGamePackageAppList();
        getTodayGameAppList();

        long endTime = System.currentTimeMillis();

        // Total time
        long lTime = endTime - startTime;
        Log.e("mithril", "today reward TIME : " + lTime + "(ms)");

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
    }

    private void setAdapter(){

        mLayoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new RewardAdapter(mActivity, mGameList, gameRewardCallListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    // 테스트용 (미사용)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    private void getAppData(){

        // 오늘 실행된 앱 목록
        Map<String, UsageStats> todayAppUsage = AppUsageStatManager.getTodayUsageStatList(mActivity);

        ArrayList<AppRequest> appRequestList = new ArrayList<AppRequest>();

        List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        for(ApplicationInfo app : mAppList){
            AppRequest appRequest;
            if(todayAppUsage.containsKey(app.packageName)){
                appRequest = new AppRequest(app.packageName, todayAppUsage.get(app.packageName).getTotalTimeInForeground()+"", todayAppUsage.get(app.packageName).getLastTimeUsed()+"");
            }else{
                appRequest = new AppRequest(app.packageName, "0", System.currentTimeMillis() +"");
            }
            appRequestList.add(appRequest);
        }

        Log.d("mithril","appRequestList.size() =" + appRequestList.size());
        for(AppRequest app2 : appRequestList){
//            Log.d("mithril",app2.getPackage() +","+ app2.getPlaytime() +","+ app2.getPlayDate());
        }


        mGameList.clear();
        // 리턴받고 온 데이터
        for(AppRequest appRequest : appRequestList){
            RewardGameItem rewardGameItem;
            if(todayAppUsage.containsKey(appRequest.getPackagename())){
                rewardGameItem = new RewardGameItem(appRequest.getPackagename(), todayAppUsage.get(appRequest.getPackagename()).getTotalTimeInForeground()+"", false, false);
//                mGameList.put(appRequest.getPackagename(), rewardGameItem);
            }
        }


    }


    // 패키지 정보만 사용 (테스트용)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gameAppPackageFilltering(List<AppGamePackageBody> gameList){

        // 오늘 실행된 앱 목록 (앱 전체)
        Map<String, UsageStats> todayAppUsage = AppUsageStatManager.getTodayUsageStatList(mActivity);

        for(AppGamePackageBody appGamePackageBody : gameList){
            String gPackagename =  appGamePackageBody.getPackagename();
            RewardGameItem rewardGameItem;
            if(todayAppUsage.containsKey(gPackagename)){
                rewardGameItem = new RewardGameItem(gPackagename, todayAppUsage.get(gPackagename).getTotalTimeInForeground()+"", false, false);
//                mGameList.put(gPackagename, rewardGameItem);
            }
        }

        if(mGameList.size() > 0){
            mAdapter.setItemData(mGameList);
            mAdapter.notifyDataSetChanged();
        }else{
            // 데이터 없음
            Toast.makeText(mActivity, getString(R.string.not_play_game), Toast.LENGTH_SHORT).show();
        }

    }

    // 패키지 정보만 사용 (테스트용)
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getGamePackageAppList(){

        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        List<AppBody> appBodies = new ArrayList<AppBody>();

        for(ApplicationInfo app : mAppList){
            AppBody appBody = new AppBody();
            appBody.setPackagename(app.packageName);
            appBodies.add(appBody);
        }

        RequestGamePackageList requestGamePackageList = new RequestGamePackageList(mActivity, mId, appBodies);
        requestGamePackageList.post(new RequestGamePackageList.ApiGamePackageListListener() {

            @Override
            public void onSuccess(AppGamePackageListResponse item) {

                if(item.getBody() == null || item.getBody().size() == 0){
                    Toast.makeText(mActivity, getString(R.string.not_install_game), Toast.LENGTH_SHORT).show();
                }else{
                    List<AppGamePackageBody> installGames = item.getBody();
                    gameAppPackageFilltering(installGames);
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    // 패지키 게임시간 투데이 적용
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getTodayGameAppList(){

        // 사용자 id
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        // 폰에 설치된 앱리스트 전부 가져오기
        List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        // 오늘 실행된 앱 목록 (앱 전체)
        Map<String, UsageStats> todayAppUsage = AppUsageStatManager.getTodayUsageStatList(mActivity);

        // 서버에 전송할 앱리스트값 셋팅
        List<AppRequest> appRequestList = new ArrayList<AppRequest>();
        for(ApplicationInfo app : mAppList){
            AppRequest appRequest = new AppRequest();
            appRequest.setPackagename(app.packageName);
            // 앱 사용시간 셋팅
            if(todayAppUsage.containsKey(app.packageName)){
                appRequest.setPlaytime(todayAppUsage.get(app.packageName).getTotalTimeInForeground()+"");
//                appRequest.setPlaytime(5000000+"");
            }
            appRequest.setPlaydate(mTodaytime);
            appRequestList.add(appRequest);
        }

        RequestTodayGameList requestTodayGameList = new RequestTodayGameList(mActivity, mId, appRequestList);
        requestTodayGameList.post(new RequestTodayGameList.ApiTodayGameListListener() {
            @Override
            public void onSuccess(AppGameListResponse item) {

                if(item.getBody() == null || item.getBody().size() == 0){
                    mRecyclerView.setVisibility(View.GONE);
                    empty_data.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    empty_data.setVisibility(View.GONE);
                    List<AppGameBody> installGames = item.getBody();
                    gameAppFilltering(installGames);
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gameAppFilltering(List<AppGameBody> gameList){

        int isGetRewardCnt = 0;

        // 리스트에 보낼 데이터 셋팅
        for(AppGameBody appGameBody : gameList){
            String gPackagename =  appGameBody.getPackagename();
            if(!appGameBody.getReward().equals("0") ){
                ++isGetRewardCnt;
            }

            if(!appGameBody.getPlaytime().equals("0")){
                mGameList.put(gPackagename, appGameBody);
            }
        }

        Log.d("mithril", "isGetRewardCnt = " + isGetRewardCnt );

        if(mGameList.size() > 0){
            mRecyclerView.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            mAdapter.setItemData(mGameList);
            mAdapter.notifyDataSetChanged();
        }else{
            mRecyclerView.setVisibility(View.GONE);
            empty_data.setVisibility(View.VISIBLE);
        }

        try{
            tv_today_reward_info.setText(String.format(getString(R.string.today_reward_remain), (3-isGetRewardCnt) + ""));
            setRewardcnt(isGetRewardCnt);
        }catch (NumberFormatException e){

        };

    }

    // 게임리워드 받기
    private void getGameReward(AppGameBody appGameBody){

        // 사용자 id
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        RequestGameRewardOrder requestGameRewardOrder = new RequestGameRewardOrder(mActivity, mId, appGameBody);
        requestGameRewardOrder.post(new RequestGameRewardOrder.ApiGameRewardOrderListener() {
            @Override
            public void onSuccess(AppGamedataRewardResponse item) {

                if(item.getBody() != null){
                    String mPackagename = item.getBody().getPackagename();
                    AppGameBody gameBody = item.getBody();
                    mGameList.put(mPackagename,gameBody);

                    mAdapter.setItemData(mGameList);
                    mAdapter.notifyDataSetChanged();

                    int isGetRewardCnt = 0;
                    String[] mKeys = mGameList.keySet().toArray(new String[mGameList.size()]);
                    for(int i = 0 ; i < mKeys.length ; i++){
                        AppGameBody appGameBody = mGameList.get(mKeys[i]);
                        if(!appGameBody.getReward().equals("0") ){
                            ++isGetRewardCnt;
                        }
                    }

                    Log.d("mithril", "isGetRewardCnt = " + isGetRewardCnt );

                    try{
                        tv_today_reward_info.setText(String.format(getString(R.string.today_reward_remain), (3-isGetRewardCnt) + ""));
                        setRewardcnt(isGetRewardCnt);
                    }catch (NumberFormatException e){

                    };

                }

            }

            @Override
            public void onFail() {

            }
        });

    }

    private String getTodayTime() {
        String todayTime;
        Calendar calendar = Calendar.getInstance();
        todayTime = dateFormat.format(calendar.getTimeInMillis());
        Log.d("mithril", "todayTime:"+ todayTime);
        return todayTime;
    }

    public interface GameRewardCallListener {
        void onGameReward(AppGameBody item);
    }

}

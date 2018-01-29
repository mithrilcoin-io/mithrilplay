package io.mithrilcoin.mithrilplay.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Log;
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

    int isGetRewardCnt = 0;
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

        mTodaytime = TimeUtil.getTodayUtcTime();

        long startTime = System.currentTimeMillis();

        getTodayGameAppList();

        long endTime = System.currentTimeMillis();

        // Total time
        long lTime = endTime - startTime;
        Log.e("mithril", "today game GET TIME : " + lTime + "(ms)");

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



    // 패지키 게임시간 투데이 적용
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getTodayGameAppList(){

        // 이메일 인증 완료 시간
        String mAuthDate = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_DATE);
        long emailAuthTime = 0;
        if(!TextUtils.isEmpty(mAuthDate)){
            emailAuthTime = Long.parseLong(mAuthDate);
        }

        // 사용자 id
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        // 폰에 설치된 앱리스트 전부 가져오기
        List<ApplicationInfo> mAppList = new ArrayList<ApplicationInfo>();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        // 오늘 실행된 앱 목록 (앱 전체)
        Map<String, UsageStats> todayAppUsage = AppUsageStatManager.getTodayUsageStatList(mActivity);

        Map<String,String> saveData = new HashMap<String,String>();

        String validDate = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_APP_EMAILAUTH_BEFORE_DATA_date);

        if(!TextUtils.isEmpty(validDate) && validDate.equals(TimeUtil.getTodayString())){
            saveData = MithrilPreferences.loadMap(mActivity, MithrilPreferences.TAG_APP_EMAILAUTH_BEFORE_DATA);
            if(saveData != null && saveData.size() > 0){
                String[] mKeys = saveData.keySet().toArray(new String[saveData.size()]);
                for(int i = 0 ; i < mKeys.length ; ++i ){
                    Log.d("mithril", "key = " + mKeys[i] + ", value =" + saveData.get(mKeys[i]));
                }
            }
        }else{
            saveData.clear();
            MithrilPreferences.putString(mActivity, MithrilPreferences.TAG_APP_EMAILAUTH_BEFORE_DATA_date, "");
            MithrilPreferences.saveMap(mActivity, MithrilPreferences.TAG_APP_EMAILAUTH_BEFORE_DATA, saveData);
        }

        ArrayList<EventVO> list = new ArrayList<EventVO>();
        HashMap<String, Long> eventTime = new HashMap<String, Long>();

        // 이벤트로 앱별 사용시간 가져오기.
        UsageStatsManager usm = (UsageStatsManager) mActivity.getSystemService("usagestats");
        UsageEvents uEvents = usm.queryEvents(AppUsageStatManager.getStartTime(), System.currentTimeMillis());
        while (uEvents.hasNextEvent()){
            UsageEvents.Event event = new UsageEvents.Event();
            uEvents.getNextEvent(event);
            if (event != null){
                Log.d("mithriltime", "Event: " + event.getPackageName() + "__" +  event.getTimeStamp() + "__" +  event.getEventType() );
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

        // 서버에 전송할 앱리스트값 셋팅
        List<AppRequest> appRequestList = new ArrayList<AppRequest>();
        for(ApplicationInfo app : mAppList){
            AppRequest appRequest = new AppRequest();
            appRequest.setPackagename(app.packageName);
            appRequest.setPlaydate(mTodaytime);

            // 앱 사용시간 셋팅
            if(todayAppUsage.containsKey(app.packageName)){
//                if(saveData != null && saveData.size() > 0){
//                    long cTime = todayAppUsage.get(app.packageName).getTotalTimeInForeground();
//                    if(saveData.containsKey(app.packageName)){
//                        appRequest.setPlaytime((cTime - Long.parseLong(saveData.get(app.packageName))) + "");
//                    }else{
//                        if(eventTime.containsKey(app.packageName)){
//                            appRequest.setPlaytime(eventTime.get(app.packageName).toString());
//                        }else{
//                            appRequest.setPlaytime(cTime +"");
//                        }
//                    }
//                }else{

                    if(eventTime.containsKey(app.packageName)){
                        Log.d("mithriltime", "Event 수정됨: " + app.packageName + "__" +  eventTime.get(app.packageName).toString() );
                        appRequest.setPlaytime(eventTime.get(app.packageName).toString());
                    }else{
                        appRequest.setPlaytime(todayAppUsage.get(app.packageName).getTotalTimeInForeground()+"");
                    }

//                }

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


/*          sample target game

            if(app.packageName.equals("com.ketchapp.rider") && todayAppUsage.containsKey("com.ketchapp.rider")) {
                Log.e("mithriltime", "Pkg: " + todayAppUsage.get(app.packageName).getPackageName() + "\n"
                        + "getFirstTimeStamp: " + todayAppUsage.get(app.packageName).getFirstTimeStamp() + "\n"
                        + "getLastTimeStamp: " + todayAppUsage.get(app.packageName).getLastTimeStamp()  + "\n"
                        + "getTotalTimeInForeground: " + todayAppUsage.get(app.packageName).getTotalTimeInForeground() + "\n"
                        + "게임시간 : " + getTime(todayAppUsage.get(app.packageName).getTotalTimeInForeground())  + "\n"
                        + "getLastTimeUsed: " + todayAppUsage.get(app.packageName).getLastTimeUsed()
                );
            }
*/

        }

        RequestTodayGameList requestTodayGameList = new RequestTodayGameList(mActivity, mId, appRequestList);
        requestTodayGameList.post(new RequestTodayGameList.ApiTodayGameListListener() {
            @Override
            public void onSuccess(AppGameListResponse item) {

                if(item.getBody() == null || item.getBody().size() == 0){
                    mRecyclerView.setVisibility(View.GONE);
                    empty_data.setVisibility(View.VISIBLE);

                    try{
                        tv_today_reward_info.setText(String.format(getString(R.string.today_reward_remain), (3-isGetRewardCnt) + ""));
                        setRewardcnt(isGetRewardCnt);
                    }catch (NumberFormatException e){

                    };

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

    private  HashMap<String, Long> sortList(ArrayList<EventVO> list){
        HashMap<String, ArrayList<EventVO>> maplist = new HashMap<>();
        HashMap<String, Long> maplist2 = new HashMap<>();
        AscendingObj abd = new AscendingObj();
        for(EventVO pack : list){
            if(pack.getPackagename().equals("io.mithrilcoin.mithrilplay")){
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gameAppFilltering(List<AppGameBody> gameList){

        isGetRewardCnt = 0;
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

        setRewardcnt(isGetRewardCnt);

    }

    // 게임리워드 받기
    private void getGameReward(AppGameBody appGameBody){

        // 사용자 id
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

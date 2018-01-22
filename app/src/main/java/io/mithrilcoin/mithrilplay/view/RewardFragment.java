package io.mithrilcoin.mithrilplay.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.mithrilcoin.mithrilplay.IntroActivity;
import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Log;
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
    private LinkedHashMap<String, RewardGameItem> mGameList = new LinkedHashMap<String, RewardGameItem>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reward, container, false);

        mActivity = (Activity) getActivity();

        setLayout(rootView);

        getData();

        return rootView;
    }

    private void setLayout(View v) {

        tv_today_reward_info = (TextView) v.findViewById(R.id.tv_today_reward_info);

        tv_today_reward_one = (ImageView) v.findViewById(R.id.tv_today_reward_one);
        tv_today_reward_two = (ImageView) v.findViewById(R.id.tv_today_reward_two);
        tv_today_reward_three = (ImageView) v.findViewById(R.id.tv_today_reward_three);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_today_reward);
        empty_data = (TextView) v.findViewById(R.id.empty_data);

    }


    // 임시 데이터 생성
    @SuppressLint("WrongConstant")
    private void getData(){

        mGameList.clear();

        // 설치된 어플리케이션 목록 가져오기
        pm = mActivity.getPackageManager();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);
        Log.d("mithril", "mAppList.size() = " + mAppList.size());
        for(ApplicationInfo app : mAppList){
//			Log.d("mithril", "packageName = " + app.packageName);
            RewardGameItem rewardGameItem = new RewardGameItem(app.loadIcon(pm), app.loadLabel(pm).toString(), "3분", app.packageName, false, false);
            mGameList.put(app.packageName, rewardGameItem);
        }

        // 임시 셋팅

        String[] mKeys = mGameList.keySet().toArray(new String[mGameList.size()]);

        mGameList.get(mKeys[0]).setmRewardIsValid(true);
        mGameList.get(mKeys[1]).setmRewardIsValid(true);
        mGameList.get(mKeys[2]).setmRewardIsValid(true);

        mGameList.get(mKeys[0]).setmRewardOn(true);

        setAdapter();

        tv_today_reward_info.setText(String.format(getString(R.string.today_reward_remain),"2"));
        setRewardcnt(1);

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
        mAdapter = new RewardAdapter(mActivity, mGameList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

}

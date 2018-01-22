package io.mithrilcoin.mithrilplay.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.view.adapter.RewardAdapter;
import io.mithrilcoin.mithrilplay.view.adapter.RewardHistoryAdapter;
import io.mithrilcoin.mithrilplay.view.adapter.RewardHistoryData;


public class RewardHistoryFragment extends Fragment {

    public RewardHistoryFragment() {
    }

    private HomeActivity mActivity = null;
    private RewardHistoryAdapter mAdapter = null;
    private LinearLayoutManager mLayoutManager = null;

    private TextView total_mtp = null;
    private RecyclerView mRecyclerView = null;

    private ArrayList<RewardHistoryData> historyDataList = new ArrayList<RewardHistoryData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reward_history, container, false);

        mActivity = (HomeActivity) getActivity();

        setLayout(rootView);

        getData();

        return rootView;
    }


    private void setLayout(View v) {

        total_mtp = (TextView) v.findViewById(R.id.total_mtp);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_reward_history);
        mRecyclerView.setHasFixedSize(true);

    }


    // 임시 데이터 생성
    private void getData(){
        historyDataList.clear();

        historyDataList.add(new RewardHistoryData("라이더", "100", 2018, 1, 2));
        historyDataList.add(new RewardHistoryData("쿠키런", "500", 2018, 1, 2));
        historyDataList.add(new RewardHistoryData("라이더", "200", 2018, 1, 2));
        historyDataList.add(new RewardHistoryData("쿠키런", "999", 2017, 12, 5));
        historyDataList.add(new RewardHistoryData("리니지", "10", 2017, 12, 5));
        historyDataList.add(new RewardHistoryData("gta", "102", 2017, 11, 2));
        historyDataList.add(new RewardHistoryData("모두의 마블", "130", 2017, 10, 6));
        historyDataList.add(new RewardHistoryData("다함께 차차차", "30", 2017, 9, 16));
        historyDataList.add(new RewardHistoryData("라이더", "100", 2017, 8, 10));
        historyDataList.add(new RewardHistoryData("쿠키런", "500", 2017, 8, 10));
        historyDataList.add(new RewardHistoryData("라이더", "200", 2017, 8, 10));
        historyDataList.add(new RewardHistoryData("쿠키런", "999", 2017, 7, 19));
        historyDataList.add(new RewardHistoryData("리니지", "10", 2017, 6, 18));
        historyDataList.add(new RewardHistoryData("gta", "102", 2017, 5, 10));
        historyDataList.add(new RewardHistoryData("모두의 마블", "130", 2017, 5, 10));
        historyDataList.add(new RewardHistoryData("다함께 차차차", "30", 2017, 4, 5));

        setAdapter();

        total_mtp.setText("1350 MTP");

    }

    private void setAdapter(){

        mLayoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new RewardHistoryAdapter(historyDataList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RewardHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showNotInputValueDialog(mAdapter.getItem(position).getAppName(), mAdapter.getItem(position).getTimeToString() + ""
                        , "11", mAdapter.getItem(position).getRewardMtp());

            }
        });

    }

    private void showNotInputValueDialog(String appname, String rewardTime, String playTime, String mtp){

        mActivity.showDialogOneButton(
                getString(R.string.reward_detail),
                String.format(getString(R.string.reward_detail_comment) ,appname ,rewardTime ,playTime ,mtp),
                getString(R.string.ok),
                new CommonDialog.CommonDialogListener() {
            @Override
            public void onConfirm() {


            }

            @Override
            public void onCancel() {

            }
        });

    }




}

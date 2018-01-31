package io.mithrilcoin.mithrilplay.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.common.TimeUtil;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.network.RequestTotalRewardGetList;
import io.mithrilcoin.mithrilplay.network.vo.GameRewardGet;
import io.mithrilcoin.mithrilplay.network.vo.GameRewardTotalListResponse;
import io.mithrilcoin.mithrilplay.view.adapter.RewardHistoryAdapter;
import io.mithrilcoin.mithrilplay.view.adapter.RewardHistoryData;

/**
 *  Get Reward history
 */
public class RewardHistoryFragment extends Fragment {

    public RewardHistoryFragment() {
    }

    public static RewardHistoryFragment instance = null;

    private HomeActivity mActivity = null;
    private RewardHistoryAdapter mAdapter = null;
    private LinearLayoutManager mLayoutManager = null;
    private TextView total_mtp = null;
    private RecyclerView mRecyclerView = null;
    private RelativeLayout mEmptyRewardHistory = null;

    private ArrayList<RewardHistoryData> historyDataList = new ArrayList<RewardHistoryData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reward_history, container, false);

        instance = RewardHistoryFragment.this;
        mActivity = (HomeActivity) getActivity();

        setLayout(rootView);

        return rootView;
    }


    private void setLayout(View v) {

        total_mtp = (TextView) v.findViewById(R.id.total_mtp);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_reward_history);
        mRecyclerView.setHasFixedSize(true);
        mEmptyRewardHistory = (RelativeLayout) v.findViewById(R.id.empty_reward_history);

        setAdapter();

    }

    public void onResume() {
        super.onResume();

        getGameRewardTotalList();

    }

    private void setAdapter(){

        mLayoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new RewardHistoryAdapter(mActivity, historyDataList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RewardHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if(mAdapter.getItem(position).getTypeCode().equals(Constant.REWARD_TYPE_GAME)){

                    showNotInputValueDialog(mAdapter.getItem(position).getAppName(), mAdapter.getItem(position).getTimeToString() + ""
                            , mAdapter.getItem(position).getRewardMtp(), (TimeUtil.getTime(mActivity, Long.parseLong(mAdapter.getItem(position).getPlayTime())))
                            , mAdapter.getItem(position).getTypeCode());

                }else if(mAdapter.getItem(position).getTypeCode().equals(Constant.REWARD_TYPE_INFO_ADD)){

                    showNotInputValueDialog(getString(R.string.reward_userinfo_add), mAdapter.getItem(position).getTimeToString() + ""
                            , mAdapter.getItem(position).getRewardMtp(), "", mAdapter.getItem(position).getTypeCode());

                }

            }
        });

    }

    private void showNotInputValueDialog(String appname, String rewardTime, String mtp, String playTime, String type){

        mActivity.showDialogOneButton(
                appname,
                (type.equals(Constant.REWARD_TYPE_GAME))
                        ? String.format(getString(R.string.reward_detail_comment_game),rewardTime, mtp, playTime)
                        : String.format(getString(R.string.reward_detail_comment_info),rewardTime, mtp),
                getString(R.string.confirm),
                new CommonDialog.CommonDialogListener() {
            @Override
            public void onConfirm() {


            }

            @Override
            public void onCancel() {

            }
        });

    }

    // Get Full Reward list
    private void getGameRewardTotalList(){

        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);
        RequestTotalRewardGetList requestTotalRewardGetList = new RequestTotalRewardGetList(mActivity, mId);
        requestTotalRewardGetList.post(new RequestTotalRewardGetList.ApiGameRewardTotalListener() {
            @Override
            public void onSuccess(GameRewardTotalListResponse item) {

                if(item.getBody() == null || item.getBody().size() == 0){
                    if(item.getUserInfo() != null && item.getUserInfo().getMtptotal() != null){
                        total_mtp.setText(String.format(getString(R.string.total_mtp), item.getUserInfo().getMtptotal().getIncomeamount()));
                    }
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyRewardHistory.setVisibility(View.VISIBLE);
                }else{

                    List<GameRewardGet> gameRewardGets = item.getBody();
//                    Log.d("mithril", "gameRewardGets =" + gameRewardGets.size() );

                    historyDataList.clear();
                    for(GameRewardGet rewardGet : gameRewardGets ){
                        long rTime = Long.parseLong(rewardGet.getRegistdate());
                        historyDataList.add(new RewardHistoryData(rewardGet.getPackagename(), rewardGet.getAlttitle(),
                                rewardGet.getReward(), rewardGet.getPlaytime(), rewardGet.getTypecode(), rTime));
                    }

                    mAdapter.setItemData(historyDataList);
                    mAdapter.notifyDataSetChanged();
                    if(item.getUserInfo() != null && item.getUserInfo().getMtptotal() != null) {
                        total_mtp.setText(String.format(getString(R.string.total_mtp), item.getUserInfo().getMtptotal().getIncomeamount()));
                    }
                    if(gameRewardGets.size() > 0){
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyRewardHistory.setVisibility(View.GONE);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyRewardHistory.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFail() {

            }
        });

    }

}

package io.mithrilcoin.mithrilplay.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.TimeUtil;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.view.RewardFragment.GameRewardCallListener;

/**
 * Today's Rewards Adapter
 */
public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ItemViewHolder> {

    private Activity mActivity = null;
    private LinkedHashMap<String, AppGameBody> mItems = null;
    private String[] mKeys;
    private PackageManager pm;
    private GameRewardCallListener mRewardListener;
    private String mValidTime = "";

    @SuppressLint("WrongConstant")
    public RewardAdapter(Activity activity, LinkedHashMap<String, AppGameBody> items, GameRewardCallListener gameRewardCallListener){
        this.mActivity = activity;
        this.mItems = items;
        this.mRewardListener = gameRewardCallListener;
        mKeys = items.keySet().toArray(new String[items.size()]);

        pm = mActivity.getPackageManager();
    }

    public void setItemData(LinkedHashMap<String, AppGameBody> item){
        this.mItems = item;
        mKeys = mItems.keySet().toArray(new String[mItems.size()]);
    }

    public void setValidTime(long vTime){
        this.mValidTime = TimeUtil.getTime(mActivity, vTime);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_today_reward,parent,false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        String key = mKeys[position];
        AppGameBody Value = mItems.get(key);

        String packgeName = Value.getPackagename();
        String AppName ="";

        try {
            holder.iv_game_icon.setImageDrawable(pm.getApplicationIcon(packgeName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            AppName = (String) pm.getApplicationLabel(pm.getApplicationInfo(packgeName, PackageManager.GET_UNINSTALLED_PACKAGES));
            if(TextUtils.isEmpty(AppName)){
                AppName = Value.getTitle();
            }
            holder.tv_game_title.setText(AppName);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        holder.tv_play_time.setText(TimeUtil.getTime(mActivity, Long.parseLong(Value.getPlaytime())));

        holder.reward_layout.setVisibility(View.VISIBLE);
        if(!Value.getReward().equals("0")){
            // Reward received
            holder.tv_today_reward.setVisibility(View.VISIBLE);
            holder.tv_today_reward.setText(String.format(mActivity.getString(R.string.get_reward_ok), Value.getReward()));
            holder.btn_reward_mtp.setVisibility(View.GONE);
            holder.iv_rewarded.setVisibility(View.VISIBLE);

        }else if(Value.getValid().equals("true")){
            // Rewards can be requested _ before receiving the reward
            holder.tv_today_reward.setVisibility(View.VISIBLE);
            holder.tv_today_reward.setText(String.format(mActivity.getString(R.string.get_reward_ready),mValidTime));
            holder.btn_reward_mtp.setVisibility(View.VISIBLE);
            holder.iv_rewarded.setVisibility(View.GONE);
        }else{
            // Games played today
            holder.reward_layout.setVisibility(View.GONE);
            holder.tv_today_reward.setVisibility(View.GONE);
            holder.btn_reward_mtp.setVisibility(View.GONE);
            holder.iv_rewarded.setVisibility(View.GONE);
        }


        String finalAppName = AppName;
        holder.btn_reward_mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Value.setAlttitle(finalAppName);
                mRewardListener.onGameReward(Value);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_game_icon;
        private TextView tv_game_title;
        private TextView tv_play_time;
        private Button btn_reward_mtp;
        private TextView tv_today_reward;
        private ImageView iv_rewarded;
        private RelativeLayout reward_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_game_icon = (ImageView) itemView.findViewById(R.id.iv_game_icon);
            tv_game_title = (TextView) itemView.findViewById(R.id.tv_game_title);
            tv_play_time = (TextView) itemView.findViewById(R.id.tv_play_time);
            btn_reward_mtp = (Button) itemView.findViewById(R.id.btn_reward_mtp);
            tv_today_reward = (TextView) itemView.findViewById(R.id.tv_today_reward);
            iv_rewarded = (ImageView) itemView.findViewById(R.id.iv_rewarded);
            reward_layout = (RelativeLayout) itemView.findViewById(R.id.reward_layout);
        }

    }

}

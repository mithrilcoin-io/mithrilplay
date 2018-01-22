package io.mithrilcoin.mithrilplay.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import io.mithrilcoin.mithrilplay.R;

/**
 */

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ItemViewHolder> {

    private Activity mActivity = null;
    private LinkedHashMap<String, RewardGameItem> mItems = null;
    private String[] mKeys;

    public RewardAdapter(Activity activity, LinkedHashMap<String, RewardGameItem> items){
        mActivity = activity;
        mItems = items;
        mKeys = items.keySet().toArray(new String[items.size()]);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_today_reward,parent,false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        String key = mKeys[position];
        RewardGameItem Value = mItems.get(key);

        holder.iv_game_icon.setImageDrawable(Value.getmIcon());
        holder.tv_game_title.setText(Value.getmAppName());
        holder.tv_play_time.setText(Value.getmPlayTime());

        if(Value.getmRewardIsValid() && Value.getmRewardOn()){
            // 3위 순위안에 들고 리워드 받음
            holder.tv_today_reward.setVisibility(View.VISIBLE);
            holder.btn_reward_mtp.setVisibility(View.GONE);
            holder.iv_rewarded.setVisibility(View.VISIBLE);

        }else if(Value.getmRewardIsValid() && !Value.getmRewardOn()){
            // 3위순위 안에 들고 리워드 안받음
            holder.tv_today_reward.setVisibility(View.VISIBLE);
            holder.btn_reward_mtp.setVisibility(View.VISIBLE);
            holder.iv_rewarded.setVisibility(View.GONE);
        }else{
            // 3위권 밖 기타 게임
            holder.tv_today_reward.setVisibility(View.GONE);
            holder.btn_reward_mtp.setVisibility(View.GONE);
            holder.iv_rewarded.setVisibility(View.GONE);
        }

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

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_game_icon = (ImageView) itemView.findViewById(R.id.iv_game_icon);
            tv_game_title = (TextView) itemView.findViewById(R.id.tv_game_title);
            tv_play_time = (TextView) itemView.findViewById(R.id.tv_play_time);
            btn_reward_mtp = (Button) itemView.findViewById(R.id.btn_reward_mtp);
            tv_today_reward = (TextView) itemView.findViewById(R.id.tv_today_reward);
            iv_rewarded = (ImageView) itemView.findViewById(R.id.iv_rewarded);
        }

    }

}

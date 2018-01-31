package io.mithrilcoin.mithrilplay.view.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;

/**
 */
public class RewardHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private ArrayList<RewardHistoryItem> itemList;
    private OnItemClickListener listener;
    private PackageManager pm;

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_list_header;

        public TimeViewHolder(View v) {
            super(v);
            tv_list_header = (TextView) v.findViewById(R.id.tv_list_header);
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv_game_icon, iv_mithril_icon;
        public TextView tv_reward_title, tv_reward_mtp;

        private OnViewHolderClickListener listener;

        public DataViewHolder(View v, OnViewHolderClickListener listener) {
            super(v);
            iv_game_icon = (ImageView) v.findViewById(R.id.iv_game_icon);
            iv_mithril_icon = (ImageView) v.findViewById(R.id.iv_mithril_icon);
            tv_reward_title = (TextView) v.findViewById(R.id.tv_reward_title);
            tv_reward_mtp = (TextView) v.findViewById(R.id.tv_reward_mtp);
            v.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(listener != null)
                listener.onViewHolderClick(getPosition());
        }

        private interface OnViewHolderClickListener {
            void onViewHolderClick(int position);
        }
    }

    public RewardHistoryAdapter(Activity activity, ArrayList<RewardHistoryData> dataset) {
        this.mActivity = activity;
        this.itemList = initItemList(dataset);

        pm = mActivity.getPackageManager();
    }

    private ArrayList<RewardHistoryItem> initItemList(ArrayList<RewardHistoryData> dataset) {

        ArrayList<RewardHistoryItem> result = new ArrayList<>();

        int year = 0, month = 0, dayOfMonth = 0;
        for(RewardHistoryData data:dataset) {
            if(year != data.getYear() || month != data.getMonth() || dayOfMonth != data.getDayOfMonth()) {
                result.add(new HistoryTimeTag(data.getYear(), data.getMonth(), data.getDayOfMonth()));
                year = data.getYear();
                month = data.getMonth();
                dayOfMonth = data.getDayOfMonth();
            }
            result.add(data);
        }

        return result;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    public void setItemData(ArrayList<RewardHistoryData> item){
        this.itemList = initItemList(item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RewardHistoryItem.TYPE_TIME)
            return new TimeViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_reward_history_time, parent, false));
        else
            return new DataViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_reward_history_data, parent, false),
                    new DataViewHolder.OnViewHolderClickListener() {
                        @Override
                        public void onViewHolderClick(int position) {
                            if(listener != null)
                                listener.onItemClick(position);
                        }
                    }
            );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TimeViewHolder) {
            TimeViewHolder tHolder = (TimeViewHolder) holder;
            tHolder.tv_list_header.setText(itemList.get(position).getTimeToString());
        } else {
            DataViewHolder dHolder = (DataViewHolder) holder;
            String packgeName = ((RewardHistoryData)itemList.get(position)).getPackageName();

            if(((RewardHistoryData) itemList.get(position)).getTypeCode().equals(Constant.REWARD_TYPE_INFO_ADD)){
                dHolder.iv_mithril_icon.setVisibility(View.VISIBLE);
                dHolder.iv_game_icon.setVisibility(View.GONE);
                dHolder.tv_reward_title.setText(mActivity.getString(R.string.reward_userinfo_add));
            }else if(((RewardHistoryData) itemList.get(position)).getTypeCode().equals(Constant.REWARD_TYPE_GAME)){
                dHolder.iv_mithril_icon.setVisibility(View.GONE);
                dHolder.iv_game_icon.setVisibility(View.VISIBLE);
                try {
                    dHolder.iv_game_icon.setImageDrawable(pm.getApplicationIcon(packgeName));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    dHolder.iv_mithril_icon.setVisibility(View.VISIBLE);
                    dHolder.iv_game_icon.setVisibility(View.GONE);
                }
                dHolder.tv_reward_title.setText(((RewardHistoryData)itemList.get(position)).getAppName());
            }
            dHolder.tv_reward_mtp.setText( String.format(mActivity.getString(R.string.total_mtp), ((RewardHistoryData)itemList.get(position)).getRewardMtp()) );
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public RewardHistoryData getItem(int position) {
        return (RewardHistoryData)itemList.get(position);
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
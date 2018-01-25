package io.mithrilcoin.mithrilplay.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.view.RewardFragment.GameRewardCallListener;

/**
 */

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ItemViewHolder> {

    private Activity mActivity = null;
    private LinkedHashMap<String, AppGameBody> mItems = null;
    private String[] mKeys;
    private PackageManager pm;
    private List<ApplicationInfo> mAppList = null;
    private GameRewardCallListener mRewardListener;



    @SuppressLint("WrongConstant")
    public RewardAdapter(Activity activity, LinkedHashMap<String, AppGameBody> items, GameRewardCallListener gameRewardCallListener){
        this.mActivity = activity;
        this.mItems = items;
        this.mRewardListener = gameRewardCallListener;
        mKeys = items.keySet().toArray(new String[items.size()]);

        pm = mActivity.getPackageManager();
        mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);
    }

    public void setItemData(LinkedHashMap<String, AppGameBody> item){
        this.mItems = item;
        mKeys = mItems.keySet().toArray(new String[mItems.size()]);
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

        try {
            holder.iv_game_icon.setImageDrawable(pm.getApplicationIcon(packgeName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String name = (String) pm.getApplicationLabel(pm.getApplicationInfo(packgeName, PackageManager.GET_UNINSTALLED_PACKAGES));
            holder.tv_game_title.setText(name);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        holder.tv_play_time.setText(getTime(Long.parseLong(Value.getPlaytime())));

        if(!Value.getReward().equals("0")){
            // 리워드 받음
            holder.tv_today_reward.setVisibility(View.INVISIBLE);
            holder.btn_reward_mtp.setVisibility(View.GONE);
            holder.iv_rewarded.setVisibility(View.VISIBLE);

        }else if(Value.getValid().equals("true")){
            // 리워드 요청가능_리워드 받기전
            holder.tv_today_reward.setVisibility(View.VISIBLE);
            holder.btn_reward_mtp.setVisibility(View.VISIBLE);
            holder.iv_rewarded.setVisibility(View.GONE);
        }else{
            // 기타 게임
            holder.tv_today_reward.setVisibility(View.GONE);
            holder.btn_reward_mtp.setVisibility(View.GONE);
            holder.iv_rewarded.setVisibility(View.GONE);
        }

        holder.btn_reward_mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRewardListener.onGameReward(Value);
            }
        });

        holder.iv_game_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(mActivity, packgeName);
            }
        });

    }

    public boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getTime(long sTime){
        String time = "";
        int hours   = (int) ((sTime / (1000*60*60)) % 24);  //시
        int minutes = (int) ((sTime / (1000*60)) % 60);     //분
        int seconds = (int) (sTime / 1000) % 60 ;           //초

        if(hours != 0){
            time += hours + "시간 ";
        }
        if(minutes != 0){
            time += minutes + "분 ";
        }
        if(seconds != 0){
            time += seconds + "초";
        }
//        Log.d("mithril", "게임시간 :" + time);
        return time;
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
        private ImageView iv_game_play;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_game_icon = (ImageView) itemView.findViewById(R.id.iv_game_icon);
            tv_game_title = (TextView) itemView.findViewById(R.id.tv_game_title);
            tv_play_time = (TextView) itemView.findViewById(R.id.tv_play_time);
            btn_reward_mtp = (Button) itemView.findViewById(R.id.btn_reward_mtp);
            tv_today_reward = (TextView) itemView.findViewById(R.id.tv_today_reward);
            iv_rewarded = (ImageView) itemView.findViewById(R.id.iv_rewarded);
            iv_game_play = (ImageView) itemView.findViewById(R.id.iv_game_play);
        }

    }

}

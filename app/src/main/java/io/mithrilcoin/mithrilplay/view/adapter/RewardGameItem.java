package io.mithrilcoin.mithrilplay.view.adapter;

import android.graphics.drawable.Drawable;

/**
 *
 */

public class RewardGameItem {

    public Drawable mIcon = null;   // 아이콘
    public String mAppName = null;  // 앱이름
    public String mPlayTime = null;     // 게임플레이 시간
    public String mAppPackge = null;    // 앱패키지명
    public Boolean mRewardOn = false;   // 리워드 받기 실행 여부
    public Boolean mRewardIsValid = false;  // 리워드 받기 가능 여부

    public RewardGameItem(Drawable mIcon, String mAppName, String mPlayTime, String mAppPackge, Boolean mRewardOn, Boolean mRewardIsValid) {
        this.mIcon = mIcon;
        this.mAppName = mAppName;
        this.mPlayTime = mPlayTime;
        this.mAppPackge = mAppPackge;
        this.mRewardOn = mRewardOn;
        this.mRewardIsValid = mRewardIsValid;
    }

    public Drawable getmIcon() {
        return mIcon;
    }

    public void setmIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getmPlayTime() {
        return mPlayTime;
    }

    public void setmPlayTime(String mPlayTime) {
        this.mPlayTime = mPlayTime;
    }

    public String getmAppPackge() {
        return mAppPackge;
    }

    public void setmAppPackge(String mAppPackge) {
        this.mAppPackge = mAppPackge;
    }

    public Boolean getmRewardOn() {
        return mRewardOn;
    }

    public void setmRewardOn(Boolean mRewardOn) {
        this.mRewardOn = mRewardOn;
    }

    public Boolean getmRewardIsValid() {
        return mRewardIsValid;
    }

    public void setmRewardIsValid(Boolean mRewardIsValid) {
        this.mRewardIsValid = mRewardIsValid;
    }
}

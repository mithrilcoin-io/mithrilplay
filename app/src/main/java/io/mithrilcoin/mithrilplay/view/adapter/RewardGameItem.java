package io.mithrilcoin.mithrilplay.view.adapter;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

/**
 *
 */

public class RewardGameItem {

    public String mAppPackge = null;    // 앱패키지명
    public String mPlayTime = null;     // 게임플레이 시간
    public Boolean mRewardOn = false;   // 리워드 받기 실행 여부
    public Boolean mRewardIsValid = false;  // 리워드 받기 가능 여부

    public RewardGameItem(String mAppPackge, String mPlayTime, Boolean mRewardOn, Boolean mRewardIsValid) {
        this.mAppPackge = mAppPackge;
        this.mPlayTime = mPlayTime;
        this.mRewardOn = mRewardOn;
        this.mRewardIsValid = mRewardIsValid;
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

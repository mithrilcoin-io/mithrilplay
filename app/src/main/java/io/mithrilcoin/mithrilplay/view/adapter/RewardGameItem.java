package io.mithrilcoin.mithrilplay.view.adapter;

/**
 *
 */

public class RewardGameItem {

    public String mAppPackge = null;
    public String mPlayTime = null;
    public Boolean mRewardOn = false;
    public Boolean mRewardIsValid = false;

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

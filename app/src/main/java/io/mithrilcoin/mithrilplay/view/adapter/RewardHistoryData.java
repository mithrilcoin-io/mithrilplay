package io.mithrilcoin.mithrilplay.view.adapter;

/**
 */
public class RewardHistoryData extends RewardHistoryItem {

    private String packageName;
    private String appName;
    private String rewardMtp;
    private String playTime;

    public RewardHistoryData(String packageName, String appName, String rewardMtp, String playTime, long rewardGetTime) {
        super(rewardGetTime);
        this.packageName = packageName;
        this.appName = appName;
        this.rewardMtp = rewardMtp;
        this.playTime = playTime;
    }

    public RewardHistoryData(String packageName, String appName, String rewardMtp, String playTime, int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
        this.packageName = packageName;
        this.appName = appName;
        this.rewardMtp = rewardMtp;
        this.playTime = playTime;
}

    @Override
    public int getType() {
        return TYPE_DATA;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getRewardMtp() {
        return rewardMtp;
    }

    public void setRewardMtp(String rewardMtp) {
        this.rewardMtp = rewardMtp;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }
}

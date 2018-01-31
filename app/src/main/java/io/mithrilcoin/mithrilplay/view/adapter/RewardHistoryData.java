package io.mithrilcoin.mithrilplay.view.adapter;

/**
 */
public class RewardHistoryData extends RewardHistoryItem {

    private String packageName;
    private String appName;
    private String rewardMtp;
    private String playTime;
    private String typeCode;

    public RewardHistoryData(String packageName, String appName, String rewardMtp, String playTime, String typeCode, long rewardGetTime) {
        super(rewardGetTime);
        this.packageName = packageName;
        this.appName = appName;
        this.rewardMtp = rewardMtp;
        this.playTime = playTime;
        this.typeCode = typeCode;
    }

    public RewardHistoryData(String packageName, String appName, String rewardMtp, String playTime, String typeCode, int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
        this.packageName = packageName;
        this.appName = appName;
        this.rewardMtp = rewardMtp;
        this.playTime = playTime;
        this.typeCode = typeCode;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

}

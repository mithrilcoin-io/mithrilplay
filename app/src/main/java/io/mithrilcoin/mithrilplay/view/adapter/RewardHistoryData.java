package io.mithrilcoin.mithrilplay.view.adapter;

/**
 */
public class RewardHistoryData extends RewardHistoryItem {

    private String appName;
    private String rewardMtp;

    public RewardHistoryData(String name, String mtp, long time) {
        super(time);
        this.appName = name;
        this.rewardMtp = mtp;
    }

    public RewardHistoryData(String name, String mtp, int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
        this.appName = name;
        this.rewardMtp = mtp;
    }

    @Override
    public int getType() {
        return TYPE_DATA;
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
}

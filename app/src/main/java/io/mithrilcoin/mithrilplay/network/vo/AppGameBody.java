package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppGameBody {

    @SerializedName("packagename")
    @Expose
    private String packagename;
    @SerializedName("playtime")
    @Expose
    private String playtime;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("playdate")
    @Expose
    private String playdate;
    @SerializedName("txnumber")
    @Expose
    private String txnumber;
    @SerializedName("valid")
    @Expose
    private String valid;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("idx")
    @Expose
    private String idx;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaydate() {
        return playdate;
    }

    public void setPlaydate(String playdate) {
        this.playdate = playdate;
    }

    public String getTxnumber() {
        return txnumber;
    }

    public void setTxnumber(String txnumber) {
        this.txnumber = txnumber;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

}

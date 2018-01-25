package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppRequest {

    @SerializedName("packagename")
    @Expose
    private String packagename;
    @SerializedName("playtime")
    @Expose
    private String playtime;
    @SerializedName("playdate")
    @Expose
    private String playdate;

    public AppRequest() {
    }

    public AppRequest(String packagename, String playtime, String playdate) {
        this.packagename = packagename;
        this.playtime = playtime;
        this.playdate = playdate;
    }

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

    public String getPlaydate() {
        return playdate;
    }

    public void setPlaydate(String playdate) {
        this.playdate = playdate;
    }

}

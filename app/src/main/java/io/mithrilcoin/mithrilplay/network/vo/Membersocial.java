package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Membersocial {

    @SerializedName("snscode")
    @Expose
    private String snscode;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;

    public String getSnscode() {
        return snscode;
    }

    public void setSnscode(String snscode) {
        this.snscode = snscode;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }
}

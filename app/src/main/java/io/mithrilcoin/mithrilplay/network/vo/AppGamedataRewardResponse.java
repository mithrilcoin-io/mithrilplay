package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppGamedataRewardResponse {

    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("body")
    @Expose
    private AppGameBody body;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public AppGameBody getBody() {
        return body;
    }

    public void setBody(AppGameBody body) {
        this.body = body;
    }

}

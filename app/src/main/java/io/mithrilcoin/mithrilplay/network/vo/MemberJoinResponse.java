package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberJoinResponse {

    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("body")
    @Expose
    private String body;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}

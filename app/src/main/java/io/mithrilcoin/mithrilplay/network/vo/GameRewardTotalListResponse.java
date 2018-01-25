package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameRewardTotalListResponse {

    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("body")
    @Expose
    private List<GameRewardGet> body = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<GameRewardGet> getBody() {
        return body;
    }

    public void setBody(List<GameRewardGet> body) {
        this.body = body;
    }

}

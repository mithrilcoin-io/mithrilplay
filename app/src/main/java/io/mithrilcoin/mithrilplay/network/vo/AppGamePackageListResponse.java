package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppGamePackageListResponse {

    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("body")
    @Expose
    private List<AppGamePackageBody> body = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<AppGamePackageBody> getBody() {
        return body;
    }

    public void setBody(List<AppGamePackageBody> body) {
        this.body = body;
    }

}

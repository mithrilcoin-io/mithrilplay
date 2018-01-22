package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("deviceid")
    @Expose
    private String deviceid;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("memberDetail")
    @Expose
    private MemberDetail memberDetail;
    @SerializedName("recentLoginTime")
    @Expose
    private String recentLoginTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public MemberDetail getMemberDetail() {
        return memberDetail;
    }

    public void setMemberDetail(MemberDetail memberDetail) {
        this.memberDetail = memberDetail;
    }

    public String getRecentLoginTime() {
        return recentLoginTime;
    }

    public void setRecentLoginTime(String recentLoginTime) {
        this.recentLoginTime = recentLoginTime;
    }

}

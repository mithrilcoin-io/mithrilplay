package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("deviceid")
    @Expose
    private String deviceid;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("fcmid")
    @Expose
    private String fcmid;
    @SerializedName("osversion")
    @Expose
    private String osversion;
    @SerializedName("membersocial")
    @Expose
    private Membersocial membersocial;

    public LoginRequest(String email, String password, String deviceid, String model, String brand, String fcmid, String osversion) {
        this.email = email;
        this.password = password;
        this.deviceid = deviceid;
        this.model = model;
        this.brand = brand;
        this.fcmid = fcmid;
        this.osversion = osversion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFcmid() {
        return fcmid;
    }

    public void setFcmid(String fcmid) {
        this.fcmid = fcmid;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public Membersocial getMembersocial() {
        return membersocial;
    }

    public void setMembersocial(Membersocial membersocial) {
        this.membersocial = membersocial;
    }

}

package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberDetail {

    @SerializedName("rnum")
    @Expose
    private String rnum;
    @SerializedName("idx")
    @Expose
    private int idx;
    @SerializedName("member_idx")
    @Expose
    private int memberIdx;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birthyear")
    @Expose
    private String birthyear;
    @SerializedName("birthmonth")
    @Expose
    private String birthmonth;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("registdate")
    @Expose
    private String registdate;
    @SerializedName("modifydate")
    @Expose
    private String modifydate;

    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getMemberIdx() {
        return memberIdx;
    }

    public void setMemberIdx(int memberIdx) {
        this.memberIdx = memberIdx;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(String birthyear) {
        this.birthyear = birthyear;
    }

    public String getBirthmonth() {
        return birthmonth;
    }

    public void setBirthmonth(String birthmonth) {
        this.birthmonth = birthmonth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegistdate() {
        return registdate;
    }

    public void setRegistdate(String registdate) {
        this.registdate = registdate;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

}

package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberUpdateRequest {

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

    public MemberUpdateRequest(String gender, String birthyear, String birthmonth, String country, String city) {
        this.gender = gender;
        this.birthyear = birthyear;
        this.birthmonth = birthmonth;
        this.country = country;
        this.city = city;
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

}

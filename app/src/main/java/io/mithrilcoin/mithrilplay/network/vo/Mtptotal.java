package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mtptotal {

    @SerializedName("member_idx")
    @Expose
    private String memberIdx;
    @SerializedName("incomeamount")
    @Expose
    private String incomeamount;
    @SerializedName("usableamount")
    @Expose
    private String usableamount;
    @SerializedName("spentamount")
    @Expose
    private String spentamount;
    @SerializedName("expireamount")
    @Expose
    private String expireamount;

    public String getMemberIdx() {
        return memberIdx;
    }

    public void setMemberIdx(String memberIdx) {
        this.memberIdx = memberIdx;
    }

    public String getIncomeamount() {
        return incomeamount;
    }

    public void setIncomeamount(String incomeamount) {
        this.incomeamount = incomeamount;
    }

    public String getUsableamount() {
        return usableamount;
    }

    public void setUsableamount(String usableamount) {
        this.usableamount = usableamount;
    }

    public String getSpentamount() {
        return spentamount;
    }

    public void setSpentamount(String spentamount) {
        this.spentamount = spentamount;
    }

    public String getExpireamount() {
        return expireamount;
    }

    public void setExpireamount(String expireamount) {
        this.expireamount = expireamount;
    }

}

package io.mithrilcoin.mithrilplay.network.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

import io.mithrilcoin.mithrilplay.view.adapter.RewardHistoryItem;

public class GameRewardGet {

    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("pageNum")
    @Expose
    private String pageNum;
    @SerializedName("pageSize")
    @Expose
    private String pageSize;
    @SerializedName("limitNum")
    @Expose
    private String limitNum;
    @SerializedName("totalCount")
    @Expose
    private String totalCount;
    @SerializedName("sortName")
    @Expose
    private String sortName;
    @SerializedName("sortOrder")
    @Expose
    private String sortOrder;
    @SerializedName("searchKey")
    @Expose
    private String searchKey;
    @SerializedName("searchValue")
    @Expose
    private String searchValue;
    @SerializedName("idx")
    @Expose
    private String idx;
    @SerializedName("member_idx")
    @Expose
    private String memberIdx;
    @SerializedName("mtp_idx")
    @Expose
    private String mtpIdx;
    @SerializedName("packagename")
    @Expose
    private String packagename;
    @SerializedName("txnumber")
    @Expose
    private String txnumber;
    @SerializedName("typecode")
    @Expose
    private String typecode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("modify_member_id")
    @Expose
    private String modifyMemberId;
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("playtime")
    @Expose
    private String playtime;
    @SerializedName("modifydate")
    @Expose
    private String modifydate;
    @SerializedName("registdate")
    @Expose
    private String registdate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(String limitNum) {
        this.limitNum = limitNum;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getMemberIdx() {
        return memberIdx;
    }

    public void setMemberIdx(String memberIdx) {
        this.memberIdx = memberIdx;
    }

    public String getMtpIdx() {
        return mtpIdx;
    }

    public void setMtpIdx(String mtpIdx) {
        this.mtpIdx = mtpIdx;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getTxnumber() {
        return txnumber;
    }

    public void setTxnumber(String txnumber) {
        this.txnumber = txnumber;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getModifyMemberId() {
        return modifyMemberId;
    }

    public void setModifyMemberId(String modifyMemberId) {
        this.modifyMemberId = modifyMemberId;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getRegistdate() {
        return registdate;
    }

    public void setRegistdate(String registdate) {
        this.registdate = registdate;
    }

}

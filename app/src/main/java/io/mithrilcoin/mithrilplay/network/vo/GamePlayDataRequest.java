package io.mithrilcoin.mithrilplay.network.vo;

public class GamePlayDataRequest {

    private String packagename;
    private String starttime;
    private String endtime;
    private String alttitle;
    private String version;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAlttitle() {
        return alttitle;
    }

    public void setAlttitle(String alttitle) {
        this.alttitle = alttitle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

package io.mithrilcoin.mithrilplay.network.eosobj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccount {

    private String email;
    private String registdate;
    private String authdate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistdate() {
        return registdate;
    }

    public void setRegistdate(String registdate) {
        this.registdate = registdate;
    }

    public String getAuthdate() {
        return authdate;
    }

    public void setAuthdate(String authdate) {
        this.authdate = authdate;
    }
}

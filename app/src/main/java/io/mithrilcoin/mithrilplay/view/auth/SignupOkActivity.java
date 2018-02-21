package io.mithrilcoin.mithrilplay.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.eosobj.DeviceInfo;
import io.mithrilcoin.mithrilplay.network.eosobj.UserAccount;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 *  Sign Up Complete
 */
public class SignupOkActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_ok);

        findViewById(R.id.btn_more_info).setOnClickListener(v -> {

            // Go to more info
            Intent intent = new Intent(SignupOkActivity.this, MoreInfoActivity.class);
            startActivityForResult(intent, Constant.REQUEST_JOIN_MOREINFO);

        });

        findViewById(R.id.btn_skip).setOnClickListener(v -> {

            for (int i = 0; i < activityList.size(); i++) {
                activityList.get(i).finish();
            }
            launchHomeScreen();

        });

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(MithrilPreferences.getString(this, MithrilPreferences.TAG_EMAIL));
        userAccount.setRegistdate(MithrilPreferences.getString(this, MithrilPreferences.TAG_REGIST_DATE));
        userAccount.setAuthdate(MithrilPreferences.getString(this, MithrilPreferences.TAG_AUTH_DATE));
        // TODO: EOS SmartContract _ (1. account info) When you sign up and verify your email
        setEosAccount(userAccount);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setEmail(MithrilPreferences.getString(this, MithrilPreferences.TAG_EMAIL));
        deviceInfo.setModel(MithrilPreferences.getString(this, MithrilPreferences.TAG_MODEL));
        deviceInfo.setBrand(MithrilPreferences.getString(this, MithrilPreferences.TAG_BRAND));
        deviceInfo.setOsversion(MithrilPreferences.getString(this, MithrilPreferences.TAG_OS_VERSION));
        // TODO: EOS SmartContract _ (3. device info) When you sign up and verify your email
        setEosDeviceInfo(deviceInfo);
    }

    private void setEosAccount(UserAccount userAccount){



    }

    private void setEosDeviceInfo(DeviceInfo deviceInfo){



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == Constant.REQUEST_JOIN_MOREINFO){

            for (int i = 0; i < activityList.size(); i++) {
                activityList.get(i).finish();
            }
            launchHomeScreen();

            return;
        }

    }



}

package io.mithrilcoin.mithrilplay.db;

import android.content.Context;
import android.os.Build;

import io.mithrilcoin.mithrilplay.common.CommonApplication;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.db.entity.Device;
import io.mithrilcoin.mithrilplay.db.entity.UserData;
import io.mithrilcoin.mithrilplay.network.vo.UserInfo;

/**
 * DBdataAccess
 */
public class DBdataAccess {

    public static final String TAG = "mithril";
    public static Context mContext;

    public static void memberDataDBaccess(UserInfo item, String email) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                // user info
                UserData userData = new UserData();

                userData.email = email;

                if(item != null && item.getRegistdate() != null){
                    userData.registdate = item.getRegistdate();
                }

                if(item.getAuthdate() != null){
                    userData.authdate = item.getAuthdate();
                }

                if(item.getMemberDetail() != null && item.getMemberDetail().getModifydate() != null){
                    userData.modifydate = item.getMemberDetail().getModifydate();
                }

                if(item.getMemberDetail() != null && item.getMemberDetail().getGender() != null){
                    userData.gender = item.getMemberDetail().getGender();
                }

                if(item.getMemberDetail() != null && item.getMemberDetail().getBirthyear() != null){
                    userData.birthyear = item.getMemberDetail().getBirthyear();
                }

                if(item.getMemberDetail() != null && item.getMemberDetail().getBirthmonth() != null){
                    userData.birthmonth = item.getMemberDetail().getBirthmonth();
                }

                if(item.getMemberDetail() != null && item.getMemberDetail().getCountry() != null){
                    userData.country = item.getMemberDetail().getCountry();
                }

                if(item.getMemberDetail() != null && item.getMemberDetail().getCity() != null){
                    userData.city = item.getMemberDetail().getCity();
                }

                int isUserData = CommonApplication.getApplication().getDB().userDataDao().checkUserdata(userData.email
                        , userData.registdate, userData.authdate, userData.modifydate, userData.gender, userData.birthyear
                        , userData.birthmonth, userData.country, userData.city);
                if(isUserData == 0){
                    Log.d(TAG,"userdata insert" );
                    CommonApplication.getApplication().getDB().userDataDao().insert(userData);
                }else{
                    Log.d(TAG,"userdata update" );
                    CommonApplication.getApplication().getDB().userDataDao().update(userData);
                }

                // device info
                int mDeviceList = CommonApplication.getApplication().getDB().deviceDao().checkEmailOsver(Build.VERSION.RELEASE, email);
                if(mDeviceList == 0){
                    Log.d(TAG,"Device insert" );
                    Device device = new Device(email, Build.MODEL, Build.BRAND, "y", Build.VERSION.RELEASE);
                    CommonApplication.getApplication().getDB().deviceDao().insert(device);
                }else{
                    Log.d(TAG,"Device update" );
                    Device device = new Device(email, Build.MODEL, Build.BRAND, "y", Build.VERSION.RELEASE);
                    CommonApplication.getApplication().getDB().deviceDao().update(device);
                }

            }
        }).start();

    }


}

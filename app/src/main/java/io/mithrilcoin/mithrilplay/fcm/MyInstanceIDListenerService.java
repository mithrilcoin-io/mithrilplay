package io.mithrilcoin.mithrilplay.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("mithril","MyInstanceIDListenerService fcm : " + refreshedToken);
        MithrilPreferences.putString(getApplication(), MithrilPreferences.TAG_PUSH_ID, refreshedToken);
    }
}
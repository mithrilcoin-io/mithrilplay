package io.mithrilcoin.mithrilplay.view;

import android.content.Intent;
import android.os.Bundle;

import io.mithrilcoin.mithrilplay.IntroActivity;
import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.fcm.FCMVo;

/**
 */
public class AlterDialogActivity extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            final FCMVo fcmVo = (FCMVo)(getIntent().getSerializableExtra(FCMVo.FCM_PUSH_INTENT_KEY));

            showDialogTwoButton(fcmVo.getFcmPushTitle(), fcmVo.getFcmPushMessage(), getString(R.string.cancel), getString(R.string.confirm), new CommonDialog.CommonDialogListener() {
                @Override
                public void onConfirm() {
                    Intent intent = new Intent(AlterDialogActivity.this, IntroActivity.class);
                    intent.putExtra(fcmVo.FCM_PUSH_INTENT_KEY, fcmVo);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {
                    finish();
                }
            });

        }catch (Exception e){
            finish();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            final FCMVo fcmVo = (FCMVo) (intent.getSerializableExtra(FCMVo.FCM_PUSH_INTENT_KEY));

            showDialogTwoButton(fcmVo.getFcmPushTitle(), fcmVo.getFcmPushMessage(), getString(R.string.cancel), getString(R.string.confirm), new CommonDialog.CommonDialogListener() {
                @Override
                public void onConfirm() {
                    Intent intent = new Intent(AlterDialogActivity.this, IntroActivity.class);
                    intent.putExtra(fcmVo.FCM_PUSH_INTENT_KEY, fcmVo);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {

                }
            });

        } catch (Exception e) {
            finish();
        }


    }
}
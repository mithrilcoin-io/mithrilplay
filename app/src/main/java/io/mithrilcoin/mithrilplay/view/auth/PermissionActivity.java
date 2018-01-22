package io.mithrilcoin.mithrilplay.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.view.ActivityBase;


public class PermissionActivity extends ActivityBase {

    private Button btnPermissionOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        btnPermissionOk = (Button) findViewById(R.id.btn_permission_ok);

        btnPermissionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 임시 허용
//                MithrilPreferences.putBoolean(PermissionActivity.this, MithrilPreferences.TAG_PERMISSION, true);
                launchLoginScreen();
            }
        });
    }

    private void launchLoginScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(PermissionActivity.this, LoginActivity.class));
        finish();

    }
}

package io.mithrilcoin.mithrilplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;


public class PermissionActivity extends BaseActivity {

    private Button btnPermissionOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        btnPermissionOk = (Button) findViewById(R.id.btn_permission_ok);

        btnPermissionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MithrilPreferences.putBoolean(PermissionActivity.this, MithrilPreferences.TAG_PERMISSION, true);
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

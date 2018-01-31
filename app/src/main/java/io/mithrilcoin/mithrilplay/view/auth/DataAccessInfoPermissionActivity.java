package io.mithrilcoin.mithrilplay.view.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 * requests permission to access usage informatiom
 */
public class DataAccessInfoPermissionActivity extends ActivityBase {

    private Button btn_set_data_access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_access_info);

        btn_set_data_access = (Button) findViewById(R.id.btn_set_data_access);
        btn_set_data_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),  Constant.REQUEST_PACKAGE_USAGE_STATS);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constant.REQUEST_PACKAGE_USAGE_STATS:

                if (hasPermission()) {
                    setResult(RESULT_OK);
                    finish();
                }

                break;
        }
    }


}

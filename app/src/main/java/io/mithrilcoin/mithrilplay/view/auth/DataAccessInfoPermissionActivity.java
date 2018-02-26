package io.mithrilcoin.mithrilplay.view.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 * requests permission to access usage informatiom
 */
public class DataAccessInfoPermissionActivity extends ActivityBase {

    private Activity mActivity;
    private Button btn_set_data_access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_access_info);

        mActivity = DataAccessInfoPermissionActivity.this;

        btn_set_data_access = (Button) findViewById(R.id.btn_set_data_access);
        btn_set_data_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),  Constant.REQUEST_PACKAGE_USAGE_STATS);

            }
        });


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(mActivity, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }

        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle(R.string.permissions_memory_order_title)
                .setRationaleMessage(R.string.permissions_memory_order)
                .setDeniedTitle(R.string.permissions_memory_deny_title)
                .setDeniedMessage(R.string.permissions_memory_deny)
                .setGotoSettingButtonText("Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

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

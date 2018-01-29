package io.mithrilcoin.mithrilplay.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.network.RequestLogout;
import io.mithrilcoin.mithrilplay.network.RequestUserInfo;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.auth.MoreInfoActivity;

public class SettingActivity extends ActivityBase  {

    private Activity mActivity = null;

    private TextView tv_email, tv_mtp, tv_more_info, logout;
    private RelativeLayout layout_more_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mActivity = SettingActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewInit();
    }

    private void viewInit(){

        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_mtp = (TextView) findViewById(R.id.tv_mtp);
        tv_more_info = (TextView) findViewById(R.id.tv_more_info);

        layout_more_info = (RelativeLayout) findViewById(R.id.layout_more_info);
        layout_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // �߰����� �Է����� �̵�
                Intent intent = new Intent(mActivity, MoreInfoActivity.class);
                startActivityForResult(intent, Constant.REQUEST_SETTING_MOREINFO);

            }
        });

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCall();
            }
        });

        // ��������
        String email = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_EMAIL);
        tv_email.setText(email);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserinfo();

    }

    private void logOutCall(){

        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);
        RequestLogout requestLogout = new RequestLogout(mActivity,mId);

        requestLogout.post(new RequestLogout.ApiLogoutResultListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item.getBody().getCode().equals("SUCCESS")){
                    Log.d("mithril","�α׾ƿ� ����" );
                    logout();
                    for (int i = 0; i < activityList.size(); i++) {
                        activityList.get(i).finish();
                    }
                    launchLoginScreen();
                }

            }

            @Override
            public void onFail() {

            }
        });

    }

    private void getUserinfo(){
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);

        RequestUserInfo requestUserInfo = new RequestUserInfo(mActivity, mId);
        requestUserInfo.post(new RequestUserInfo.ApiGetUserinfoListener() {
            @Override
            public void onSuccess(MemberResponse item) {

                if(item == null || item.getUserInfo() == null){
                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!item.getBody().getCode().equals("SUCCESS")){
                    return;
                }

                if(item.getUserInfo().getState().equals(Constant.USER_STATUS_NOT_AUTH)){ // ������

                    Toast.makeText(mActivity, item.getBody().getCode(), Toast.LENGTH_SHORT).show();

                }else if(item.getUserInfo().getState().equals(Constant.USER_STATUS_AUTH_ON)){  // ����
                    Log.d("mithril", "�̸��� �������� �Ϸ�");

                    layout_more_info.setVisibility(View.VISIBLE);
                    tv_more_info.setVisibility(View.GONE);

                }else if(item.getUserInfo().getState().equals(Constant.USER_AUTH_PLUS_PROFILE)){  // �߰����� �Է¿Ϸ�
                    Log.d("mithril", "�߰����� �Է¿Ϸ�");

                    // ����� �߰� ����
                    layout_more_info.setVisibility(View.GONE);
                    tv_more_info.setVisibility(View.VISIBLE);

                    // "���� / 1979.12 / ���ѹα�"
                    if(item.getUserInfo().getMemberDetail() != null){
                        tv_more_info.setText(String.format(getString(R.string.more_info_set),
                                item.getUserInfo().getMemberDetail().getGender(),
                                item.getUserInfo().getMemberDetail().getBirthyear(),
                                item.getUserInfo().getMemberDetail().getBirthmonth(),
                                item.getUserInfo().getMemberDetail().getCountry()));
                    }

                }else{


                }

                if(item.getUserInfo().getMtptotal() != null && !TextUtils.isEmpty(item.getUserInfo().getMtptotal().getIncomeamount())){
                    tv_mtp.setText(String.format(getString(R.string.setting_mtp), item.getUserInfo().getMtptotal().getIncomeamount()));
                }else{
                    tv_mtp.setText(String.format(getString(R.string.setting_mtp), "0"));
                }

            }

            @Override
            public void onFail() {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("mithril", ": setting onActivityResult");

        if (resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == Constant.REQUEST_SETTING_MOREINFO){
            Log.d("mithril", ": REQUEST_SETTING_MOREINFO");

            getUserinfo();

            return;
        }

    }
}

package io.mithrilcoin.mithrilplay.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

// 회원가입 완료
public class SignupOkActivity extends ActivityBase {

    private Button btnMoreInfo, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_ok);

        btnMoreInfo = (Button) findViewById(R.id.btn_more_info);
        btnSkip = (Button) findViewById(R.id.btn_skip);

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 추가정보 입력으로 이동
                Intent intent = new Intent(SignupOkActivity.this, MoreInfoActivity.class);
                startActivityForResult(intent, Constant.REQUEST_JOIN_MOREINFO);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < activityList.size(); i++) {
                    activityList.get(i).finish();
                }
                launchHomeScreen();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("mithril", ": SignupOkActivity onActivityResult");

        if (resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == Constant.REQUEST_JOIN_MOREINFO){
            Log.d("mithril", ": REQUEST_JOIN_MOREINFO");
            for (int i = 0; i < activityList.size(); i++) {
                activityList.get(i).finish();
            }
            launchHomeScreen();

            return;
        }

    }



}

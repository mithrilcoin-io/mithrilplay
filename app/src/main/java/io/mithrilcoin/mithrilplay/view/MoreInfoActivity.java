package io.mithrilcoin.mithrilplay.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;

public class MoreInfoActivity extends AppCompatActivity {

    private Button btnGetBonusReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.more_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnGetBonusReward = (Button) findViewById(R.id.btn_get_bonus);

        btnGetBonusReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO HENRY 보너스리워드 다이얼로그를 띄워주세용.
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

// 추가정보 입력
public class MoreInfoActivity extends ActivityBase implements View.OnClickListener {

    private Activity mActivity;

    private Button btn_toggle_male, btn_toggle_female;
    private AppCompatSpinner sp_birth_year, sp_birth_month, sp_nation;

    private Button btnGetBonusReward;

    // data
    private String mGender = "";
    private String mBirthYear = "";
    private String mBirthMonth = "";
    private String mNation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        mActivity = MoreInfoActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.more_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewInit();



    }

    private void viewInit(){

        btn_toggle_male = (Button) findViewById(R.id.btn_toggle_male);
        btn_toggle_female = (Button) findViewById(R.id.btn_toggle_female);
        btn_toggle_male.setOnClickListener(this);
        btn_toggle_female.setOnClickListener(this);

        sp_birth_year = (AppCompatSpinner) findViewById(R.id.sp_birth_year);
        sp_birth_month = (AppCompatSpinner) findViewById(R.id.sp_birth_month);
        sp_nation = (AppCompatSpinner) findViewById(R.id.sp_nation);

        // 태어난 해
        ArrayList<String> years = new ArrayList<String>();
        years.add(getString(R.string.birth_year));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1900; i--) {
            years.add(i +"년");
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_birth_year.setAdapter(yearAdapter);

        // 태어난 달
        ArrayList<String> months = new ArrayList<String>();
        months.add(getString(R.string.month));
        for (int i = 1; i <= 12; i++) {
            months.add(i +"월");
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_birth_month.setAdapter(monthAdapter);


        // 국가정보
        ArrayList<String> nations = new ArrayList<String>();
        nations.add(getString(R.string.nation_sel));
//        Locale[] locales = Locale.getAvailableLocales();
//        for( int i=0; i < locales.length; i++ ){
////            Log.d(TAG, locales[i].getDisplayCountry() );
//            nations.add(locales[i].getDisplayCountry());
//        }

        String[] isoCodes = Locale.getISOCountries();
        for( int i=0; i < isoCodes.length; i++ ){
            Locale locale = new Locale( "ko", isoCodes[ i ] );
//            Log.d( TAG, locale.getDisplayName() );
            nations.add(locale.getDisplayName());
        }

        ArrayAdapter<String> nationAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, nations);
        nationAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_nation.setAdapter(nationAdapter);


        btnGetBonusReward = (Button) findViewById(R.id.btn_get_bonus);
        btnGetBonusReward.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_toggle_male:

                btn_toggle_male.setSelected(true);
                btn_toggle_female.setSelected(false);
                mGender = "male";

                break;

            case R.id.btn_toggle_female:

                btn_toggle_male.setSelected(false);
                btn_toggle_female.setSelected(true);
                mGender = "female";

                break;

            case R.id.btn_get_bonus:

                showRewardDoneDialog();


                break;

        }
    }

    private void showRewardDoneDialog(){

        showDialogOneButton(getString(R.string.congrats), getString(R.string.bonus), getString(R.string.login_go), new CommonDialog.CommonDialogListener() {
            @Override
            public void onConfirm() {


            }

            @Override
            public void onCancel() {

            }
        });

    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package io.mithrilcoin.mithrilplay.view.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 * 추가정보 입력
 */
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
        sp_birth_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mBirthYear = "";
                }else{
                    mBirthYear = parent.getItemAtPosition(position) + "";
                    mBirthYear = mBirthYear.replace("년","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 태어난 달
        ArrayList<String> months = new ArrayList<String>();
        months.add(getString(R.string.month));
        for (int i = 1; i <= 12; i++) {
            months.add(i +"월");
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_birth_month.setAdapter(monthAdapter);
        sp_birth_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mBirthMonth = "";
                }else{
                    mBirthMonth = parent.getItemAtPosition(position) + "";
                    mBirthMonth = mBirthMonth.replace("월","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 국가정보
        LinkedHashMap<String, Locale> mLocaleList = new LinkedHashMap<String, Locale>();
        ArrayList<String> nations = new ArrayList<String>();

        Locale[] availableLocales = Locale.getAvailableLocales();

        for(Locale locale : availableLocales){
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();
            String ename = locale.getDisplayCountry(Locale.ENGLISH);
            if(!TextUtils.isEmpty(code) && !TextUtils.isEmpty(name) && !mLocaleList.containsKey(name)){
                mLocaleList.put(name, locale);
                Log.d("mithril", code + "," + name + "," + ename);
            }
        }

        Iterator<String> iterator = mLocaleList.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            nations.add(key);
        }
        Collections.sort(nations, new Sortk());
        nations.add(0, getString(R.string.nation_sel));

        ArrayAdapter<String> nationAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, nations);
        nationAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_nation.setAdapter(nationAdapter);
        sp_nation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mNation = "";
                }else{
                    mNation = parent.getItemAtPosition(position) + "";
                    Log.d("mithril", "mNation =" + mNation + ", mNation.code =" + mLocaleList.get(mNation).getCountry());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

                if(TextUtils.isEmpty(mGender) || TextUtils.isEmpty(mBirthYear) || TextUtils.isEmpty(mBirthMonth) || TextUtils.isEmpty(mNation)) {
                    showNotInputValueDialog();
                    break;
                }


                showRewardDoneDialog();


                break;

        }
    }

    private void showNotInputValueDialog(){

        showDialogOneButton("","프로필 정보를 모두 선택하신 후 다시 시도하세요.", getString(R.string.confirm) , new CommonDialog.CommonDialogListener() {
            @Override
            public void onConfirm() {


            }

            @Override
            public void onCancel() {

            }
        });

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

    class Sortk implements Comparator<String>{
        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }
}

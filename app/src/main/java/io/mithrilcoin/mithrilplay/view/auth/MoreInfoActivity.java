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
import android.widget.Toast;

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
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.common.MithrilPreferences;
import io.mithrilcoin.mithrilplay.dialog.CommonDialog;
import io.mithrilcoin.mithrilplay.network.RequestMemberDetailUpdate;
import io.mithrilcoin.mithrilplay.network.RequestUserInfo;
import io.mithrilcoin.mithrilplay.network.eosobj.UserMoreInfo;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;

/**
 * Enter additional information
 */
public class MoreInfoActivity extends ActivityBase {

    private Activity mActivity;

    private AppCompatSpinner sp_birth_year, sp_birth_month, sp_nation;

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

        findViewById(R.id.btn_toggle_male).setOnClickListener(v -> {

            findViewById(R.id.btn_toggle_male).setSelected(true);
            findViewById(R.id.btn_toggle_female).setSelected(false);
            mGender = Constant.GENDER_MALE;

        });

        findViewById(R.id.btn_toggle_female).setOnClickListener(v -> {

            findViewById(R.id.btn_toggle_male).setSelected(false);
            findViewById(R.id.btn_toggle_female).setSelected(true);
            mGender = Constant.GENDER_FEMALE;

        });

        sp_birth_year = (AppCompatSpinner) findViewById(R.id.sp_birth_year);
        sp_birth_month = (AppCompatSpinner) findViewById(R.id.sp_birth_month);
        sp_nation = (AppCompatSpinner) findViewById(R.id.sp_nation);

        // birth year
        ArrayList<String> years = new ArrayList<String>();
        years.add(getString(R.string.birth_year));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1900; i--) {
            years.add(i+"");
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // birth month
        ArrayList<String> months = new ArrayList<String>();
        months.add(getString(R.string.month));
        for (int i = 1; i <= 12; i++) {
            months.add(i +"");
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // nation info
        LinkedHashMap<String, Locale> mLocaleList = new LinkedHashMap<String, Locale>();
        ArrayList<String> nations = new ArrayList<String>();

        Locale[] availableLocales = Locale.getAvailableLocales();

        for(Locale locale : availableLocales){
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();
            String ename = locale.getDisplayCountry(Locale.ENGLISH);
            if(!TextUtils.isEmpty(code) && !TextUtils.isEmpty(name) && !mLocaleList.containsKey(name)){
                mLocaleList.put(name, locale);
//                Log.d(TAG, code + "," + name + "," + ename);
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
                    mNation = parent.getItemAtPosition(position).toString();
                    mNation += "-" + mLocaleList.get(mNation).getCountry();
//                    Log.d(TAG, "mNation =" + mNation);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btn_get_bonus).setOnClickListener(v -> {

            if(TextUtils.isEmpty(mGender) || TextUtils.isEmpty(mBirthYear) || TextUtils.isEmpty(mBirthMonth) || TextUtils.isEmpty(mNation)) {
                showNotInputValueDialog();
            }else{
                setMemberDetailUpdate();
            }

        });

    }

    private void showNotInputValueDialog(){

        showDialogOneButton("",getString(R.string.alert_more_info_empty), getString(R.string.confirm) , new CommonDialog.CommonDialogListener() {
            @Override
            public void onConfirm() {
            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void showRewardDoneDialog(){

        showDialogOneButton(getString(R.string.congrats), getString(R.string.bonus), getString(R.string.confirm), new CommonDialog.CommonDialogListener() {
            @Override
            public void onConfirm() {
                setResult(RESULT_OK);
                finish();
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

    /**
     * Send additional information
     */
    private void setMemberDetailUpdate(){

        //TODO City information should be added later
        String mId = MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_AUTH_ID);
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(mGender, mBirthYear, mBirthMonth, mNation, "");

        RequestMemberDetailUpdate requestMemberDetailUpdate = new RequestMemberDetailUpdate(mActivity, mId, memberUpdateRequest);
        requestMemberDetailUpdate.post(new RequestMemberDetailUpdate.ApiMemberDetailUpdateListener() {
            @Override
            public void onSuccess(MemberUpdateResponse item) {
                if(item.getBody().getCode().equals(Constant.SUCCESS)){

                    if(item.getUserInfo() != null){

                        UserMoreInfo userMoreInfo = new UserMoreInfo();
                        userMoreInfo.setEmail(MithrilPreferences.getString(mActivity, MithrilPreferences.TAG_EMAIL));
                        userMoreInfo.setModifydate(item.getUserInfo().getMemberDetail().getModifydate());
                        userMoreInfo.setGender(mGender);
                        userMoreInfo.setBirthYear(mBirthYear);
                        userMoreInfo.setBirthMonth(mBirthMonth);
                        userMoreInfo.setCountry(mNation);
                        userMoreInfo.setCity("");

                        // TODO: EOS SmartContract _ (2.account more info) When you have completed the addition of member information
                        setEosMemberMoreInfo(userMoreInfo);

//                      showRewardDoneDialog();
                    }
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    private void setEosMemberMoreInfo(UserMoreInfo userMoreInfo){
        // TODO: Function "showRewardDoneDialog()" call when smartcontract completes



        showRewardDoneDialog();

    }


}

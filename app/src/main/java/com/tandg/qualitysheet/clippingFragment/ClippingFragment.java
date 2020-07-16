package com.tandg.qualitysheet.clippingFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import com.tandg.qualitysheet.R;
import com.tandg.qualitysheet.database.dataSource.QualityInfoDataSource;
import com.tandg.qualitysheet.di.DependencyInjector;
import com.tandg.qualitysheet.helper.ApplicationHelper;
import com.tandg.qualitysheet.listeners.DoneOnEditorActionListener;
import com.tandg.qualitysheet.listeners.ViewCallback;
import com.tandg.qualitysheet.model.QualityInfo;
import com.tandg.qualitysheet.model.SpinInfo;
import com.tandg.qualitysheet.qualitySheetActivity.QualitySheetActivity;
import com.tandg.qualitysheet.utils.AppContext;
import com.tandg.qualitysheet.utils.ApplicationUtils;
import com.tandg.qualitysheet.utils.BaseFragment;
import com.tandg.qualitysheet.webservice.Config;
import com.tandg.qualitysheet.webservice.SpreadsheetWebService;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClippingFragment extends BaseFragment<ClippingFragmentPresenter> implements ClippingFragmentContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = ClippingFragment.class.getSimpleName();

    @Inject
    QualityInfoDataSource qualityInfoDataSource;

    private AppCompatActivity mActivity;

    //@BindView(R.id.edt_worker_name)                   EditText edtWorkerName;
    @BindView(R.id.edt_row_number)                      EditText edtRowNumber;
    //@BindView(R.id.edt_comments)                        EditText edtComments;


    @BindView(R.id.spin_comments)                       Spinner spinComments;


    @BindView(R.id.input_row_number)                    TextInputLayout inputRowNumber;

    @BindView(R.id.rdg_clipping_data1)                  RadioGroup rdgClippingData1;
    @BindView(R.id.rdg_clipping_data2)                  RadioGroup rdgClippingData2;
    @BindView(R.id.rdg_clipping_data3)                  RadioGroup rdgClippingData3;
    @BindView(R.id.rdg_clipping_data4)                  RadioGroup rdgClippingData4;

    @BindView(R.id.radio_clipping_data1_yes)            LiquidRadioButton radioClippingData1yes;
    @BindView(R.id.radio_clipping_data1_no)             LiquidRadioButton radioClippingData1no;
    @BindView(R.id.radio_clipping_data2_yes)            LiquidRadioButton radioClippingData2yes;
    @BindView(R.id.radio_clipping_data2_no)             LiquidRadioButton radioClippingData2no;
    @BindView(R.id.radio_clipping_data3_yes)            LiquidRadioButton radioClippingData3yes;
    @BindView(R.id.radio_clipping_data3_no)             LiquidRadioButton radioClippingData3no;
    @BindView(R.id.radio_clipping_data4_yes)            LiquidRadioButton radioClippingData4yes;
    @BindView(R.id.radio_clipping_data4_no)             LiquidRadioButton radioClippingData4no;

    @BindView(R.id.btn_submit)                          Button btnSubmit;

    @BindView(R.id.linearLayoutDisplay)                 LinearLayout llDispaly;
    @BindView(R.id.txtQualityPercentage)                TextView txtQualityPercent;



    String                                              radioData1, radioData2, radioData3, radioData4;
    String                                              argJobName, argAuditorName, argHouseNumber, argWeekNumber, argWorkerName, argAdiCode, percentTest, comments;
    private String                                      spinnerWorkerName, spinnerAdiNumber, workerName1, adiNumber1, spinnerComments;
    private int                                         workerPosition, combinedPos;
    private QualityInfo                                 globalQualityInfo, qualityInfo;
    private ProgressDialog                              progressDialog;
    private int                                         count = 0;
    private int                                         percentCount = 25;
    List<QualityInfo>                                   qualityInfosList;
    int id;
    String strJobName, strAuditorName, strHouseNumber, strWeekNumber, strWorkerName, strAdiCode, strRowNumber, strData1, strData2, strData3, strData4, strData5, strData6, strData7, strData8, strComments, infoStatus, qualityPercent;
    ArrayList<String> WorkersName, ADICode;
    ArrayList<String> ssCombinedData, ssPercentage;
    boolean isVisited = false;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (AppCompatActivity) activity;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initResources();


    }

    private void initResources() {

        DependencyInjector.appComponent().inject(this);


        argJobName = getArguments().getString("txtJobName");
        argAuditorName = getArguments().getString("txtAuditorName");
        argHouseNumber = getArguments().getString("txtHouseNo");
        argWeekNumber = getArguments().getString("txtWeekNo");
        argWorkerName    = getArguments().getString("txtWorkerName");
        argAdiCode       = getArguments().getString("txtADICode");

        Log.e(TAG, "Data From Activity to Clipping fragment: " + argJobName + " " + argAuditorName + " " + argHouseNumber + " " + argWeekNumber+" "+argWorkerName+" "+argAdiCode);

        globalQualityInfo = new QualityInfo();
        qualityInfo       = new QualityInfo();
        WorkersName       = new ArrayList<>();
        ADICode           = new ArrayList<>();
        ssCombinedData    = new ArrayList<>();
        ssPercentage      = new ArrayList<>();


        edtRowNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        //edtComments.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //edtComments.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        rdgClippingData1.setOnCheckedChangeListener(this);
        rdgClippingData2.setOnCheckedChangeListener(this);
        rdgClippingData3.setOnCheckedChangeListener(this);
        rdgClippingData4.setOnCheckedChangeListener(this);

        spinComments.setOnItemSelectedListener(this);

        btnSubmit.setOnClickListener(this);

        /*Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.radio_button_animation);
        radioClippingData1yes.startAnimation(animation);
        radioClippingData1no.startAnimation(animation);*/

        if(ApplicationUtils.isConnected(mActivity)){

            getQualityPercentageFromSheet();

        }
        initSpinners();


    }

    private void displayPercentageData() {

        if(ApplicationUtils.isConnected(mActivity)){

            if(!ssCombinedData.isEmpty() && !ssPercentage.isEmpty()){

                String combined = argJobName+" "+argWorkerName;

                Log.e(TAG, "onItemSelected: "+combined );

                if(ssCombinedData.contains(combined)) {

                    llDispaly.setVisibility(View.VISIBLE);

                    combinedPos = getCategoryPosCombinedData(combined);


                    if(ssPercentage.get(combinedPos) != null){

                        //if(pos1 == pos2){

                        percentTest = ssPercentage.get(combinedPos);
                        txtQualityPercent.setText(percentTest + "%");

                    }else {

                        llDispaly.setVisibility(View.GONE);
                        txtQualityPercent.setText("");

                    }





                }else{

                    llDispaly.setVisibility(View.GONE);
                    txtQualityPercent.setText("");

                }


            }else {

                llDispaly.setVisibility(View.GONE);
                txtQualityPercent.setText("");


            }



        }else {

            qualityInfoDataSource.open();

            globalQualityInfo = qualityInfoDataSource.getQualityInfoByJobAndWorkerName(workerName1, argJobName);

            qualityInfoDataSource.close();

            if (globalQualityInfo != null && globalQualityInfo.getQualityPercent() != null) {

                llDispaly.setVisibility(View.VISIBLE);

                txtQualityPercent.setText(globalQualityInfo.getQualityPercent() + "%");


            } else {

                llDispaly.setVisibility(View.GONE);
                txtQualityPercent.setText("");


            }

        }
    }

    private int getCategoryPosCombinedData(String category) {
        return ssCombinedData.lastIndexOf(category);
    }
    private void getQualityPercentageFromSheet() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbxB6eqo6n7rPW1jzuGfJOxojLEqI_hfOMhcg3BCPc3ssnCrJ5o/exec?action=getGerData",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("items");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String Combined     =jsonObject1.getString("combinedData");
                                String Quality      =jsonObject1.getString("percent");

                                ssCombinedData.add(Combined);
                                ssPercentage.add(Quality);
                            }

                            displayPercentageData();


                        }catch (JSONException e){e.printStackTrace();}


                    }
                },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(mActivity);
        queue.add(stringRequest);
    }




    private void initSpinners() {

        /*List<String> arrayList = Arrays.asList(getResources().getStringArray(R.array.ger_adi));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity, R.layout.layout_spinner_label, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinAdiNumber.setAdapter(arrayAdapter);*/

        //------------------------------JOB_NAME----------------------------------------

        ArrayList<SpinInfo> stringArrayList = new ArrayList<>();
        stringArrayList.add(new SpinInfo(0, "SELECT"));
        stringArrayList.add(new SpinInfo(1, "Sent staff back to fix the issue"));
        stringArrayList.add(new SpinInfo(2, "Will check more of his/her rows"));
        stringArrayList.add(new SpinInfo(3, "Recurring issue, needs to be escalated"));
        stringArrayList.add(new SpinInfo(4, "Informal talk conducted"));


        ArrayAdapter<SpinInfo> adapter = new ArrayAdapter<SpinInfo>(mActivity, R.layout.layout_spinner_label, stringArrayList);
        adapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinComments.setAdapter(adapter);

        ApplicationUtils.hideKeypad(mActivity, spinComments);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isVisited )
        {
            isVisited = true;
        }
        else if(isVisited)
        {
            //setSpinner();
        }
    }

    @Override
    protected ClippingFragmentPresenter getPresenter() {
        return new ClippingFragmentPresenter(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_clipping;
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    //------------------------------------------VALIDATION-------------------------------------------------------------
    private void validateJobName() {

        if (argJobName != null && argJobName.trim().length() > 0) {

            qualityInfo.setJobName(argJobName);

            validateAuditorsName(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Select Job Name");

        }

    }

    private void validateAuditorsName(QualityInfo qualityInfo) {

        if (argAuditorName != null && argAuditorName.trim().length() > 0) {

            qualityInfo.setAuditorName(argAuditorName);

            validateHouseNumber(qualityInfo);

        } else {
            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Select Auditor's Name");

        }
    }

    private void validateHouseNumber(QualityInfo qualityInfo) {

        if (argHouseNumber != null && argHouseNumber.trim().length() > 0) {

            qualityInfo.setHouseNumber(argHouseNumber);

            validateWeekNumber(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Enter House Number");

        }

    }

    private void validateWeekNumber(QualityInfo qualityInfo) {

        if (argWeekNumber != null && argWeekNumber.trim().length() > 0) {

            qualityInfo.setWeekNumber(argWeekNumber);

            validateWorkerName(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Week Number Missing");

        }

    }

    private void validateWorkerName(QualityInfo qualityInfo) {

        if(argWorkerName != null && argWorkerName.trim().length() > 0){

            qualityInfo.setWorkerName(argWorkerName);

            validateAdiNumber(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Select Name of the Worker");

        }

    }
    private void validateAdiNumber(QualityInfo qualityInfo) {

        if (argAdiCode != null && argAdiCode.trim().length() > 0) {

            qualityInfo.setAdiCode(argAdiCode);
            validateRowNumber(qualityInfo);


        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "ADI Number Missing");
        }
    }

    private void validateRowNumber(QualityInfo qualityInfo) {

        String rowNumber = edtRowNumber.getText().toString().trim();

        if (rowNumber != null && rowNumber.trim().length() > 0) {

            inputRowNumber.setErrorEnabled(false);
            qualityInfo.setRowNumber(rowNumber);
            validateClippingData1(qualityInfo);


        } else {

            hideProgressDialog();
            inputRowNumber.setErrorEnabled(true);
            inputRowNumber.setError("Enter Row Number");
        }
    }

    private void validateClippingData1(QualityInfo qualityInfo) {

        if (radioData1 != null && radioData1.trim().length() > 0) {

            qualityInfo.setQualityData1(radioData1);

            validateClippingData2(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Choose One Option from Clip Position");

        }

    }

    private void validateClippingData2(QualityInfo qualityInfo) {

        if (radioData2 != null && radioData2.trim().length() > 0) {

            qualityInfo.setQualityData2(radioData2);

            validateClippingData3(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Choose One Option from Broken Heads and Trusses");

        }

    }

    private void validateClippingData3(QualityInfo qualityInfo) {

        if (radioData3 != null && radioData3.trim().length() > 0) {

            qualityInfo.setQualityData3(radioData3);

            validateClippingData4(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Choose One Option from Broken Heads Replaced and Bobbin Hung");

        }
    }

    private void validateClippingData4(QualityInfo qualityInfo) {

        if (radioData4 != null && radioData4.trim().length() > 0) {

            qualityInfo.setQualityData4(radioData4);

            validateClippingComments(qualityInfo);

        } else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity, "Choose One Option from Laterals");

        }


    }

    private void validateClippingComments(QualityInfo qualityInfo) {

        qualityInfo.setComments(spinnerComments);

        //String comments = edtComments.getText().toString().trim();

        calculatePercentage(qualityInfo);


    }

    private void calculatePercentage(QualityInfo qualityInfo) {

        if (radioData1.equalsIgnoreCase(QUALITY_YES)) {

            count = count + percentCount;

        }


        if (radioData2.equalsIgnoreCase(QUALITY_YES)) {

            count = count + percentCount;


        }

        if (radioData3.equalsIgnoreCase(QUALITY_YES)) {

            count = count + percentCount;


        }

        if (radioData4.equalsIgnoreCase(QUALITY_YES)) {

            count = count + percentCount;


        }

        qualityInfo.setQualityPercent(Integer.toString(count));
        Log.e(TAG, "calculatePercentage: " + "  " + count);
        saveDataToDB(qualityInfo);

        ApplicationUtils.showToast(mActivity, "Details added successfully");


    }


    //---------------------------------------VALIDATION COMPLETED--------------------------------------------------------

    private void saveDataToDB(QualityInfo qualityInfo) {

        qualityInfoDataSource.open();
        qualityInfo.setQualityInfoStaus(QUALITY_INFO_L);
        qualityInfoDataSource.createQualityInfo(qualityInfo);

        qualityInfoDataSource.close();
        hideProgressDialog();
        clearText();
        navigateToQualitySheetActivity();

        if (ApplicationUtils.isConnected(AppContext.getInstance())) {

            //if(!BuildConfig.DEBUG){
                new SendSpreadsheetClass().execute();
          //  }

        }

    }

    private class SendSpreadsheetClass extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                        Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                if(qualityInfoDataSource != null) {

                    qualityInfoDataSource.open();
                    qualityInfosList = qualityInfoDataSource.getAllQualityInfo();
                    qualityInfoDataSource.close();

                    for (QualityInfo info : qualityInfosList) {

                        id = info.getWorkerId();
                        strJobName = info.getJobName();
                        strAuditorName = info.getAuditorName();
                        strHouseNumber = info.getHouseNumber();
                        strWeekNumber = info.getWeekNumber();
                        strWorkerName = info.getWorkerName();
                        strAdiCode = info.getAdiCode();
                        strRowNumber = info.getRowNumber();
                        strData1 = info.getQualityData1();
                        strData2 = info.getQualityData2();
                        strData3 = info.getQualityData3();
                        strData4 = info.getQualityData4();
                        strData5 = "";
                        strData6 = "";
                        strData7 = "";
                        strData8 = "";
                        strComments = info.getComments();
                        infoStatus = info.getQualityInfoStaus();
                        qualityPercent = info.getQualityPercent();
                    }


                    final String jsonString = ApplicationUtils.toJson(qualityInfosList);

                    Log.e(TAG, "sendTheLogFile: " + infoStatus);

                    if (qualityInfosList != null && qualityInfosList.size() > 0 && infoStatus.equalsIgnoreCase(QUALITY_INFO_L)) {

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://docs.google.com/forms/u/0/d/e/")
                                .build();
                        final SpreadsheetWebService spreadsheetWebService = retrofit.create(SpreadsheetWebService.class);

                        Call<Void> completeQuestionnaireCall = spreadsheetWebService.completeQualityQuestionnaireV2(String.valueOf(id), strJobName, strAuditorName, strHouseNumber, strWeekNumber, strWorkerName, strAdiCode, strRowNumber, strData1, strData2, strData3, strData4, strData5, strData6, strData7, strData8, strComments, qualityPercent);
                        Log.e(TAG, "sendTheLogFile: " + id + " " + strJobName + " " + strAuditorName + " " + strHouseNumber + " " + strWeekNumber + " " + strWorkerName + " " + strAdiCode+ " " + strRowNumber + " " + strData1 + " " + strData2 + " " + strData3 + " " + strData4 + " " + strData5 + " " + strData6 + " " + strData7 + " " + strData8 + " " + strComments + " " + qualityPercent);
                        completeQuestionnaireCall.enqueue(callCallback);

                    }

                }

            } catch (Exception ex) {

                Log.i("Mail", "Failed" + ex);
            }

            return null;
        }
    }


    private final Callback<Void> callCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

            qualityInfoDataSource.open();

            for(QualityInfo info : qualityInfosList){
                info.setQualityInfoStaus(QUALITY_INFO_S);

                long updateId = qualityInfoDataSource.updateQualityInfo(info);
            }

            qualityInfoDataSource.close();
            Log.d(TAG, "Submitted. " + response);
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.e(TAG, "Failed", t);
            call.cancel();
        }
    };

    private void clearText() {


        edtRowNumber.setText("");
        //edtComments.setText("");
        spinComments.setSelection(0);
        rdgClippingData1.clearCheck();
        rdgClippingData2.clearCheck();
        rdgClippingData3.clearCheck();
        rdgClippingData4.clearCheck();
        llDispaly.setVisibility(View.GONE);

    }




    private void navigateToQualitySheetActivity() {



        /*for (Fragment fragment : mActivity.getSupportFragmentManager().getFragments()) {

            if (fragment != null) {
                mActivity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();*/
                Intent i = new Intent(getActivity(), QualitySheetActivity.class);
                startActivity(i);
                finish();




    }


    @Override
    public void onClick(View v) {

        if (v == btnSubmit) {

            ApplicationUtils.hideKeypad(mActivity, btnSubmit);

            showProgressDialog();

            validateJobName();

        }

    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(mActivity, "", "Please wait...");

    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        } else
            return;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int id) {


        if (id == R.id.radio_clipping_data1_yes) {

            radioData1 = QUALITY_YES;


        }

        if (id == R.id.radio_clipping_data1_no) {

            radioData1 = QUALITY_NO;

        }

        if (id == R.id.radio_clipping_data2_yes) {

            radioData2 = QUALITY_YES;

        }

        if (id == R.id.radio_clipping_data2_no) {

            radioData2 = QUALITY_NO;

        }

        if (id == R.id.radio_clipping_data3_yes) {

            radioData3 = QUALITY_YES;

        }

        if (id == R.id.radio_clipping_data3_no) {

            radioData3 = QUALITY_NO;

        }

        if (id == R.id.radio_clipping_data4_yes) {

            radioData4 = QUALITY_YES;

        }

        if (id == R.id.radio_clipping_data4_no) {

            radioData4 = QUALITY_NO;

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        int viewId = adapterView.getId();
        switch (viewId) {


            case R.id.spin_comments:

                ApplicationUtils.hideKeypad(mActivity, spinComments);

                comments = adapterView.getItemAtPosition(position).toString();

                if (comments != null && comments.trim().length() > 0 && !comments.equalsIgnoreCase("SELECT")) {

                    spinnerComments = comments;

                }
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    public void popBackStackTillEntry(int entryIndex) {

        if (mActivity.getSupportFragmentManager() == null) {
            return;
        }
        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() <= entryIndex) {
            return;
        }
        FragmentManager.BackStackEntry entry = mActivity.getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            mActivity.getSupportFragmentManager().popBackStackImmediate(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


    }

}

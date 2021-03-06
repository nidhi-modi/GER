package com.tandg.qualitysheet.pickingFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.tandg.qualitysheet.listeners.ViewCallback;
import com.tandg.qualitysheet.model.QualityInfo;
import com.tandg.qualitysheet.model.SpinInfo;
import com.tandg.qualitysheet.qualitySheetActivity.QualitySheetActivity;
import com.tandg.qualitysheet.utils.AppContext;
import com.tandg.qualitysheet.utils.ApplicationUtils;
import com.tandg.qualitysheet.utils.BaseFragment;
import com.tandg.qualitysheet.webservice.SpreadsheetWebService;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class PickingFragment extends BaseFragment<PickingFragmentPresenter> implements PickingFragmentContract.View,  RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = PickingFragment.class.getSimpleName();

    @Inject
    QualityInfoDataSource qualityInfoDataSource;

    private AppCompatActivity mActivity;

    //@formatter:off

    @BindView(R.id.edt_row_number)                      EditText edtRowNumber;
    //@BindView(R.id.edt_comments)                        EditText edtComments;

    @BindView(R.id.spin_comments)                       Spinner spinComments;

    @BindView(R.id.input_row_number)                  TextInputLayout inputRowNumber;

    @BindView(R.id.rdg_picking_data1)                 RadioGroup rdgPickingData1;
    @BindView(R.id.rdg_picking_data2)                 RadioGroup rdgPickingData2;
    @BindView(R.id.rdg_picking_data3)                 RadioGroup rdgPickingData3;
    @BindView(R.id.rdg_picking_data4)                 RadioGroup rdgPickingData4;

    @BindView(R.id.radio_picking_data1_yes)           LiquidRadioButton radioPickingData1yes;
    @BindView(R.id.radio_picking_data1_no)            LiquidRadioButton radioPickingData1no;
    @BindView(R.id.radio_picking_data2_yes)           LiquidRadioButton radioPickingData2yes;
    @BindView(R.id.radio_picking_data2_no)            LiquidRadioButton radioPickingData2no;
    @BindView(R.id.radio_picking_data3_yes)           LiquidRadioButton radioPickingData3yes;
    @BindView(R.id.radio_picking_data3_no)            LiquidRadioButton radioPickingData3no;
    @BindView(R.id.radio_picking_data4_yes)           LiquidRadioButton radioPickingData4yes;
    @BindView(R.id.radio_picking_data4_no)            LiquidRadioButton radioPickingData4no;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.linearLayoutDisplay)
    LinearLayout llDispaly;
    @BindView(R.id.txtQualityPercentage)
    TextView txtQualityPercent;

    //@formatter:on


    String radioData1, radioData2, radioData3, radioData4;
    String argJobName, argAuditorName, argHouseNumber, argWeekNumber, argWorkerName, argAdiCode,percentTest;
    private QualityInfo globalQualityInfo, qualityInfo;
    private ProgressDialog progressDialog;
    private String                                     spinnerWorkerName, spinnerAdiNumber, workerName1, adiNumber1, spinnerComments, comments;
    private int                                        workerPosition, combinedPos;
    private int count = 0;
    private int percentCount = 25;
    List<QualityInfo> qualityInfosList;
    int id;
    String strJobName, strAuditorName, strHouseNumber, strWeekNumber, strWorkerName, strAdiCode, strRowNumber, strData1, strData2, strData3, strData4, strData5, strData6, strData7, strData8, strComments, infoStatus, qualityPercent;
    ArrayList<String> WorkersName, ADICode;
    ArrayList<String> ssCombinedData, ssPercentage;
    boolean isVisited = false;
    private ViewCallback mListener;





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

        argJobName       = getArguments().getString("txtJobName");
        argAuditorName   = getArguments().getString("txtAuditorName");
        argHouseNumber   = getArguments().getString("txtHouseNo");
        argWeekNumber    = getArguments().getString("txtWeekNo");
        argWorkerName    = getArguments().getString("txtWorkerName");
        argAdiCode       = getArguments().getString("txtADICode");

        Log.e(TAG, "Data From Activity to Picking fragment: " +argJobName+" "+argAuditorName+" "+argHouseNumber+" "+argWeekNumber+" "+argWorkerName+" "+argAdiCode );

        globalQualityInfo = new QualityInfo();
        qualityInfo       = new QualityInfo();
        WorkersName       = new ArrayList<>();
        ADICode           = new ArrayList<>();
        ssCombinedData    = new ArrayList<>();
        ssPercentage      = new ArrayList<>();

        edtRowNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        //edtComments.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //edtComments.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        rdgPickingData1.setOnCheckedChangeListener(this);
        rdgPickingData2.setOnCheckedChangeListener(this);
        rdgPickingData3.setOnCheckedChangeListener(this);
        rdgPickingData4.setOnCheckedChangeListener(this);


        spinComments.setOnItemSelectedListener(this);


        btnSubmit.setOnClickListener(this);


        if(ApplicationUtils.isConnected(mActivity)) {

            getQualityPercentageFromSheet();

        }else{

            mListener.freezeComponent(false);
            displayPercentageData();
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

            globalQualityInfo = qualityInfoDataSource.getQualityInfoByJobAndWorkerName(argWorkerName, argJobName);

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

                            mListener.freezeComponent(false);

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewCallback) {
            //init the listener
            mListener = (ViewCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void initSpinners() {

        /*List<String> arrayList = Arrays.asList(getResources().getStringArray(R.array.ger_adi));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity, R.layout.layout_spinner_label, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinAdiNumber.setAdapter(arrayAdapter);*/

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
    protected PickingFragmentPresenter getPresenter() {
        return new PickingFragmentPresenter(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_picking;
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    //------------------------------------------VALIDATION-------------------------------------------------------------
    private void validateJobName() {

        if(argJobName != null && argJobName.trim().length() > 0){

            qualityInfo.setJobName(argJobName);

            validateAuditorsName(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Select Job Name");

        }

    }

    private void validateAuditorsName(QualityInfo qualityInfo) {

        if(argAuditorName != null && argAuditorName.trim().length() > 0){

            qualityInfo.setAuditorName(argAuditorName);

            validateHouseNumber(qualityInfo);

        }else {
            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Select Auditor's Name");

        }
    }

    private void validateHouseNumber(QualityInfo qualityInfo) {

        if(argHouseNumber != null && argHouseNumber.trim().length() > 0){

            qualityInfo.setHouseNumber(argHouseNumber);

            validateWeekNumber(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Enter House Number");

        }

    }

    private void validateWeekNumber(QualityInfo qualityInfo) {

        if(argWeekNumber != null && argWeekNumber.trim().length() > 0){

            qualityInfo.setWeekNumber(argWeekNumber);

            validateWorkerName(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Week Number Missing");

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

        if(rowNumber != null && rowNumber.trim().length() > 0){

            inputRowNumber.setErrorEnabled(false);
            qualityInfo.setRowNumber(rowNumber);
            validatePickingData1(qualityInfo);


        }else {

            hideProgressDialog();
            inputRowNumber.setErrorEnabled(true);
            inputRowNumber.setError("Enter Row Number");
        }
    }

    private void validatePickingData1(QualityInfo qualityInfo) {

        if(radioData1!= null && radioData1.trim().length() > 0){

            qualityInfo.setQualityData1(radioData1);

            validatePickingData2(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Choose One Option from Accuracy");

        }

    }

    private void validatePickingData2(QualityInfo qualityInfo) {

        if(radioData2!= null && radioData2.trim().length() > 0){

            qualityInfo.setQualityData2(radioData2);

            validatePickingData3(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Choose One Option from Position of Arch");

        }

    }

    private void validatePickingData3(QualityInfo qualityInfo) {

        if(radioData3!= null && radioData3.trim().length() > 0){

            qualityInfo.setQualityData3(radioData3);

            validatePickingData4(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Choose One Option from Picking & Arching");

        }
    }

    private void validatePickingData4(QualityInfo qualityInfo) {

        if(radioData4!= null && radioData4.trim().length() > 0){

            qualityInfo.setQualityData4(radioData4);

            validatePickingComments(qualityInfo);

        }else {

            hideProgressDialog();
            ApplicationUtils.showToast(mActivity,"Choose One Option from No Broken Trusses");

        }


    }

    private void validatePickingComments(QualityInfo qualityInfo) {

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
        rdgPickingData1.clearCheck();
        rdgPickingData2.clearCheck();
        rdgPickingData3.clearCheck();
        rdgPickingData4.clearCheck();
        spinComments.setSelection(0);
        llDispaly.setVisibility(View.GONE);


    }


    private void navigateToQualitySheetActivity() {

        Intent i = new Intent(getActivity(), QualitySheetActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onClick(View v) {
        if(v == btnSubmit){

            ApplicationUtils.hideKeypad(mActivity,btnSubmit);

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

        if(id == R.id.radio_picking_data1_yes){

            radioData1 = QUALITY_YES;

        }

        if(id == R.id.radio_picking_data1_no){

            radioData1 = QUALITY_NO;

        }

        if(id == R.id.radio_picking_data2_yes){

            radioData2 = QUALITY_YES;

        }

        if(id == R.id.radio_picking_data2_no){

            radioData2 = QUALITY_NO;

        }

        if(id == R.id.radio_picking_data3_yes){

            radioData3 = QUALITY_YES;

        }

        if(id == R.id.radio_picking_data3_no){

            radioData3 = QUALITY_NO;

        }

        if(id == R.id.radio_picking_data4_yes){

            radioData4 = QUALITY_YES;

        }

        if(id == R.id.radio_picking_data4_no){

            radioData4 = QUALITY_NO;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        int viewId = adapterView.getId();
        switch (viewId) {


            case R.id.spin_adi_number:

                adiNumber1 = adapterView.getItemAtPosition(workerPosition).toString();


                if (adiNumber1 != null && adiNumber1.trim().length() > 0 && !adiNumber1.equalsIgnoreCase("SELECT")) {

                    spinnerAdiNumber = adiNumber1;

                }

                break;

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
}

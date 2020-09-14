package com.tandg.qualitysheet.qualitySheetActivity;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tandg.qualitysheet.clippingFragment.ClippingFragment;
import com.tandg.qualitysheet.database.dataSource.QualityInfoDataSource;
import com.tandg.qualitysheet.deleafingFragment.DeleafingFragment;
import com.tandg.qualitysheet.droppingFragment.DroppingFragment;
import com.tandg.qualitysheet.R;
import com.tandg.qualitysheet.di.DependencyInjector;
import com.tandg.qualitysheet.helper.ApplicationHelper;
import com.tandg.qualitysheet.listeners.ViewCallback;
import com.tandg.qualitysheet.model.SpinInfo;
import com.tandg.qualitysheet.pickingFragment.PickingFragment;
import com.tandg.qualitysheet.pruningFragment.PruningFragment;
import com.tandg.qualitysheet.twistingFragment.TwistingFragment;
import com.tandg.qualitysheet.utils.ApplicationUtils;
import com.tandg.qualitysheet.utils.BaseActivity;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class QualitySheetActivity extends BaseActivity<QualitySheetPresenter> implements QualitySheetContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener, ViewCallback{

    private static final String TAG = QualitySheetActivity.class.getSimpleName();

    //@formatter:off


    @BindView(R.id.spin_auditor_name)              Spinner spinAuditorName;
    @BindView(R.id.spin_job_name)                  Spinner spinJobName;
    @BindView(R.id.spin_house_number)              Spinner spinHousenumber;
    @BindView(R.id.spin_worker_name)               SearchableSpinner spinWorkerName;
    @BindView(R.id.spin_adi_number)                Spinner spinAdiNumber;



    //@formatter:on

    private static ViewCallback viewSelectionCallback;

    private String spinnerAuditorName, spinnerJobName, spinnerWeekNumber, spinnerHouseNumber, spinnerWorkerName, spinnerAdiNumber;
    private String auditorName, jobName, weekNumber, houseNumber, workerName1, adiNumber1;
    ArrayList<String> WorkersName, ADICode;
    ArrayList<String> ssCombinedData, ssPercentage;
    private int                                         workerPosition, combinedPos;
    private boolean isAuditor = false;
    private boolean isWeekNumber = false;
    private boolean isHouseNumber = false;


    Handler handler;
    QualityInfoDataSource qualityInfoDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quality_sheet);

        initResources();

    }

    private void initResources() {

        DependencyInjector.appComponent().inject(this);


        //spinJobName.setEnabled(false);
        spinHousenumber.setEnabled(true);
        spinWorkerName.setEnabled(true);
        spinAuditorName.setEnabled(true);


        WorkersName       = new ArrayList<>();
        ADICode           = new ArrayList<>();
        ssCombinedData    = new ArrayList<>();
        ssPercentage      = new ArrayList<>();


        spinJobName.setOnItemSelectedListener(this);
        spinAuditorName.setOnItemSelectedListener(this);
        spinHousenumber.setOnItemSelectedListener(this);
        spinAdiNumber.setOnItemSelectedListener(this);
        spinWorkerName.setOnItemSelectedListener(this);


        spinnerWeekNumber = ApplicationUtils.getDateTime();

        initListners();

        initSpinners();

        if(ApplicationUtils.isConnected(getApplicationContext())){

            getItems();

            getQualityPercentageFromSheet();


        }else {

            List<String> arrayList = Arrays.asList(getResources().getStringArray(R.array.ger_names));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_spinner_label, arrayList);
            arrayAdapter.setDropDownViewResource(R.layout.layout_spinner_label);
            spinWorkerName.setAdapter(arrayAdapter);

            List<String> arrayList1 = Arrays.asList(getResources().getStringArray(R.array.ger_adi));
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_spinner_label, arrayList1);
            arrayAdapter1.setDropDownViewResource(R.layout.layout_spinner_label);
            spinAdiNumber.setAdapter(arrayAdapter1);




        }




        navigateToFragments();

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

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    private void getItems() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbwg5HBhqUaD8_anJooaGgWtWbzSrGA2iYnMdSqzYnOe8aSZsG9Y/exec?action=getGerNames",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("items");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String name1=jsonObject1.getString("workersName");
                                String adi1=jsonObject1.getString("adiCode");

                                WorkersName.add(name1);
                                ADICode.add(adi1);
                            }


                            setSpinner();



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

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

    }



    private void setSpinner() {

        spinWorkerName.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, WorkersName));
        spinAdiNumber.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ADICode));


    }


    private void initListners() {

        spinAuditorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerAuditorName = parent.getItemAtPosition(position).toString();

                if (spinnerAuditorName != null && spinnerAuditorName.length() > 0 && !spinnerAuditorName.equalsIgnoreCase("SELECT")) {

                    spinAuditorName.setEnabled(true);
                    spinJobName.setEnabled(true);
                    spinHousenumber.setEnabled(true);
                    spinWorkerName.setEnabled(true);
                    validateHouseNumber();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


    }


    private void validateHouseNumber() {

        spinHousenumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerHouseNumber = parent.getItemAtPosition(position).toString();

                if (spinnerHouseNumber != null && spinnerHouseNumber.length() > 0 && !spinnerHouseNumber.equalsIgnoreCase("SELECT")) {

                    spinAuditorName.setEnabled(true);
                    spinJobName.setEnabled(true);
                    spinHousenumber.setEnabled(true);
                    spinWorkerName.setEnabled(true);
                    validateWorker();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

    }

    private void validateWorker() {

        spinWorkerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ApplicationUtils.hideKeypad(getApplicationContext(), spinWorkerName);

                ApplicationUtils.hideKeypad(getApplicationContext(), spinAdiNumber);

                spinnerWorkerName = parent.getItemAtPosition(position).toString();

                workerPosition = spinWorkerName.getSelectedItemPosition();

                spinAdiNumber.setSelection(workerPosition);

                if (spinnerWorkerName != null && spinnerWorkerName.length() > 0 && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {
                    spinAuditorName.setEnabled(true);

                    spinJobName.setEnabled(true);
                    spinHousenumber.setEnabled(true);
                    spinWorkerName.setEnabled(true);
                    validateADI();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }

    private void validateADI() {

        spinAdiNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                adiNumber1 = parent.getItemAtPosition(workerPosition).toString();

                if (adiNumber1 != null && adiNumber1.trim().length() > 0 && !adiNumber1.equalsIgnoreCase("SELECT")) {

                    spinnerAdiNumber = adiNumber1;
                    spinAuditorName.setEnabled(true);

                    spinJobName.setEnabled(true);
                    spinHousenumber.setEnabled(true);
                    spinWorkerName.setEnabled(true);


                }else {

                    Log.e(TAG, "Adapter is null" );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }


    private void navigateToFragments() {

        if (spinnerJobName == null) {

            FragmentManager fm = getSupportFragmentManager();
            for (int j = 0; j < fm.getBackStackEntryCount(); ++j) {
                fm.popBackStack();
            }
        }
    }

    private void setUpEditText(EditText editText) {
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

    }

    private void initSpinners() {


        //------------------------------JOB_NAME----------------------------------------

        ArrayList<SpinInfo> stringArrayList = new ArrayList<>();
        stringArrayList.add(new SpinInfo(0, "SELECT"));
        stringArrayList.add(new SpinInfo(1, "Clipping"));
        stringArrayList.add(new SpinInfo(2, "Deleafing"));
        stringArrayList.add(new SpinInfo(3, "Pruning"));
        stringArrayList.add(new SpinInfo(4, "Dropping"));
        stringArrayList.add(new SpinInfo(5, "Twisting"));
        stringArrayList.add(new SpinInfo(6, "Picking"));


        ArrayAdapter<SpinInfo> adapter = new ArrayAdapter<SpinInfo>(getApplicationContext(), R.layout.layout_spinner_label, stringArrayList);
        adapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinJobName.setAdapter(adapter);

        ApplicationUtils.hideKeypad(getApplicationContext(), spinJobName);

        //------------------------------AUDITOR_NAME----------------------------------------

        ArrayList<SpinInfo> arrayList = new ArrayList<>();
        arrayList.add(new SpinInfo(0, "SELECT"));
        arrayList.add(new SpinInfo(1, "Deep Singh"));
        arrayList.add(new SpinInfo(2, "Melody Taylor"));
        arrayList.add(new SpinInfo(3, "Nau Pesa"));
        arrayList.add(new SpinInfo(4, "Francis Dee"));
        arrayList.add(new SpinInfo(5, "Nilesh Patel"));
        arrayList.add(new SpinInfo(6, "Gurjant Singh"));
        arrayList.add(new SpinInfo(7, "Tevita Fetuani"));
        arrayList.add(new SpinInfo(8, "Sione Potesio"));

        ArrayAdapter<SpinInfo> arrayAdapter = new ArrayAdapter<SpinInfo>(getApplicationContext(), R.layout.layout_spinner_label, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinAuditorName.setAdapter(arrayAdapter);

        ApplicationUtils.hideKeypad(getApplicationContext(), spinAuditorName);

        //------------------------------HOUSE_NUMBER----------------------------------------

        ArrayList<SpinInfo> list = new ArrayList<>();
        list.add(new SpinInfo(0, "SELECT"));
        list.add(new SpinInfo(1, "GER 1"));
        list.add(new SpinInfo(2, "GER 2"));
        list.add(new SpinInfo(3, "GER 3"));
        list.add(new SpinInfo(4, "GER 4"));
        list.add(new SpinInfo(5, "GER 5"));


        ArrayAdapter<SpinInfo> infoArrayAdapter = new ArrayAdapter<SpinInfo>(getApplicationContext(), R.layout.layout_spinner_label, list);
        infoArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinHousenumber.setAdapter(infoArrayAdapter);

        ApplicationUtils.hideKeypad(getApplicationContext(), spinAuditorName);
        ApplicationUtils.hideKeypad(getApplicationContext(), spinAdiNumber);
        ApplicationUtils.hideKeypad(getApplicationContext(), spinWorkerName);




    }


    @Override
    protected QualitySheetPresenter getPresenter() {
        return new QualitySheetPresenter(this);
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onClick(View v) {

    }

    private void clearSpinners() {

        spinAuditorName.setSelection(0);
        spinJobName.setSelection(0);
        spinHousenumber.setSelection(0);
        spinWorkerName.setSelection(0);
        spinAdiNumber.setSelection(0);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int viewId = adapterView.getId();
        switch (viewId) {

            case R.id.spin_job_name:

                ApplicationUtils.hideKeypad(getApplicationContext(), spinJobName);

                jobName = adapterView.getItemAtPosition(i).toString();

                if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Dropping") && spinnerAuditorName!= null && spinnerWorkerName != null && spinnerHouseNumber != null && !spinnerAuditorName.equalsIgnoreCase("SELECT") && !spinnerHouseNumber.equalsIgnoreCase("SELECT") && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    bundle.putString("txtWorkerName", spinnerWorkerName);
                    bundle.putString("txtADICode", spinnerAdiNumber);
                    DroppingFragment fragment = new DroppingFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, fragment);
                    fragmentTransaction.addToBackStack("tag");
                    fragmentTransaction.commitAllowingStateLoss();

                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Clipping") && spinnerAuditorName!= null && spinnerWorkerName != null && spinnerHouseNumber != null && !spinnerAuditorName.equalsIgnoreCase("SELECT") && !spinnerHouseNumber.equalsIgnoreCase("SELECT") && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    bundle.putString("txtWorkerName", spinnerWorkerName);
                    bundle.putString("txtADICode", spinnerAdiNumber);
                    ClippingFragment clippingFragment = new ClippingFragment();
                    clippingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, clippingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Deleafing") && spinnerAuditorName!= null && spinnerWorkerName != null && spinnerHouseNumber != null && !spinnerAuditorName.equalsIgnoreCase("SELECT") && !spinnerHouseNumber.equalsIgnoreCase("SELECT") && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    bundle.putString("txtWorkerName", spinnerWorkerName);
                    bundle.putString("txtADICode", spinnerAdiNumber);
                    DeleafingFragment deleafingFragment = new DeleafingFragment();
                    deleafingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, deleafingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Pruning") && spinnerAuditorName!= null && spinnerWorkerName != null && spinnerHouseNumber != null && !spinnerAuditorName.equalsIgnoreCase("SELECT") && !spinnerHouseNumber.equalsIgnoreCase("SELECT") && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    bundle.putString("txtWorkerName", spinnerWorkerName);
                    bundle.putString("txtADICode", spinnerAdiNumber);
                    PruningFragment pruningFragment = new PruningFragment();
                    pruningFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, pruningFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Twisting") && spinnerAuditorName!= null && spinnerWorkerName != null && spinnerHouseNumber != null && !spinnerAuditorName.equalsIgnoreCase("SELECT") && !spinnerHouseNumber.equalsIgnoreCase("SELECT") && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    bundle.putString("txtWorkerName", spinnerWorkerName);
                    bundle.putString("txtADICode", spinnerAdiNumber);
                    TwistingFragment twistingFragment = new TwistingFragment();
                    twistingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, twistingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Picking") && spinnerAuditorName!= null && spinnerWorkerName != null && spinnerHouseNumber != null && !spinnerAuditorName.equalsIgnoreCase("SELECT") && !spinnerHouseNumber.equalsIgnoreCase("SELECT") && !spinnerWorkerName.equalsIgnoreCase("SELECT")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    bundle.putString("txtWorkerName", spinnerWorkerName);
                    bundle.putString("txtADICode", spinnerAdiNumber);
                    PickingFragment pickingFragment = new PickingFragment();
                    pickingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, pickingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName.equalsIgnoreCase("SELECT")) {

                    clearSpinners();
                    spinAuditorName.setEnabled(true);
                    spinHousenumber.setEnabled(false);
                    spinWorkerName.setEnabled(false);
                    spinAdiNumber.setEnabled(false);
                    spinJobName.setEnabled(false);
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);
                    if (fragment != null) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }

                }else {

                    clearSpinners();
                    Toast.makeText(getApplicationContext(),"Please fill all the drop boxes",Toast.LENGTH_SHORT).show();
                }


                break;


            case R.id.spin_auditor_name:

                ApplicationUtils.hideKeypad(getApplicationContext(), spinAuditorName);

                auditorName = adapterView.getItemAtPosition(i).toString();

                if (auditorName != null && auditorName.trim().length() > 0 && !auditorName.equalsIgnoreCase("SELECT")) {

                    spinnerAuditorName = auditorName;

                } else if (auditorName.equalsIgnoreCase("SELECT")) {


                    clearSpinners();
                }

                break;

            case R.id.spin_worker_name:


                ApplicationUtils.hideKeypad(getApplicationContext(), spinWorkerName);

                ApplicationUtils.hideKeypad(getApplicationContext(), spinAdiNumber);

                workerName1 = adapterView.getItemAtPosition(i).toString();

                workerPosition = spinWorkerName.getSelectedItemPosition();

                spinAdiNumber.setSelection(workerPosition);

                if (workerName1 != null && workerName1.length() > 0 && !workerName1.equalsIgnoreCase("SELECT")) {

                    spinnerWorkerName = workerName1;

                }else if (workerName1.equalsIgnoreCase("SELECT")) {


                    clearSpinners();
                }



                break;

            case R.id.spin_adi_number:


                adiNumber1 = adapterView.getItemAtPosition(workerPosition).toString();


                if (adiNumber1 != null && adiNumber1.trim().length() > 0 && !adiNumber1.equalsIgnoreCase("SELECT")) {

                    spinnerAdiNumber = adiNumber1;


                }else if (adiNumber1.equalsIgnoreCase("SELECT")) {


                    clearSpinners();
                }



                break;

            case R.id.spin_house_number:

                ApplicationUtils.hideKeypad(getApplicationContext(), spinHousenumber);

                houseNumber = adapterView.getItemAtPosition(i).toString();

                if (houseNumber != null && houseNumber.trim().length() > 0 && !houseNumber.equalsIgnoreCase("SELECT")) {

                    spinnerHouseNumber = houseNumber;

                } else if (houseNumber.equalsIgnoreCase("SELECT")) {


                    clearSpinners();
                }


                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_files) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void freezeComponent(boolean b) {

        Log.e(TAG, "freezeComponent: "+b );

        if(!b){

            spinJobName.setEnabled(true);

        }else{

            spinJobName.setEnabled(false);

        }

    }
}

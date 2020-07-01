package com.tandg.qualitysheet.qualitySheetActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

import java.util.ArrayList;

import butterknife.BindView;

public class QualitySheetActivity extends BaseActivity<QualitySheetPresenter> implements QualitySheetContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = QualitySheetActivity.class.getSimpleName();

    //@formatter:off


    @BindView(R.id.spin_auditor_name)              Spinner spinAuditorName;
    @BindView(R.id.spin_job_name)                  Spinner spinJobName;
    @BindView(R.id.spin_house_number)              Spinner spinHousenumber;


    //@formatter:on

    private static ViewCallback viewSelectionCallback;

    private String spinnerAuditorName, spinnerJobName, spinnerWeekNumber, spinnerHouseNumber;
    private String auditorName, jobName, weekNumber, houseNumber;
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

        //Client ID 631934245578-gf4dvsmqesblncuehs5klp9h8g3mgtg4.apps.googleusercontent.com


    }

    private void initResources() {

        DependencyInjector.appComponent().inject(this);


        spinJobName.setEnabled(false);
        spinHousenumber.setEnabled(false);


        spinJobName.setOnItemSelectedListener(this);
        spinAuditorName.setOnItemSelectedListener(this);
        spinHousenumber.setOnItemSelectedListener(this);


        spinnerWeekNumber = ApplicationUtils.getDateTime();

        initListners();

        initSpinners();

        navigateToFragments();

    }


    private void initListners() {

        spinAuditorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerAuditorName = parent.getItemAtPosition(position).toString();

                if (spinnerAuditorName != null && spinnerAuditorName.length() > 0 && !spinnerAuditorName.equalsIgnoreCase("SELECT")) {

                    spinJobName.setEnabled(false);
                    spinHousenumber.setEnabled(true);
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

                    spinJobName.setEnabled(true);
                    spinHousenumber.setEnabled(true);
                    validateHouseNumber();

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

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int viewId = adapterView.getId();
        switch (viewId) {

            case R.id.spin_job_name:

                ApplicationUtils.hideKeypad(getApplicationContext(), spinJobName);

                jobName = adapterView.getItemAtPosition(i).toString();

                if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Dropping")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    DroppingFragment fragment = new DroppingFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, fragment);
                    fragmentTransaction.addToBackStack("tag");
                    fragmentTransaction.commitAllowingStateLoss();

                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Clipping")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    ClippingFragment clippingFragment = new ClippingFragment();
                    clippingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, clippingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Deleafing")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    DeleafingFragment deleafingFragment = new DeleafingFragment();
                    deleafingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, deleafingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Pruning")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    PruningFragment pruningFragment = new PruningFragment();
                    pruningFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, pruningFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Twisting")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    TwistingFragment twistingFragment = new TwistingFragment();
                    twistingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, twistingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName != null && jobName.trim().length() > 0 && !jobName.equalsIgnoreCase("SELECT") && jobName.equalsIgnoreCase("Picking")) {

                    spinnerJobName = jobName;
                    spinAuditorName.setEnabled(false);
                    spinHousenumber.setEnabled(false);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("txtJobName", spinnerJobName);
                    bundle.putString("txtAuditorName", spinnerAuditorName);
                    bundle.putString("txtHouseNo", spinnerHouseNumber);
                    bundle.putString("txtWeekNo", spinnerWeekNumber);
                    PickingFragment pickingFragment = new PickingFragment();
                    pickingFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_layout_main, pickingFragment);
                    fragmentTransaction.commitAllowingStateLoss();


                } else if (jobName.equalsIgnoreCase("SELECT")) {

                    clearSpinners();
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);
                    if (fragment != null) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }

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

}

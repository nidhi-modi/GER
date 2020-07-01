package com.tandg.qualitysheet.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tandg.qualitysheet.MainActivity;
import com.tandg.qualitysheet.R;
import com.tandg.qualitysheet.database.dataSource.QualityInfoDataSource;
import com.tandg.qualitysheet.model.QualityInfo;
import com.tandg.qualitysheet.utils.AppContext;
import com.tandg.qualitysheet.utils.ApplicationUtils;
import com.tandg.qualitysheet.webservice.SpreadsheetWebService;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;

import static com.tandg.qualitysheet.constants.AppConstants.QUALITY_INFO_L;
import static com.tandg.qualitysheet.constants.AppConstants.QUALITY_INFO_S;


public class MyBackgroundService extends Service {


    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private static final String TAG = MyBackgroundService.class.getSimpleName();
    private Handler mHandler;

    private QualityInfoDataSource qualityInfoDataSource;

    List<QualityInfo> qualityInfosList;
    QualityInfo qualityInfo;


    int id;
    String strJobName, strAuditorName, strHouseNumber, strWeekNumber, strWorkerName, strAdiCode, strRowNumber, strData1, strData2, strData3, strData4, strData5, strData6, strData7, strData8, strComments, infoStatus, qualityPercent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        qualityInfoDataSource = new QualityInfoDataSource(AppContext.getInstance());
        qualityInfo = new QualityInfo();

        mHandler = new Handler();
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String strId = getString(R.string.noti_channel_id);
            final String strTitle = getString(R.string.app_name);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(strId);
            if (channel == null) {
                channel = new NotificationChannel(strId, strTitle, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(this, strId).build();
            startForeground(1, notification);
        }
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            String path = ApplicationUtils.exportDB(getApplicationContext());

            if (ApplicationUtils.isConnected(AppContext.getInstance())) {
                sendTheLogFile(path);
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    /*private static final String TAG = MyBackgroundService.class.getSimpleName();
    private Handler mHandler;

    public MyBackgroundService() {
        super("MyBackgroundService");
    }

    private QualityInfoDataSource qualityInfoDataSource;

    List<QualityInfo> qualityInfosList;

    int id;
    String strJobName, strAuditorName, strHouseNumber, strWeekNumber, strWorkerName, strRowNumber, strData1, strData2, strData3, strData4, strComments, infoStatus;

    @Override
    public void onCreate() {
        super.onCreate();

        qualityInfoDataSource = new QualityInfoDataSource(AppContext.getInstance());

        mHandler = new Handler();

        Log.e(TAG, "service called");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String path = ApplicationUtils.exportDB(getApplicationContext());

        if(ApplicationUtils.isConnected(AppContext.getInstance())){
            sendTheLogFile(path);
        }


    }*/

    public void sendTheLogFile(final String path /*,final boolean isFilesaved*/) {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                    Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if (qualityInfoDataSource != null) {

                qualityInfoDataSource.open();
                qualityInfosList = qualityInfoDataSource.getAllQualityInfoByStatus();
                qualityInfoDataSource.close();

            }

           /*  for(int i = 0; i < qualityInfosList.size(); i++){

               id = qualityInfosList.get(i).getWorkerId();
                strJobName = qualityInfosList.get(i).getJobName();
                strAuditorName = qualityInfosList.get(i).getAuditorName();
                strHouseNumber = qualityInfosList.get(i).getHouseNumber();
                strWeekNumber = qualityInfosList.get(i).getWeekNumber();
                strWorkerName = qualityInfosList.get(i).getWorkerName();
                strRowNumber = qualityInfosList.get(i).getRowNumber();
                strData1 = qualityInfosList.get(i).getQualityData1();
                strData2 = qualityInfosList.get(i).getQualityData2();
                strData3 = qualityInfosList.get(i).getQualityData3();
                strData4 = qualityInfosList.get(i).getQualityData4();
                strComments = qualityInfosList.get(i).getComments();
                infoStatus = qualityInfosList.get(i).getQualityInfoStaus();
                qualityPercent = qualityInfosList.get(i).getQualityPercent();


            }*/
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
                strData5 = info.getQualityData5();
                strData6 = info.getQualityData6();
                strData7 = info.getQualityData7();
                strData8 = info.getQualityData8();
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



              /* StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://docs.google.com/forms/u/0/d/e/1FAIpQLSdI43ATa34tXHnfTdPgs30LRtCQtBCUx5u7yQaPu0QZRZqqWA/formResponse",
                       new com.android.volley.Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {

                               Log.e(TAG, "onResponse: " + response);

                           }
                       },
                       new com.android.volley.Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {

                               Log.e(TAG, "onErrorResponse: "+ error );
                           }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> parmas = new HashMap<>();

                        //here we pass params

                        parmas.put("entry.880516821", String.valueOf(id));
                        parmas.put("entry.1430492217",strJobName);
                        parmas.put("entry.373744288",strAuditorName);
                        parmas.put("entry.389878221",strHouseNumber);
                        parmas.put("entry.247279399",strWeekNumber);
                        parmas.put("entry.668900209",strWorkerName);
                        parmas.put("entry.1994273709",strRowNumber);
                        parmas.put("entry.1363445947",strData1);
                        parmas.put("entry.790697825",strData2);
                        parmas.put("entry.1921784399",strData3);
                        parmas.put("entry.806686809",strData4);
                        parmas.put("entry.1399381046",strComments);
                        parmas.put("entry.1369821809",qualityPercent);


                        return parmas;

                    }
                };

                Log.e(TAG, "sendTheLogFile: "+id+ " "+strJobName+ " "+ strAuditorName+ " "+strHouseNumber+ " "+ strWeekNumber+ " "+strWorkerName+ " "+ strRowNumber+ " "+ strData1+ " "+ strData2+ " "+ strData3+ " "+ strData4+ " "+ strComments+ " "+ qualityPercent );


                int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

                RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);

                RequestQueue queue = Volley.newRequestQueue(this);

                queue.add(stringRequest);*/
            }

            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }


        } catch (Exception ex) {

            Log.i("Mail", "Failed" + ex);
        }


    }

    private final Callback<Void> callCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

            qualityInfoDataSource.open();

            for (QualityInfo info : qualityInfosList) {
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


}

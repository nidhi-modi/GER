package com.tandg.qualitysheet.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.tandg.qualitysheet.BuildConfig;
import com.tandg.qualitysheet.R;
import com.tandg.qualitysheet.constants.DatabaseConstants;
import com.tandg.qualitysheet.receivers.AlarmReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by root on 23/12/17.
 */

public class ApplicationUtils {

    private static final String TAG = ApplicationUtils.class.getSimpleName();

    public static boolean isFragmentVisible(Fragment fragment) {
        if (fragment.isVisible()) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideKeypad(Context context, View view) {

        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            //   imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Toast showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
        return toast;
    }

    public static Toast showToastSmall(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
        return toast;
    }



    public static void validatingDateSelection(final EditText editText, Context context){

        editText.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            private String dob;
            private String valueText;

            /* private int day = 0;
             private int mon = 0;
             private int year = 0;*/
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int in = 2; in <= cl && in < 6; in += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        try {
                            int day = Integer.parseInt(clean.substring(0, 2));
                            int mon = Integer.parseInt(clean.substring(2, 4));
                            int year = Integer.parseInt(clean.substring(4, 8));

                            if (mon > 12) mon = 12;
                            cal.set(Calendar.MONTH, mon - 1);
                            year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                            cal.set(Calendar.YEAR, year);

                            // ^ first set year for the line below to work correctly
                            //with leap years - otherwise, date e.g. 29/02/2012
                            //would be automatically corrected to 28/02/2012

                            day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                            clean = String.format("%02d%02d%02d",day, mon, year);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(sel < current.length() ? sel : current.length());

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


        public static void showToast(String message, EditText editText, boolean warning) {

        if (warning == true) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_cross, 0);

            Toast toast = Toast.makeText(AppContext.getInstance(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 200);
            toast.show();
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_check, 0);
        }


    }


    public static void clearFragmentByTag(Context context, String tag) {
        try {
            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();

            for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
                String backEntry = fm.getBackStackEntryAt(i).getName();
                if (backEntry.equals(tag)) {
                    break;
                } else {
                    fm.popBackStack();
                }
            }
        } catch (Exception e) {
            System.out.print("!====Popbackstack error : " + e);
            e.printStackTrace();
        }
    }

    public static String exportDB(Context context){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/" + BuildConfig.APPLICATION_ID + "/databases/"+ DatabaseConstants.DATABASE_NAME_RUNTIME;
        String backupDBPath = DatabaseConstants.DATABASE_NAME_RUNTIME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
            Log.e(TAG, "exportDB: exported ");
        } catch(IOException e) {
            e.printStackTrace();
        }

        return backupDB.getAbsolutePath();
    }

    public static void scheduleAlarm(Context context) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)!= null);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                30 * 1000, pIntent);

        //fire an alarm after every 30 sec

    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static int getConnectivityType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static boolean isConnected(Context context) {
        int connectivityType = getConnectivityType(context);
        if (connectivityType == -1)
            return false;
        else
            return true;
    }

    public static String getDateTime(){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String formattedDate = df.format(c.getTime());
        Log.e(TAG, "getDateTime: "+formattedDate );
        return formattedDate;

    }

}

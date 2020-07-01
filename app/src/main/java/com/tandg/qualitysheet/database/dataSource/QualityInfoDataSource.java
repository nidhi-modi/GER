package com.tandg.qualitysheet.database.dataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tandg.qualitysheet.database.RunTimeSqLiteHelper;
import com.tandg.qualitysheet.helper.ApplicationHelper;
import com.tandg.qualitysheet.helper.HelperInterface;
import com.tandg.qualitysheet.model.QualityInfo;

import java.util.ArrayList;
import java.util.List;
import static com.tandg.qualitysheet.constants.AppConstants.QUALITY_INFO_L;


/**
 * Created by root on 23/12/17.
 */

public class QualityInfoDataSource implements HelperInterface {

    private static final String TAG = QualityInfoDataSource.class.getSimpleName();

    private RunTimeSqLiteHelper dbHelper;
    private SQLiteDatabase database;

    private String[] allColumns = {
            WORKER_ID,
            WORKER_NAME,
            ADI_CODE,
            WEEK_NUMBER,
            HOUSE_NUMBER,
            ROW_NUMBER,
            COMMENTS,
            QUALITY_INFO_STATUS,
            CREATE_DATE_TIME,
            QUALITY_DATA1,
            QUALITY_DATA2,
            QUALITY_DATA3,
            QUALITY_DATA4,
            QUALITY_DATA5,
            QUALITY_DATA6,
            QUALITY_DATA7,
            QUALITY_DATA8,
            AUDITOR_NAME,
            QUALITY_PERCENT,
            JOB_NAME};


    public QualityInfoDataSource(Context context) {
        dbHelper = RunTimeSqLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createQualityInfo(QualityInfo qualityInfo) {

        ContentValues values = new ContentValues();

        //values.put(WORKER_ID                  , qualityInfo.getWorkerId());
        values.put(WORKER_NAME                  , qualityInfo.getWorkerName());
        values.put(ADI_CODE                     , qualityInfo.getAdiCode());
        values.put(WEEK_NUMBER                  , qualityInfo.getWeekNumber());
        values.put(HOUSE_NUMBER                 , qualityInfo.getHouseNumber());
        values.put(ROW_NUMBER                   , qualityInfo.getRowNumber());
        values.put(COMMENTS                     , qualityInfo.getComments());
        values.put(QUALITY_INFO_STATUS          , qualityInfo.getQualityInfoStaus());
        values.put(CREATE_DATE_TIME             , qualityInfo.getCreatedDateTime());
        values.put(QUALITY_DATA1                , qualityInfo.getQualityData1());
        values.put(QUALITY_DATA2                , qualityInfo.getQualityData2());
        values.put(QUALITY_DATA3                , qualityInfo.getQualityData3());
        values.put(QUALITY_DATA4                , qualityInfo.getQualityData4());
        values.put(QUALITY_DATA5                , qualityInfo.getQualityData5());
        values.put(QUALITY_DATA6                , qualityInfo.getQualityData6());
        values.put(QUALITY_DATA7                , qualityInfo.getQualityData7());
        values.put(QUALITY_DATA8                , qualityInfo.getQualityData8());
        values.put(AUDITOR_NAME                 , qualityInfo.getAuditorName());
        values.put(QUALITY_PERCENT              , qualityInfo.getQualityPercent());
        values.put(JOB_NAME                     , qualityInfo.getJobName());


        long insertId = database.insert(TABLE_QUALITY_INFO, null, values);
        Log.i(TAG, "create quality info id : " + insertId + " insert id : " + insertId);
        return insertId;
    }

    public long updateQualityInfo(QualityInfo qualityInfo) {

        ContentValues values = new ContentValues();

        values.put(WORKER_ID                    , qualityInfo.getWorkerId());
        values.put(WORKER_NAME                  , qualityInfo.getWorkerName());
        values.put(ADI_CODE                     , qualityInfo.getAdiCode());
        values.put(WEEK_NUMBER                  , qualityInfo.getWeekNumber());
        values.put(HOUSE_NUMBER                 , qualityInfo.getHouseNumber());
        values.put(ROW_NUMBER                   , qualityInfo.getRowNumber());
        values.put(COMMENTS                     , qualityInfo.getComments());
        values.put(QUALITY_INFO_STATUS          , qualityInfo.getQualityInfoStaus());
        values.put(CREATE_DATE_TIME             , qualityInfo.getCreatedDateTime());
        values.put(QUALITY_DATA1                , qualityInfo.getQualityData1());
        values.put(QUALITY_DATA2                , qualityInfo.getQualityData2());
        values.put(QUALITY_DATA3                , qualityInfo.getQualityData3());
        values.put(QUALITY_DATA4                , qualityInfo.getQualityData4());
        values.put(QUALITY_DATA5                , qualityInfo.getQualityData5());
        values.put(QUALITY_DATA6                , qualityInfo.getQualityData6());
        values.put(QUALITY_DATA7                , qualityInfo.getQualityData7());
        values.put(QUALITY_DATA8                , qualityInfo.getQualityData8());
        values.put(AUDITOR_NAME                 , qualityInfo.getAuditorName());
        values.put(QUALITY_PERCENT              , qualityInfo.getQualityPercent());
        values.put(JOB_NAME                     , qualityInfo.getJobName());


        long updateId = database.update(TABLE_QUALITY_INFO, values, WORKER_ID + " =? ",
                new String[]{String.valueOf(qualityInfo.getWorkerId())});
        Log.i(TAG, "quality info update id : " + updateId);
        return updateId;

    }

    public ArrayList<QualityInfo> getAllQualityInfo() {
        ArrayList<QualityInfo> qualityInfoList = new ArrayList<QualityInfo>();

        try {
            Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                QualityInfo qualityInfo = cursorToQuality(cursor);
                qualityInfoList.add(qualityInfo);
                cursor.moveToNext();
            }
            Log.i(TAG, "total quality info found : " + cursor.getCount() + " Quality info list size : " + qualityInfoList.size());
            cursor.close();

        }catch (Exception e){

            e.printStackTrace();

        }finally {

            database.close();

        }
       /* ClientInfo info = new ClientInfo();
        info.setClientName("SELECT");
        info.setScriptName("SELECT");
        ClientInfoList.add(0, info);*/
        return qualityInfoList;
    }

    public List<QualityInfo> getAllInfoByWorkerName(String name) {
        List<QualityInfo> infoList = new ArrayList<QualityInfo>();

        Cursor cursor = null;

        if(name != null && name.trim().length() > 0){
            cursor = database.query(TABLE_QUALITY_INFO, allColumns, WORKER_NAME + " LIKE ?",
                    new String[]{name+"%"}, null, null, WORKER_NAME + " ASC");

        }else {
            cursor = database.query(TABLE_QUALITY_INFO, allColumns,
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QualityInfo city = cursorToQuality(cursor);
            infoList.add(city);
            cursor.moveToNext();
        }
        Log.i(TAG, "total info found : " + cursor.getCount() + " info list size : " + infoList.size());
        cursor.close();

        return infoList;
    }

    public ArrayList<QualityInfo> getAllQualityInfoByStatus() {

        ArrayList<QualityInfo> qualityInfoArrayList = new ArrayList<QualityInfo>();
        Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, QUALITY_INFO_STATUS + "= ?" ,
                new String[]{QUALITY_INFO_L}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QualityInfo qualityInfo = cursorToQuality(cursor);
            qualityInfoArrayList.add(qualityInfo);
            cursor.moveToNext();
        }
        Log.i(TAG, "total Quality info found : " + cursor.getCount() + " Quality info list size : " + qualityInfoArrayList.size());
        cursor.close();

        return qualityInfoArrayList;
    }



    public ArrayList<QualityInfo> getAllQualityInfoByAuditorName() {

        ArrayList<QualityInfo> qualityInfoArrayList = new ArrayList<QualityInfo>();
        Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, AUDITOR_NAME + "= ?" ,
                new String[]{QUALITY_INFO_L}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QualityInfo qualityInfo = cursorToQuality(cursor);
            qualityInfoArrayList.add(qualityInfo);
            cursor.moveToNext();
        }
        Log.i(TAG, "total Quality info found : " + cursor.getCount() + " Quality info list size : " + qualityInfoArrayList.size());
        cursor.close();

        return qualityInfoArrayList;
    }


    public ArrayList<QualityInfo> getAllQualityInfoByJobWork(String jobWork) {

        ArrayList<QualityInfo> qualityInfoArrayList = new ArrayList<QualityInfo>();
        Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, JOB_NAME + " LIKE ?" ,
                new String[]{jobWork}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QualityInfo qualityInfo = cursorToQuality(cursor);
            qualityInfoArrayList.add(qualityInfo);
            cursor.moveToNext();
        }
        Log.i(TAG, "total Quality info found : " + cursor.getCount() + " Quality info list size : " + qualityInfoArrayList.size());
        cursor.close();

        return qualityInfoArrayList;
    }

    public QualityInfo getQualityInfoByWorkerName( String workerName) {

        Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, WORKER_NAME + "=? ",
                new String[]{workerName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            QualityInfo qualityInfo = cursorToQuality(cursor);
            return qualityInfo;
        } else {
            return null;
        }
    }

    public QualityInfo getAllQualityInfoByStatus2() {

        Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, QUALITY_INFO_STATUS + "= ?" ,
                new String[]{QUALITY_INFO_L}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            QualityInfo qualityInfo = cursorToQuality(cursor);
            return qualityInfo;
        } else {
            return null;
        }
    }




    public int deleteQualityInfo(QualityInfo qualityInfo) {
        String appNo = String.valueOf(qualityInfo.getWorkerId());
        System.out.println("Quality info deleted with no: " + appNo);
        int deleteId = database.delete(TABLE_QUALITY_INFO, WORKER_ID + " = " + appNo, null);
        return deleteId;

    }


    public int deleteAllQualityInfo() {
        int deleteId = database.delete(TABLE_QUALITY_INFO, null, null);
        System.out.println("All Quality Info deleted with id: " + deleteId);
        return deleteId;

    }

    public QualityInfo getQualityInfoByJobAndWorkerName(String workerName, String jobName) {

        Cursor cursor = database.query(TABLE_QUALITY_INFO, allColumns, WORKER_NAME + "=? AND " + JOB_NAME +" = ?",
                new String[]{workerName, jobName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            QualityInfo qualityInfo = cursorToQuality(cursor);
            return qualityInfo;
        } else {
            return null;
        }
    }

    /*public QualityInfo getClientInfoByClientName( String clientName) {

        Cursor cursor = database.query(TABLE_CLIENT_INFO, allColumns, CLIENT_NAME + "=? ",
                new String[]{clientName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ClientInfo clientInfo = cursorToClient(cursor);
            return clientInfo;
        } else {
            return null;
        }
    }*/


    public int isMasterEmpty() {

        String count = "SELECT count(*) FROM " + TABLE_QUALITY_INFO ;
        Cursor mcursor = database.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        Log.e(TAG, "isMasterEmpty: "+icount );
        mcursor.close();
        return icount;
    }




    private QualityInfo cursorToQuality(Cursor cursor) {

        QualityInfo qualityInfo = new QualityInfo();

        qualityInfo.setWorkerId(cursor.getInt(0));
        qualityInfo.setWorkerName(cursor.getString(1));
        qualityInfo.setAdiCode(cursor.getString(2));
        qualityInfo.setWeekNumber(cursor.getString(3));
        qualityInfo.setHouseNumber(cursor.getString(4));
        qualityInfo.setRowNumber(cursor.getString(5));
        qualityInfo.setComments(cursor.getString(6));
        qualityInfo.setQualityInfoStaus(cursor.getString(7));
        qualityInfo.setCreatedDateTime(cursor.getString(8));
        qualityInfo.setQualityData1(cursor.getString(9));
        qualityInfo.setQualityData2(cursor.getString(10));
        qualityInfo.setQualityData3(cursor.getString(11));
        qualityInfo.setQualityData4(cursor.getString(12));
        qualityInfo.setQualityData5(cursor.getString(13));
        qualityInfo.setQualityData6(cursor.getString(14));
        qualityInfo.setQualityData7(cursor.getString(15));
        qualityInfo.setQualityData8(cursor.getString(16));
        qualityInfo.setAuditorName(cursor.getString(17));
        qualityInfo.setQualityPercent(cursor.getString(18));
        qualityInfo.setJobName(cursor.getString(19));

        return qualityInfo;
    }


    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}

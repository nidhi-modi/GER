package com.tandg.qualitysheet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tandg.qualitysheet.helper.ApplicationHelper;
import com.tandg.qualitysheet.helper.HelperInterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * Created by root on 28/7/17.
 */

public class RunTimeSqLiteHelper extends SQLiteOpenHelper implements HelperInterface {

    private static final String TAG = RunTimeSqLiteHelper.class.getSimpleName();

    private Context context;
    private static RunTimeSqLiteHelper instance;


    public RunTimeSqLiteHelper(Context context){
        super(context,DATABASE_NAME_RUNTIME, null, DATABASE_VERSION_RUNTIME);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_QUALITY_INFO);

        Log.e(TAG, "TABLES CREATED ON RUNTIME");
    }

    private static void createDataBase(Context context) {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            SQLiteDatabase database = getExistingReadSqliteDatabase();
            if (database != null) {
                if (DATABASE_VERSION_RUNTIME > database.getVersion()) {
                    context.deleteDatabase(DATABASE_NAME_RUNTIME);
                    pasteNewDataBase(context);
                } else {
                    instance.getReadableDatabase();
                }
            } else {
                context.deleteDatabase(DATABASE_NAME_RUNTIME);
                pasteNewDataBase(context);
            }
        } else {
            pasteNewDataBase(context);
        }
    }

    private static SQLiteDatabase getExistingReadSqliteDatabase() {

        SQLiteDatabase database = null;
        try {
            String path = DB_PATH + DATABASE_NAME_RUNTIME;
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return database;
    }

    private static boolean checkDataBase() {
        SQLiteDatabase database = getExistingReadSqliteDatabase();
        if (database != null) {
            database.close();
        }
        return database != null ? true : false;
    }




    private static void pasteNewDataBase(Context context) {
        instance.getReadableDatabase();
        try {
            copyDataBase(context);
            SQLiteDatabase database = getExistingWriteSqliteDatabase();
            database.setVersion(DATABASE_VERSION_RUNTIME);
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private static SQLiteDatabase getExistingWriteSqliteDatabase() {
        SQLiteDatabase database = null;
        try {
            String path = DB_PATH + DATABASE_NAME_RUNTIME;
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return database;

    }


    public static void copyDataBase(Context context) throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME_RUNTIME);
        String outFileName = DB_PATH + DATABASE_NAME_RUNTIME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldDB, int newDB) {
        if(newDB > oldDB){
            Cursor cursor = null;

            /*--------------------- TABLE_QUALITY_INFO*---------------------------------*/

            try {

                cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_QUALITY_INFO + " limit 1", null);
                int index = cursor.getColumnIndex(JOB_NAME);

                if (index == -1) {
                    sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_QUALITY_INFO + " ADD COLUMN " +
                            JOB_NAME + " TEXT");
                }

                } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            cursor = null;


            Log.e(TAG, "NEW DB AVAILABLE, TABLES DROPED");
            onCreate(sqLiteDatabase);

        }else{
            Log.e(TAG, "RUNTIME DB ALREADY EXISTS");
        }
    }

    public static RunTimeSqLiteHelper getInstance(Context context){
        if(null == instance){
            instance = new RunTimeSqLiteHelper(context);
            instance.getWritableDatabase();
        }
        return instance;
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}
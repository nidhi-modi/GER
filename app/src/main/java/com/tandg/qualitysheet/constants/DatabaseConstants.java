package com.tandg.qualitysheet.constants;


import com.tandg.qualitysheet.BuildConfig;

/**
 * Created by root on 5/06/20.
 */

public interface DatabaseConstants {


    String DATABASE_NAME_RUNTIME     = "quality_sheet.sqlite";
    String DB_PATH                   = "/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    int    DATABASE_VERSION_RUNTIME  = 1;

    String TABLE_QUALITY_INFO           = "qualityInfoTable";

    String WORKER_ID                    = "workerId";
    String WORKER_NAME                  = "workerName";
    String ADI_CODE                      = "adiCode";
    String WEEK_NUMBER                  = "weekNumber";
    String HOUSE_NUMBER                 = "houseNumber";
    String ROW_NUMBER                   = "rowNumber";
    String COMMENTS                     = "comments";
    String QUALITY_INFO_STATUS          = "qualityInfoStaus";
    String CREATE_DATE_TIME             = "createdDateTime";
    String QUALITY_DATA1                = "qualityData1";
    String QUALITY_DATA2                = "qualityData2";
    String QUALITY_DATA3                = "qualityData3";
    String QUALITY_DATA4                = "qualityData4";
    String QUALITY_DATA5                = "qualityData5";
    String QUALITY_DATA6                = "qualityData6";
    String QUALITY_DATA7                = "qualityData7";
    String QUALITY_DATA8                = "qualityData8";
    String AUDITOR_NAME                 = "auditorName";
    String QUALITY_PERCENT              = "qualityPercent";
    String JOB_NAME                     = "jobName";


    String CREATE_TABLE_QUALITY_INFO    =   "CREATE TABLE IF NOT EXISTS "+TABLE_QUALITY_INFO +" (" +
            WORKER_ID                      +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            WORKER_NAME                    +" TEXT, " +
            ADI_CODE                       +" TEXT, " +
            WEEK_NUMBER                    +" TEXT, " +
            HOUSE_NUMBER                   +" TEXT, " +
            ROW_NUMBER                     +" TEXT, " +
            COMMENTS                       +" TEXT, " +
            QUALITY_INFO_STATUS            +" TEXT, " +
            CREATE_DATE_TIME               +" TEXT, " +
            QUALITY_DATA1                  +" TEXT, " +
            QUALITY_DATA2                  +" TEXT, " +
            QUALITY_DATA3                  +" TEXT, " +
            QUALITY_DATA4                  +" TEXT, " +
            QUALITY_DATA5                  +" TEXT, " +
            QUALITY_DATA6                  +" TEXT, " +
            QUALITY_DATA7                  +" TEXT, " +
            QUALITY_DATA8                  +" TEXT, " +
            AUDITOR_NAME                   +" TEXT, " +
            QUALITY_PERCENT                +" TEXT, " +
            JOB_NAME                       +" TEXT )";

}

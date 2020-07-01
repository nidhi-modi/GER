package com.tandg.qualitysheet.webservice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SpreadsheetWebService {

    @POST("1FAIpQLSdI43ATa34tXHnfTdPgs30LRtCQtBCUx5u7yQaPu0QZRZqqWA/formResponse")
    @FormUrlEncoded
    Call<Void> completeQualityQuestionnaireV2(
            @Field("entry.880516821")  String workerId,
            @Field("entry.1430492217") String jobName,
            @Field("entry.373744288")  String auditorName,
            @Field("entry.389878221")  String houseNumber,
            @Field("entry.247279399")  String weekNumber,
            @Field("entry.668900209")  String workerName,
            @Field("entry.760976391")  String adiCode,
            @Field("entry.1994273709") String rowNumber,
            @Field("entry.1363445947") String qualityData1,
            @Field("entry.790697825")  String qualityData2,
            @Field("entry.1921784399") String qualityData3,
            @Field("entry.806686809")  String qualityData4,
            @Field("entry.479203985")  String qualityData5,
            @Field("entry.1498841540") String qualityData6,
            @Field("entry.488158984")  String qualityData7,
            @Field("entry.57874806")   String qualityData8,
            @Field("entry.1399381046") String comments,
            @Field("entry.1369821809") String qualityPercent
    );


    @POST("1FAIpQLSfbkxkJxNSLBDL7wWHAXIf5SLZ9RIUwnyasdaXQBYh-PmDpMg/formResponse")
    @FormUrlEncoded
    Call<Void> completeQualityQuestionnaireV3(
            @Field("entry.1014242298")   String workerId,
            @Field("entry.1215674826")   String jobName,
            @Field("entry.881361054")    String auditorName,
            @Field("entry.817227060")    String houseNumber,
            @Field("entry.750881026")    String weekNumber,
            @Field("entry.1705406993")   String workerName,
            @Field("entry.514936776")    String adiCode,
            @Field("entry.343561759")    String rowNumber,
            @Field("entry.1115241264")   String qualityData1,
            @Field("entry.325239959")    String qualityData2,
            @Field("entry.1824794340")   String qualityData3,
            @Field("entry.1578285996")   String qualityData4,
            @Field("entry.1954888983")   String qualityData5,
            @Field("entry.1824059317")   String qualityData6,
            @Field("entry.58232539")     String qualityData7,
            @Field("entry.1124881341")   String qualityData8,
            @Field("entry.2033216696")   String comments,
            @Field("entry.1457383390")   String qualityPercent
    );

}

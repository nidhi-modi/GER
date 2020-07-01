package com.tandg.qualitysheet.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 5/06/20.
 */

public class QualityInfo implements Parcelable {

    private int     workerId;
    private String  workerName;
    private String  adiCode;
    private String  weekNumber;
    private String  houseNumber;
    private String  rowNumber;
    private String  comments;
    private String  qualityInfoStaus;   // flag for local/ sv
    private String  createdDateTime;
    private String  qualityData1;
    private String  qualityData2;
    private String  qualityData3;
    private String  qualityData4;
    private String  qualityData5;
    private String  qualityData6;
    private String  qualityData7;
    private String  qualityData8;
    private String  auditorName;
    private String  jobName;
    private String  qualityPercent;
    private boolean isExapanded;



    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getQualityInfoStaus() {
        return qualityInfoStaus;
    }

    public void setQualityInfoStaus(String qualityInfoStaus) {
        this.qualityInfoStaus = qualityInfoStaus;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getQualityData1() {
        return qualityData1;
    }

    public void setQualityData1(String qualityData1) {
        this.qualityData1 = qualityData1;
    }

    public String getQualityData2() {
        return qualityData2;
    }

    public void setQualityData2(String qualityData2) {
        this.qualityData2 = qualityData2;
    }

    public String getQualityData3() {
        return qualityData3;
    }

    public void setQualityData3(String qualityData3) {
        this.qualityData3 = qualityData3;
    }

    public String getQualityData4() {
        return qualityData4;
    }

    public void setQualityData4(String qualityData4) {
        this.qualityData4 = qualityData4;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getQualityPercent() {
        return qualityPercent;
    }

    public void setQualityPercent(String qualityPercent) {
        this.qualityPercent = qualityPercent;
    }

    public boolean isExapanded() {
        return isExapanded;
    }

    public void setExapanded(boolean exapanded) {
        isExapanded = exapanded;
    }

    public String getAdiCode() {
        return adiCode;
    }

    public void setAdiCode(String adiCode) {
        this.adiCode = adiCode;
    }

    public String getQualityData5() {
        return qualityData5;
    }

    public void setQualityData5(String qualityData5) {
        this.qualityData5 = qualityData5;
    }

    public String getQualityData6() {
        return qualityData6;
    }

    public void setQualityData6(String qualityData6) {
        this.qualityData6 = qualityData6;
    }

    public String getQualityData7() {
        return qualityData7;
    }

    public void setQualityData7(String qualityData7) {
        this.qualityData7 = qualityData7;
    }

    public String getQualityData8() {
        return qualityData8;
    }

    public void setQualityData8(String qualityData8) {
        this.qualityData8 = qualityData8;
    }

    public QualityInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.workerId);
        dest.writeString(this.workerName);
        dest.writeString(this.adiCode);
        dest.writeString(this.weekNumber);
        dest.writeString(this.houseNumber);
        dest.writeString(this.rowNumber);
        dest.writeString(this.comments);
        dest.writeString(this.qualityInfoStaus);
        dest.writeString(this.createdDateTime);
        dest.writeString(this.qualityData1);
        dest.writeString(this.qualityData2);
        dest.writeString(this.qualityData3);
        dest.writeString(this.qualityData4);
        dest.writeString(this.qualityData5);
        dest.writeString(this.qualityData6);
        dest.writeString(this.qualityData7);
        dest.writeString(this.qualityData8);
        dest.writeString(this.auditorName);
        dest.writeString(this.jobName);
        dest.writeString(this.qualityPercent);
        dest.writeByte(this.isExapanded ? (byte) 1 : (byte) 0);
    }

    protected QualityInfo(Parcel in) {
        this.workerId = in.readInt();
        this.workerName = in.readString();
        this.adiCode = in.readString();
        this.weekNumber = in.readString();
        this.houseNumber = in.readString();
        this.rowNumber = in.readString();
        this.comments = in.readString();
        this.qualityInfoStaus = in.readString();
        this.createdDateTime = in.readString();
        this.qualityData1 = in.readString();
        this.qualityData2 = in.readString();
        this.qualityData3 = in.readString();
        this.qualityData4 = in.readString();
        this.qualityData5 = in.readString();
        this.qualityData6 = in.readString();
        this.qualityData7 = in.readString();
        this.qualityData8 = in.readString();
        this.auditorName = in.readString();
        this.jobName = in.readString();
        this.qualityPercent = in.readString();
        this.isExapanded = in.readByte() != 0;
    }

    public static final Creator<QualityInfo> CREATOR = new Creator<QualityInfo>() {
        @Override
        public QualityInfo createFromParcel(Parcel source) {
            return new QualityInfo(source);
        }

        @Override
        public QualityInfo[] newArray(int size) {
            return new QualityInfo[size];
        }
    };
}

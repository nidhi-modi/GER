package com.tandg.qualitysheet.listeners;


/**
 * Created by root on 08/06/20.
 */

public interface ViewCallback {

    void sendDataAuditorFragments(String auditorName);
    void sendDataHouseFragments(String houseNo);
    void sendDataWeekFragments(String weekNo);


}

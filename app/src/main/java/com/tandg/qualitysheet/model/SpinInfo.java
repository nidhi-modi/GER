package com.tandg.qualitysheet.model;

/**
 * Created by root on 5/06/20.
 */

public class SpinInfo {

    private int id;
    private String value;

    public SpinInfo(int id, String value){
        this.id    = id;
        this.value = value;
    }


    @Override
    public String toString(){
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

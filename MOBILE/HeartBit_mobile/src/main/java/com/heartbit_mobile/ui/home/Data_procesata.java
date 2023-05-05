package com.heartbit_mobile.ui.home;

import com.google.firebase.database.PropertyName;

import java.util.Date;

public class Data_procesata {
    @PropertyName("Tip_data")
    private String tip_data;
    @PropertyName("Time_stamp")
    private Date time_stamp;

    public Data_procesata() {
    }

    public Data_procesata(String tip_data, Date time_stamp) {
        this.tip_data = tip_data;
        this.time_stamp = time_stamp;
    }

    @PropertyName("Tip_data")
    public String getTip_data() {
        return tip_data;
    }

    @PropertyName("Tip_data")
    public void setTip_data(String tip_data) {
        this.tip_data = tip_data;
    }

    @PropertyName("Time_stamp")
    public Date getTime_stamp() {
        return time_stamp;
    }

    @PropertyName("Time_stamp")
    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }
}

package com.heartbit_mobile.ui.calendar;

import com.google.firebase.database.PropertyName;

public class Recomandare {
    @PropertyName("date")
    private String data_recomandare;
    @PropertyName("text")
    private String text;

    public Recomandare() {

    }

    public Recomandare(String data_recomandare, String text) {
        this.data_recomandare = data_recomandare;
        this.text = text;
    }

    @PropertyName("date")
    public String getData_recomandare() {
        return data_recomandare;
    }

    @PropertyName("date")
    public void setData_recomandare(String data_recomandare) {
        this.data_recomandare = data_recomandare;
    }

    @PropertyName("text")
    public String getText() {
        return text;
    }

    @PropertyName("text")
    public void setText(String text) {
        this.text = text;
    }
}

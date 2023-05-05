package com.heartbit_mobile.ui.home;

import com.google.firebase.database.PropertyName;

import java.util.Date;

public class Data_procesata {
    @PropertyName("Denumire")
    private String denumire;
    @PropertyName("Time_stamp")
    private Date time_stamp;
    @PropertyName("Identificator")
    private String identificator;
    @PropertyName("Alerta")
    private boolean alerta;
    @PropertyName("Valoare")
    private float valoare;

    public Data_procesata() {
    }

    public Data_procesata(String identificator, Date time_stamp, float valoare) {
        this.identificator = identificator;
        switch (identificator) {
            case "EKG":
                denumire = "EKG sensor";
                break;
            case "UMD":
                denumire = "Umidity sensor";
                break;
            case "TEMP":
                denumire = "Temperature sensor";
                break;
            case "PULS":
                denumire = "Pulse sensor";
                break;
        }
        this.time_stamp = time_stamp;
        this.alerta = false;
        this.valoare = valoare;
    }

    @PropertyName("Denumire")
    public String getDenumire() {
        return denumire;
    }

    @PropertyName("Denumire")
    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    @PropertyName("Time_stamp")
    public Date getTime_stamp() {
        return time_stamp;
    }

    @PropertyName("Time_stamp")
    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    @PropertyName("Identificator")
    public String getIdentificator() {
        return identificator;
    }

    @PropertyName("Identificator")
    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    @PropertyName("Alerta")
    public boolean getAlerta() {
        return alerta;
    }

    @PropertyName("Denumire")
    public void setAlerta(boolean alerta) {
        this.alerta = alerta;
    }

    @PropertyName("Valoare")
    public void setValoare(float valoare) {
        this.valoare = valoare;
    }

    @PropertyName("Valoare")
    public float getValoare() {
        return valoare;
    }
}

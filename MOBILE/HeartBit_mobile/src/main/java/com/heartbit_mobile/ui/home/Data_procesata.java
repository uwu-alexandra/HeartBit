package com.heartbit_mobile.ui.home;

import com.google.firebase.database.PropertyName;

public class Data_procesata {
    @PropertyName("Denumire")
    private String denumire;
    @PropertyName("Time_stamp")
    private String time_stamp;
    @PropertyName("Identificator")
    private String identificator;
    @PropertyName("Alerta")
    private boolean alerta;
    @PropertyName("Valoare")
    private float valoare;

    public Data_procesata() {
    }

    public Data_procesata(String identificator, String time_stamp, float valoare,boolean alerta) {
        this.identificator = identificator;
        switch (identificator) {
            case "EKG":
                this.denumire = "EKG sensor";
                break;
            case "UMD":
                this.denumire = "Umidity sensor";
                break;
            case "TEMP":
                this.denumire = "Temperature sensor";
                break;
            case "PULS":
                this.denumire = "Pulse sensor";
                break;
            default: this.denumire="EROARE";
        }
        this.time_stamp = time_stamp;
        this.alerta = alerta;
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
    public String getTime_stamp() {
        return time_stamp;
    }

    @PropertyName("Time_stamp")
    public void setTime_stamp(String time_stamp) {
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

    @PropertyName("Alerta")
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

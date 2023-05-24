package com.heartbit_mobile.ui.support;

import com.google.firebase.database.PropertyName;

public class Solicitare {
    @PropertyName("Motiv_solicitare")
    private String motiv_solicitare;

    @PropertyName("Detalii_solicitare")
    private String detalii_solicitare;

    public Solicitare(){

    }

    public Solicitare(String motiv_solicitare, String detalii_solicitare) {
        this.motiv_solicitare = motiv_solicitare;
        this.detalii_solicitare = detalii_solicitare;
    }
    @PropertyName("Motiv_solicitare")
    public String getMotiv_solicitare() {
        return motiv_solicitare;
    }
    @PropertyName("Motiv_solicitare")
    public void setMotiv_solicitare(String motiv_solicitare) {
        this.motiv_solicitare = motiv_solicitare;
    }
    @PropertyName("Detalii_solicitare")
    public String getDetalii_solicitare() {
        return detalii_solicitare;
    }
    @PropertyName("Detalii_solicitare")
    public void setDetalii_solicitare(String detalii_solicitare) {
        this.detalii_solicitare = detalii_solicitare;
    }
}

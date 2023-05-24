package com.heartbit_mobile.ui.calendar;

import com.google.firebase.database.PropertyName;

public class Programare {
    @PropertyName("Data_programare")
    private String Data_programare;

    @PropertyName("Nume_pacient")
    private String Nume_pacient;

    @PropertyName("Prenume_pacient")
    private String Prenume_pacient;
    @PropertyName("locatia")
    private String locatia;
    @PropertyName("Nume_medic")
    private String Nume_medic;

    public Programare(){

    }

    public Programare(String data,String locatia,String nume,String prenume){
        this.Data_programare=data;
        this.locatia=locatia;
        this.Nume_pacient=nume;
        this.Prenume_pacient=prenume;
    }

    public Programare(String data,String locatia,String medic,String nume,String prenume) {
        this.Data_programare=data;
        this.locatia=locatia;
        this.Nume_medic=medic;
        this.Nume_pacient=nume;
        this.Prenume_pacient=prenume;
    }
    @PropertyName("Data_programare")
    public String getData() {
        return Data_programare;
    }

    @PropertyName("Data_programare")
    public void setData(String data) {
        this.Data_programare = data;
    }

    @PropertyName("locatia")
    public String getLocatia() {
        return locatia;
    }

    @PropertyName("locatia")
    public void setLocatia(String locatia) {
        this.locatia = locatia;
    }

    @PropertyName("Nume_pacient")
    public String getNume_pacient() {
        return Nume_pacient;
    }

    @PropertyName("Nume_pacient")
    public void setNume_pacient(String nume) {
        this.Nume_pacient = nume;
    }

    @PropertyName("Prenume_pacient")
    public String getPrenume_pacient() {
        return Prenume_pacient;
    }

    @PropertyName("Prenume_pacient")
    public void setPrenume_pacient(String prenume) {
        this.Prenume_pacient = prenume;
    }

    @PropertyName("Nume_medic")
    public String getNume_medic() {
        return Nume_medic;
    }

    @PropertyName("Nume_medic")
    public void setNume_medic(String medic) {
        this.Nume_medic = medic;
    }

    @Override
    public String toString() {
        return "Data programării: " + Data_programare + "\n" +
                "Locatia: '" + locatia;
    }
}

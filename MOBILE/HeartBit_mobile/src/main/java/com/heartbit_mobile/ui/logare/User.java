package com.heartbit_mobile.ui.logare;

import com.google.firebase.database.PropertyName;

public class User {
    @PropertyName("nume")
    private String nume;
    @PropertyName("prenume")
    private String prenume;
    @PropertyName("cnp")
    private String cnp;
    @PropertyName("email")
    private String email;
    @PropertyName("password")
    private String password;

    public User() {

    }

    public User(String email, String password, String nume, String prenume, String cnp) {
        this.email = email;
        this.password = password;
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
    }

    @PropertyName("nume")
    public String getNume() {
        return nume;
    }

    @PropertyName("nume")
    public void setNume(String nume) {
        this.nume = nume;
    }

    @PropertyName("prenume")
    public String getPrenume() {
        return prenume;
    }

    @PropertyName("prenume")
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    @PropertyName("cnp")
    public String getCnp() {
        return cnp;
    }

    @PropertyName("cnp")
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName("password")
    public String getPassword() {
        return password;
    }

    @PropertyName("password")
    public void setPassword(String password) {
        this.password = password;
    }
}

package com.heartbit_mobile.ui.logare;

public class User {
    private String nume;
    private String prenume;
    private String cnp;
    private String email;

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
}

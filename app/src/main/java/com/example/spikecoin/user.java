package com.example.spikecoin;

public class user {

    String email,password, fullName, bal;

    public user() {
    }

    public user(String email, String password, String fullName, String Bal) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.bal = bal;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBal() {
        return bal;
    }
}

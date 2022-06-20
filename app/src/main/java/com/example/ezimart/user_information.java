package com.example.ezimart;

import java.io.Serializable;

public class user_information implements Serializable {

public  user_information(){

}
    private static String username;
    private static String password;
    private static String full_name;
    private static String email;
    private static String mobile_number;
    private static String residential_address;

    public String getUsername() {
        return username;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getResidential_address() {
        return residential_address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public void setResidential_address(String residential_address) {
        this.residential_address = residential_address;
    }

    public user_information(String username, String full_name,String password, String email,  String mobile_number, String residential_address) {
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.residential_address = residential_address;
    }



}

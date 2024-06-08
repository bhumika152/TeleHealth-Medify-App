package com.example.myapplication.models;
public class Doctors {

    public Doctors(){

    }
    String name,dob,speciality;
    boolean is_verified;
    public Doctors(String name, String dob, String speciality) {
        this.name = name;
        this.dob = dob;
        this.speciality = speciality;
        this.is_verified = false;
    }
    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getSpeciality() {
        return speciality;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

}

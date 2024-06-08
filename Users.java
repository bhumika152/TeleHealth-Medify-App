package com.example.myapplication.models;

public class Users {
    String name;
    String id;
    String dob;
    public  Users(){}
    public Users(String name, String id, String dob) {
        this.name = name;
        this.id = id;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

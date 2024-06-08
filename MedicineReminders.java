package com.example.myapplication.models;
import java.util.ArrayList;
public class MedicineReminders {
    String name;
    int amount;
    int dose;
    String frequency;
    String reminders;
    String username;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public MedicineReminders(){

    }
    public MedicineReminders(String name, int amount, int dose, String frequency, String reminders, String username, String id) {
        this.name = name;
        this.amount = amount;
        this.dose = dose;
        this.frequency = frequency;
        this.reminders = reminders;
        this.username = username;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReminders() {
        return reminders;
    }

    public void setReminders(String reminders) {
        this.reminders = reminders;
    }
}

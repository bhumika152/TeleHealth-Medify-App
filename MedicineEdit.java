package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.myapplication.receivers.AlarmReceiver;
import com.example.myapplication.models.MedicineReminders;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Calendar;

public class MedicineEdit extends AppCompatActivity {
    static final int ALARM_REQ = 100;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button submitB;
    TextView timeview;
    EditText name;
    EditText amount;
    String key = null;
    EditText dose;
    Spinner freq;
    Calendar calendar;
    AlarmManager alarmManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String[] freqency = {"Everyday","Every 2 days","Twice a week","Once a week"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_edit);
        getSupportActionBar().hide();
        Spinner spinner = findViewById(R.id.freq);
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        createNotificationChannel();
        ArrayAdapter<CharSequence> adapter=new ArrayAdapter<>(MedicineEdit.this,android.R.layout.simple_spinner_item, freqency);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        timeview = findViewById(R.id.m_set_time);
        name = findViewById(R.id.m_med_name);
        amount = findViewById(R.id.m_amt);
        dose = findViewById(R.id.m_dose);
        freq = findViewById(R.id.freq);
        String medcine = sharedPreferences.getString("current_med",null);
        if(medcine!=null){
            Gson gson = new Gson();
            MedicineReminders rem =  gson.fromJson(medcine,MedicineReminders.class);
            timeview.setText(rem.getReminders());
            name.setText(rem.getName());
            amount.setText(String.valueOf(rem.getAmount()));
            dose.setText(String.valueOf(rem.getDose()));
            key = rem.getId();
        }
        submitB = findViewById(R.id.new_med_button);
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
        timeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTimeDilog();
            }
        });
    }
    public void getTimeDilog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(MedicineEdit.this , new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                boolean isPM = (hours>= 12);
                timeview.setText(String.format("%02d:%02d %s", (hours == 12 || hours == 0) ? 12 : hours % 12, minute, isPM ? "PM" : "AM"));
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
            }
        },12,00,false);
        timePickerDialog.show();
    }
    private  void  onSubmit(){
        setAlarm();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("reminders");
        if(key==null){
            key = reference.push().getKey();
        }
        String time = timeview.getText().toString();
        String medname = name.getText().toString();
        String amt = amount.getText().toString();
        String dse = dose.getText().toString();
        String freqs = freq.getSelectedItem().toString();
        String username=  getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("name","User");
        MedicineReminders rem = new MedicineReminders(medname,Integer.parseInt(amt),Integer.parseInt(dse),freqs,time,username,key);
        reference.child(key).setValue(rem);
        goToMed();
    }
    private void  goToMed(){
        editor.putString("current_med",null);
        editor.commit();
        Intent intent = new Intent(MedicineEdit.this,MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("tab", 2);
        intent.putExtras(b);
        startActivity(intent);
    }
    public void  backB(View view){
        goToMed();
    }

    public void setAlarm(){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent  iBroadCast = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,iBroadCast,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pi);
    }
    private void createNotificationChannel(){
            CharSequence name = "channel";
            String desc = "Notification Channel";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("medical1", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }
}
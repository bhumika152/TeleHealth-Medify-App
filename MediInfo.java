package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.models.MedicineReminders;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class MediInfo extends AppCompatActivity {
    MedicineReminders rem;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_medi_info);
        Gson gson = new Gson();
        sharedPreferences= getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String json = sharedPreferences.getString("current_med",null);
        rem =  gson.fromJson(json,MedicineReminders.class);
        ((TextView)findViewById(R.id.m_info_name)).setText(rem.getName());
        ((TextView)findViewById(R.id.m_info_amt)).setText(rem.getAmount()+" pills");
        ((TextView)findViewById(R.id.m_info_dose)).setText(rem.getDose()+" mg");
        ((TextView)findViewById(R.id.m_info_freq)).setText(rem.getFrequency());
        ((TextView)findViewById(R.id.m_info_rem)).setText(rem.getReminders());
        ((ImageButton)findViewById(R.id.backinfo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMed();
            }
        });
    }
    private void  goToMed(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_med",null);
        Intent intent = new Intent(MediInfo.this,MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("tab", 2);
        intent.putExtras(b);
        startActivity(intent);
    }
}
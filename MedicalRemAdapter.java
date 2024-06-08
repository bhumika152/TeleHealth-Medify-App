package com.example.myapplication.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MediInfo;
import com.example.myapplication.MedicineEdit;
import com.example.myapplication.R;
import com.example.myapplication.models.MedicineReminders;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;
public class MedicalRemAdapter extends ArrayAdapter<MedicineReminders> {
    ImageButton edit,info,remove1;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference reference;
    Activity a;
    public MedicalRemAdapter(Context context, List<MedicineReminders> reminders,Activity a) {
        super(context, 0,reminders);
        this.a = a;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MedicineReminders reminder = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medical_view_main,parent,false);
        }
        TextView name = convertView.findViewById(R.id.main_med1);
        TextView time = convertView.findViewById(R.id.main_time1);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("reminders");
        edit = convertView.findViewById(R.id.med_edit);
        info= convertView.findViewById(R.id.med_info);
        remove1= convertView.findViewById(R.id.med_rm);
        name.setText(reminder.getName());
        time.setText(reminder.getReminders());
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItemStorage(reminder);
                Intent intent  = new Intent(a, MediInfo.class);
                getContext().startActivity(intent);

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItemStorage(reminder);
                Intent intent  = new Intent(a, MedicineEdit.class);
                getContext().startActivity(intent);
            }
        });
        remove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = reminder.getId();
                reference.child(id).removeValue();
                remove(reminder);
                Intent intent = new Intent(a,MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("tab", 2);
                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
    public void setItemStorage(MedicineReminders medicineReminder){
        sharedPreferences = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(medicineReminder);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_med",json);
        editor.commit();
    }
}

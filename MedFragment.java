package com.example.myapplication.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myapplication.MedicineEdit;
import com.example.myapplication.R;
import com.example.myapplication.adapters.MedicalRemAdapter;
import com.example.myapplication.dilogs.LoaderDilog;
import com.example.myapplication.models.MedicineReminders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class  MedFragment extends Fragment {
    private View rootView;
    ListView listView;
    LoaderDilog loaderDilog;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public MedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Activity a = getActivity();
        loaderDilog = new LoaderDilog(a);
        rootView = inflater.inflate(R.layout.fragment_med, container, false);
        sharedPreferences = a.getSharedPreferences("PREFERENCE",a.MODE_PRIVATE);
        String username=  sharedPreferences.getString("name","User");
        editor =sharedPreferences.edit();
        Gson gson = new Gson();
        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("reminders");
        Query remr = reference.orderByChild("username").equalTo(username);
        List<MedicineReminders> reminders = new ArrayList<>();
        loaderDilog.startDilog();
        remr.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if(snapshot.exists()){
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MedicineReminders mer= dataSnapshot.getValue(MedicineReminders.class);
                    reminders.add(mer);
                }
                String json = gson.toJson(reminders);
                editor.putString("medicine_data",json);
                editor.commit();
            }
            if(reminders.size()==0){
                replaceFragment(new NoMed());
                editor.putString("medicine_data",null);
                editor.commit();
            }else{
                    listView =(ListView) rootView.findViewById(R.id.medicineReminderList);
                    inflateReminder(reminders);
                }
            loaderDilog.endDilog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }
        );
        ImageButton button = rootView.findViewById(R.id.addmed);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MedicineEdit.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
    public void inflateReminder(List<MedicineReminders> reminders){
        MedicalRemAdapter adapter = new MedicalRemAdapter(getContext(),reminders,getActivity());
        listView.setAdapter(adapter);
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.r_med_frame,fragment);
        fragmentTransaction.commit();
    }
}
package com.example.myapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.UserChatAdapter;
import com.example.myapplication.models.Doctors;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class ConsultFragment extends Fragment {
    ListView listView;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference reference;
    FirebaseDatabase database;
    DatabaseReference dreference;
    Activity a;
    public ConsultFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        a = getActivity();
        View rootView =  inflater.inflate(R.layout.fragment_consult, container, false);
        listView = rootView.findViewById(R.id.user_conlist);
        Gson gson = new Gson();
        List<Pair<String,Doctors>> doctors = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        database  = FirebaseDatabase.getInstance();
        dreference = database.getReference("doctors");
        dreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                Doctors doctor =   dataSnapshot.child("info").getValue(Doctors.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                doctors.add(new Pair<>(id,doctor));
                }
                inflateReminder(doctors);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  rootView;
    }
    public void inflateReminder(List<Pair<String,Doctors>> chatrooms){
        UserChatAdapter adapter = new UserChatAdapter(getContext(),chatrooms,getActivity(),reference);
        listView.setAdapter(adapter);
    }
}
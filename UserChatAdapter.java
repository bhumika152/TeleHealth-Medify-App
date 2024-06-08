package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.ChatDoctorActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.Doctors;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UserChatAdapter extends ArrayAdapter<Pair<String,Doctors>> {
    Activity a;
    StorageReference reference;
    StorageReference riversRef;

    public UserChatAdapter(Context context, List<Pair<String,Doctors>> objects, Activity a, StorageReference reference) {
        super(context, 0, objects);
        this.a =  a;
        this.reference = reference;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair<String,Doctors> map = getItem(position);
        String id = map.first;
        Doctors room = map.second;
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_view_patient,parent,false);
        }
        TextView name = convertView.findViewById(R.id.dnamec);
        TextView spec = convertView.findViewById(R.id.dspeci);
        name.setText(room.getName());
        spec.setText(room.getSpeciality());
        ImageView profile_pic = convertView.findViewById(R.id.profileppi);
        setImage(id,profile_pic);
        Button c_button = convertView.findViewById(R.id.chat_u);
        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat(room.getName(),room.getSpeciality(),id);
            }
        });
;       return convertView;
    }
    private  void  startChat(String name,String spec,String id){
        SharedPreferences sharedPreferences = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("dname",name);
        editor.putString("dspec",spec);
        editor.putString("did",id);
        editor.apply();
        Intent intent = new Intent(getContext(), ChatDoctorActivity.class);
        a.startActivity(intent);
    }
    private  void setImage(String id,ImageView profile_pic){
        riversRef = reference.child("images/"+id+".jpg");
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_pic);
            }
        });
    }
}

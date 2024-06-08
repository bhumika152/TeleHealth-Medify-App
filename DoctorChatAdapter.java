package com.example.myapplication.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.ChatDoctorActivity;
import com.example.myapplication.R;
import com.example.myapplication.SendUserActivity;
import com.example.myapplication.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorChatAdapter extends ArrayAdapter<Users> {
    Activity a;
    StorageReference reference;
    StorageReference riversRef;
    public DoctorChatAdapter(Context context, List<Users> objects, Activity a,StorageReference reference) {
        super(context, 0, objects);
        this.reference = reference;
        this.a =  a;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Users room = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_view_doctor,parent,false);
        }
        TextView name = convertView.findViewById(R.id.usernaemc);
        name.setText(room.getName());
        ImageView profile_pic = convertView.findViewById(R.id.profile_ppp);
        setImage(room.getId(),profile_pic);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat(room.getName(),room.getId());
            }
        });
        return convertView;
    }
    private  void  startChat(String name,String id){
        SharedPreferences sharedPreferences = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("uname",name);
        editor.putString("uid",id);
        editor.apply();
        Intent intent = new Intent(getContext(), SendUserActivity.class);
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

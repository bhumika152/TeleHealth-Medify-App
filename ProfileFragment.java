package com.example.myapplication.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.SignupActivity;
import com.example.myapplication.dilogs.LoaderDilog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class ProfileFragment extends Fragment {
    Activity a;
    public Uri  imageUri;
    private View rootView;
    TextView Name,DOB,Email,Age;
    FirebaseAuth auth;
    ImageView profile_pic;

    FirebaseStorage storage;
    StorageReference reference;
    StorageReference riversRef;
    LoaderDilog loaderDilog;
    FirebaseUser user;
    Button logout;
    SharedPreferences.Editor editor;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        a = getActivity();
        loaderDilog = new LoaderDilog(a);
        user = FirebaseAuth.getInstance().getCurrentUser();
        editor = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE).edit();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        String getName = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE).getString("name","User");
        String getEmail = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE).getString("email","abc@mail.com");
        String getDOB = a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE).getString("dob","11/01/2004");
        Name =   rootView.findViewById(R.id.name_profile_page);
        Email =   rootView.findViewById(R.id.email_profile);
        DOB =   rootView.findViewById(R.id.dob_profile);
        Age =   rootView.findViewById(R.id.age_profile);
        profile_pic = rootView.findViewById(R.id.profile_pic_p);
        storage = FirebaseStorage.getInstance();
        reference  = storage.getReference();
        logout = rootView.findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogOut(view);
            }
        });
        String imguri =  a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE).getString("imageuri",null);
        if(imguri!=null) {
            Picasso.get().load(Uri.parse(imguri)).into(profile_pic);
        }else{
                setImage();
            };

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeImage();
            }
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dob_s = LocalDate.parse(getDOB,formatter);
        String getAge = Integer.toString(calculateAge(dob_s));
        Name.setText(getName);
        Email.setText(getEmail);
        DOB.setText(getDOB);
        Age.setText(getAge);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==1) && (resultCode==RESULT_OK) && data!=null && data.getData()!=null){
            imageUri  = data.getData();
            uploadPicture();
        }
    }

    public void onLogOut(View view){
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        SharedPreferences sharedPreferences =  a.getSharedPreferences("PREFERENCE", a.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("imageuri");
        editor.remove("spec");
        editor.apply();
        Intent intent = new Intent(a, SignupActivity.class);
        startActivity(intent);
    }

    private void uploadPicture() {
        loaderDilog.startDilog();
        StorageReference riversRef = reference.child("images/"+user.getUid()+".jpg");
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask. TaskSnapshot>() {
                    @Override
                    public void onSuccess (UploadTask.TaskSnapshot taskSnapshot) {
                        setImage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception exception) {
                        Toast.makeText(getActivity(),"Profile Not Updated",Toast.LENGTH_LONG);
                    }
                });
    }

    private void onChangeImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    public int calculateAge(LocalDate dob_s)
    {
        LocalDate curDate = LocalDate.now();
        if ((dob_s != null) && (curDate != null))
        {
            return Period.between(dob_s, curDate).getYears();
        }
        else
        {
            return 0;
        }
    }
    private  void setImage(){
        riversRef = reference.child("images/"+user.getUid()+".jpg");
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_pic);
                editor.putString("imageuri",uri.toString());
                editor.apply();
            }
        });
    }
}
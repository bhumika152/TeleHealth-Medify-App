package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.dilogs.LoaderDilog;
import com.example.myapplication.models.Doctors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoctorJoin extends AppCompatActivity {
    LoaderDilog loaderDilog;
    EditText name_signup,email_signup,pass_signup;
    String id;
    TextView dob_signup,files;
    Button apply_b;
    ImageButton back_b;
    Spinner spinner;
    private static final int REQUEST_CODE = 123;
    private StorageReference mStorageRef;
    ArrayList<Uri> uris;
    FirebaseAuth auth;
    FirebaseUser authUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    String[] speci = {"Select your Speciality","Allergy and immunology" ,"Anesthesiology" ,"Dermatology" ,"Diagnostic radiology" ,"Emergency medicine" ,"Family medicine" ,"Internal medicine" ,"Medical genetics" ,"Neurology" ,"Nuclear medicine" ,"Obstetrics and gynecology" ,"Ophthalmology" ,"Pathology" ,"Pediatrics" ,"Physical medicine and rehabilitation" ,"Preventive medicine" ,"Psychiatry" ,"Radiation oncology" ,"Surgery" ,"Urology"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor_join);
        loaderDilog = new LoaderDilog(this);
        uris = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        spinner = findViewById(R.id.speciality);
        name_signup = findViewById(R.id.d_name);
        email_signup = findViewById(R.id.d_email);
        pass_signup = findViewById(R.id.d_pass);
        apply_b = findViewById(R.id.apply_d);
        back_b = findViewById(R.id.backdoc);
        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorJoin.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        ArrayAdapter<CharSequence> adapter=new ArrayAdapter<>(DoctorJoin.this,android.R.layout.simple_spinner_item, speci);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dob_signup = findViewById(R.id.d_dob);
        files = findViewById(R.id.d_files);
        dob_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDOBPick();
            }
        });
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Files"), REQUEST_CODE);
            }
        });
        apply_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onApply();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == DoctorLoginActivity.RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        uris.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else if (data.getData() != null) {
                    uris.add(data.getData());
                }
            }
        }
    }

    private void onDOBPick(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String m1,d1;
                if(month+1<10){
                    m1 = "0"+(month+1);
                }else{
                    m1 = String.valueOf(month+1);
                }
                if(dayOfMonth<10){
                    d1 = "0"+(dayOfMonth);
                }else{
                    d1 = String.valueOf(dayOfMonth);
                }
                String date = d1 + "/" + m1 + "/" + year;
                dob_signup.setText(date);
            }
        },
                2024, 0, 1);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    void onApply(){
        loaderDilog.startDilog();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("doctors");
        auth = FirebaseAuth.getInstance();
        String name = name_signup.getText().toString();
        String email =email_signup.getText().toString()  ;
        String password =pass_signup.getText().toString()  ;
        String dob =dob_signup.getText().toString();
        String spec = spinner.getSelectedItem().toString();
        if(name.isEmpty()) {
            name_signup.setError("Name can't be empty");
        }
        if(email.isEmpty()) {
            email_signup.setError("Email can't be empty");
        }
        if(dob.isEmpty()) {
            dob_signup.setError("DOB can't be empty");
        }
        if(password.isEmpty()) {
            pass_signup.setError("Password can't be empty");
        }
        if(spec=="Select your Speciality"){
            showToast("Please, Select a Speciality");
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        authUser = auth.getCurrentUser();
                        addDoctor(dob,name,spec);
                        }
                        });
                    }else{
                        showToast("Invalid Credentials");
                    }
                }
            });
        }
    }

    private void  uploadFiles(){
        for (Uri uri : uris) {
            uploadFile(uri);
        }
    }

    private void  addDoctor(String dob,String name,String spec){
        id = authUser.getUid();
        Doctors doctor = new Doctors(name,dob,spec);
        Map<String,Object> slots = new HashMap<>();
        slots.put("info",doctor);
        slots.put("id",id);
        reference.child(id).setValue(slots);
        uploadFiles();
        showToast("Applied Successfully, Verify your Email");
        loaderDilog.endDilog();
        Intent intent = new Intent(DoctorJoin.this,DoctorLoginActivity.class);
        startActivity(intent);
    }

    private void uploadFile(Uri uri) {
        UUID uuid = UUID.randomUUID();
        StorageReference fileRef = mStorageRef.child("doctor_docs/"+id+"/"+uuid);
        fileRef.putFile(uri);
    }
    private  void  showToast(String s){
        Toast.makeText(DoctorJoin.this, s,
                Toast.LENGTH_SHORT).show();
    }

}
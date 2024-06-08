package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_NOT_CODE = 100;
    private static final int PERMISSION_REQUEST_STO_CODE = 101;
    EditText name_signup,email_signup,pass_signup;
    TextView login_redirect,dob_signup,join_as_doc;
    Button signup;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_NOT_CODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_NOT_CODE);
        }
        name_signup = findViewById(R.id.name_signup);
        email_signup = findViewById(R.id.email_signup);
        pass_signup = findViewById(R.id.password_signup);
        dob_signup = findViewById(R.id.dob_signup);
        signup = findViewById(R.id.signupbutton);
        login_redirect = findViewById(R.id.to_log);
        join_as_doc = findViewById(R.id.join_d);
        dob_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDOBPick();
            }
        });
        login_redirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        join_as_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,DoctorJoin.class);
                startActivity(intent);
            }
        });
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
    public void onSignup(View view){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        String name =name_signup.getText().toString()  ;
        String email =email_signup.getText().toString()  ;
        String password =pass_signup.getText().toString()  ;
        String dob =dob_signup.getText().toString()  ;
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
        }else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                        showToast("Verify Your Email");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        addUserAndRedirect(user,dob,name);
                                        }else{
                                            showToast("Invalid Credentials");
                                        }
                                    }
                            });
                            } else {
                                showToast("Invalid Credentials");
                            }
                        }
                    });
        }
    }
    private  void addUserAndRedirect(FirebaseUser user1,String dob,String name){
        String id = user1.getUid();
        Users user = new Users(name,id,dob);
        reference.child(id).setValue(user);
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    private  void  showToast(String s){
        Toast.makeText(SignupActivity.this, s,
                Toast.LENGTH_SHORT).show();
    }
    }
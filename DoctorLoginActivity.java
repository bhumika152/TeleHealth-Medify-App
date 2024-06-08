package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DoctorLoginActivity extends AppCompatActivity {

    TextView toULogin;
    EditText email_login,pass_login;
    Button loginB;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor_login);
        toULogin = findViewById(R.id.toulogin);
        email_login = findViewById(R.id.d_email_login);
        pass_login =findViewById(R.id.d_password_login);
        loginB = findViewById(R.id.d_login_button);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });
        toULogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DoctorLoginActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void onLogin(){
        if(validateName() & validatePass()){
            checkUser();
        }
    }
    public Boolean validateName(){
        String val = email_login.getText().toString();
        if (val.isEmpty()){
            email_login.setError("Email cannot be empty");
            return false;
        } else {
            email_login.setError(null);
            return true;
        }
    }
    public Boolean validatePass(){
        String val = pass_login.getText().toString();
        if (val.isEmpty()){
            pass_login.setError("Password cannot be empty");
            return false;
        } else {
            pass_login.setError(null);
            return true;
        }
    }
    public void checkUser(){
        String emailUser = email_login.getText().toString().trim();
        String passUser = pass_login.getText().toString().trim();
        database  = FirebaseDatabase.getInstance();
        reference = database.getReference("doctors");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(emailUser, passUser)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                setUserLogin(user);
                            }else{
                                showToast("Email is not Verified");
                            }
                        } else {
                            showToast("Invalid Credentials.");

                        }
                    }
                });
    }
    private void setUserLogin(FirebaseUser user){
        String id = user.getUid();
        Query checkUserDatabase = reference.orderByChild("id").equalTo(id);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                DataSnapshot info = snapshot.child(id).child("info");
                String name = info.child("name").getValue(String.class);
                boolean verified = info.child("is_verified").getValue(boolean.class);
                if(verified){
                String email = user.getEmail();
                String dob = info.child("dob").getValue(String.class);
                String spec = info.child("speciality").getValue(String.class);
                SharedPreferences sharedPreferences =  getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name",name);
                editor.putString("dob",dob);
                editor.putString("email",email);
                editor.putString("spec",spec);
                editor.apply();
                Intent intent = new Intent(DoctorLoginActivity.this,DoctorMainActivity.class);
                startActivity(intent);
                }else{
                    showToast("Account Not Verified");
                }
                }else{
                    showToast("Not a Doctor Account");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void  showToast(String text){
        Toast.makeText(DoctorLoginActivity.this, text,
                Toast.LENGTH_SHORT).show();
    }
}
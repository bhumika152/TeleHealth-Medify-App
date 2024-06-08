package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {
    EditText email_login,pass_login;
    TextView signup_redirect,d_redirect;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_login = findViewById(R.id.email_login);
        pass_login = findViewById(R.id.password_login);
        signup_redirect = findViewById(R.id.to_sign);
        d_redirect = findViewById(R.id.dlogin);
        d_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,DoctorLoginActivity.class);
                startActivity(intent);
            }
        });
        signup_redirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onLogin(View view){
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
        reference = database.getReference("users");
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
                                Toast.makeText(LoginActivity.this, "Email is not Verified",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials.",
                                    Toast.LENGTH_SHORT).show();
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
                    String name = snapshot.child(id).child("name").getValue(String.class);
                    String dob = snapshot.child(id).child("dob").getValue(String.class);
                    String email = user.getEmail();
                    SharedPreferences sharedPreferences =  getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name",name);
                    editor.putString("dob",dob);
                    editor.putString("email",email);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Try using Doctor Login",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

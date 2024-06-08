package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragments.ConsultFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.MedFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    String notLogined;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
            value = b.getInt("tab");
        SharedPreferences sharedPreferences =  getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        if(sharedPreferences.getString("spec",null)!=null){
            Intent intent = new Intent(MainActivity.this,DoctorMainActivity.class);
            startActivity(intent);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notLogined = sharedPreferences.getString("email", null);
        if(notLogined==null){
        Intent intent = new Intent(MainActivity.this,SignupActivity.class);
        startActivity(intent);
        }
        if(value==2){
        replaceFragment(new MedFragment());
        binding.bottomNavigationBar.setSelectedItemId(R.id.med);
        }else{
        replaceFragment(new HomeFragment());
        }
        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_p:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.consult:
                    replaceFragment(new ConsultFragment());
                    break;
                case R.id.med:
                    replaceFragment(new MedFragment());
                    break;
            }
            return  true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityDoctorMainBinding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragments.AppointmentsFragment;
import com.example.myapplication.fragments.ConsultFragment;
import com.example.myapplication.fragments.DoctorChatFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.MedFragment;
import com.example.myapplication.fragments.ProfileFragment;

public class DoctorMainActivity extends AppCompatActivity {

    ActivityDoctorMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        binding = ActivityDoctorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AppointmentsFragment());
        binding.doctornavbar .setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.book_a:
                    replaceFragment(new AppointmentsFragment());
                    break;
                case R.id.profile_d:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.chat_n:
                    replaceFragment(new DoctorChatFragment());
                    break;
            }
            return  true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_d,fragment);
        fragmentTransaction.commit();
    }
}
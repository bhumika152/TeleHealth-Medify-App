package com.example.myapplication.dilogs;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

public class LoaderDilog {
    Activity activity;
    AlertDialog dilog;
    public LoaderDilog(Activity a){
        activity = a;
    }
    public void startDilog(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loader_dilog,null));
        builder.setCancelable(true);
        dilog = builder.create();
        dilog.show();
    }
    public  void endDilog(){
        dilog.dismiss();
    }
}

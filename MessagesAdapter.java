package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.example.myapplication.models.Message;

import java.util.List;

public class MessagesAdapter extends ArrayAdapter<Message> {
    Activity a;
    String id;

    public MessagesAdapter(Context context, List<Message> objects, Activity a,String id) {
        super(context, 0, objects);
        this.a =  a;
        this.id = id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message room = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble,parent,false);
        }
        TextView mtext = convertView.findViewById(R.id.message_text);
        if(room.getSenderId().equals(id)){
            CardView cardView = convertView.findViewById(R.id.message_card);
            LinearLayout linearLayout = convertView.findViewById(R.id.message_align);
            cardView.setCardBackgroundColor(Color.parseColor("#b4d6bd"));
            linearLayout.setGravity(Gravity.RIGHT);
        }
        mtext.setText(room.getText());
        return convertView;
    }
}


package com.example.myapplication.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.MedicineReminders;

import java.util.List;
public class ReminderListAdapter extends ArrayAdapter<MedicineReminders> {
    public ReminderListAdapter(Context context, List<MedicineReminders> reminders) {
        super(context, 0,reminders);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MedicineReminders reminder = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_view_home,parent,false);
        }
        TextView name = convertView.findViewById(R.id.medi_name);
        TextView time = convertView.findViewById(R.id.medi_time);
        name.setText(reminder.getName());
        time.setText(reminder.getReminders());
        return convertView;
    }
}

package edu.ewubd.studenthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class CustomCalEventAdapter extends ArrayAdapter<Calendar> {

    private final Context context;
    private final ArrayList<Calendar> values;


    public CustomCalEventAdapter(@NonNull Context context, @NonNull ArrayList<Calendar> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.view_calendar_row, parent, false);

        TextView calDate = rowView.findViewById(R.id.tvDate);
        TextView calDay = rowView.findViewById(R.id.tvDay);
        TextView calEvent = rowView.findViewById(R.id.tvEvent);

        calDate.setText(values.get(position).Date);
        calDay.setText(values.get(position).Day);
        calEvent.setText(values.get(position).Event);
        return rowView;
    }
}
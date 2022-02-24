package edu.ewubd.studenthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class CustomCourseAdapter extends ArrayAdapter<Course> {

    private final Context context;
    private final ArrayList<Course> values;


    public CustomCourseAdapter(@NonNull Context context, @NonNull ArrayList<Course> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.view_course_row, parent, false);

        TextView courseCode = rowView.findViewById(R.id.tvCourseCode);
        TextView room = rowView.findViewById(R.id.tvRoom);
        TextView timeSlot = rowView.findViewById(R.id.tvTimeSlot);


        courseCode.setText(values.get(position).courseCode);
        room.setText(values.get(position).room);
        timeSlot.setText(values.get(position).timeSlot);

        return rowView;
    }
}
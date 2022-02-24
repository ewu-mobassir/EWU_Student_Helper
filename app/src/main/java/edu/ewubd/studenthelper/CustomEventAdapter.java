package edu.ewubd.studenthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class CustomEventAdapter extends ArrayAdapter<Review> {

    private final Context context;
    private final ArrayList<Review> values;


    public CustomEventAdapter(@NonNull Context context, @NonNull ArrayList<Review> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.view_review_row, parent, false);

        TextView eventName = rowView.findViewById(R.id.tvCourseCode);
        TextView eventDateTime = rowView.findViewById(R.id.tvReview);


        eventName.setText(values.get(position).courseCode);
        eventDateTime.setText(values.get(position).review);

        return rowView;
    }
}
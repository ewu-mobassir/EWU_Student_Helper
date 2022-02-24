package edu.ewubd.studenthelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class view_review_activity extends Activity {

    private ListView lvReview;
    private ArrayList<Review> reviews;
    private CustomEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        lvReview = findViewById(R.id.lvReviews);

        initializeEventList();
        //initializeCustomEventList();


        findViewById(R.id.btnAddReview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view_review_activity.this, add_review_activity.class);
                startActivity(i);
            }
        });


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_review_activity.this.finish();
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        initializeCustomEventList();
    }


    private void initializeEventList(){
        String eventsList[] = {};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventsList);
        lvReview.setAdapter(arrayAdapter);

        lvReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                System.out.println(item);
            }
        });
    }



    private void initializeCustomEventList(){
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if (rows.getCount() == 0){
            return;
        }

        reviews = new ArrayList<>();

        int i = 0;
        while (rows.moveToNext()){
            String key = rows.getString(0);
            String eventData = rows.getString(1);

            String[] fieldValues = eventData.split(":-:-:");
            String courseCode = fieldValues[0];
            String review = fieldValues[1];


            Review e = new Review(key, courseCode, review);
            reviews.add(e);
        }
        db.close();

        adapter = new CustomEventAdapter(this, reviews);
        lvReview.setAdapter(adapter);

        lvReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // String item = (String) parent.getItemAtPosition(position);
                System.out.println(position);

                Intent i = new Intent(view_review_activity.this, add_review_activity.class);
                i.putExtra("EventKey", reviews.get(position).key);
                startActivity(i);
            }
        });

        lvReview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "Do you want to delete review of - "+reviews.get(position).courseCode+" ?";
                showDialog(message, "Delete event", position);
                return true;
            }
        });

    }

    private void showDialog(String message, String title, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message) .setTitle(title);

        //Setting message manually and performing action on button click
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Util.getInstance().deleteByKey(view_review_activity.this, reviews.get(position).key);
                        dialog.cancel();
                        adapter.notifyDataSetChanged();
                        initializeCustomEventList();
                        //lvEvents.notifyAll();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();
    }

}






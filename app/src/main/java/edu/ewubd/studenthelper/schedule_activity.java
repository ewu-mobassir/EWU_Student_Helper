package edu.ewubd.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class schedule_activity extends Activity {

    private ListView lvCourses, lvExams;
    private ArrayList<Course> courses;
    private ArrayList<Exam> exams;
    private CustomExamAdapter adapter;
    private CustomCourseAdapter c_adapter;
    String stdID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.schedule);
        Intent i = getIntent();
        stdID = i.getStringExtra("StudentID");

        httpRequest(new String[] {"u_user"}, new String[] {stdID});
        httpRequest2(new String[] {"u_user"}, new String[] {stdID});


        lvCourses = findViewById(R.id.lvCourses);
        lvExams = findViewById(R.id.lvExams);
//
//        initializeCourseList();
//        initializeExamList();


        findViewById(R.id.btnAddNewCourse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(schedule_activity.this, add_course_activity.class);
                i.putExtra("StudentID",stdID);
                startActivity(i);
            }
        });

        findViewById(R.id.btnAddNewExam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(schedule_activity.this, add_exam_activity.class);
                i.putExtra("StudentID",stdID);
                startActivity(i);
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule_activity.this.finish();
            }
        });
    }


    @Override
    public void onResume(){
        super.onStart();
        System.out.println(stdID);
        httpRequest(new String[] {"c_user"}, new String[] {stdID});
        httpRequest2(new String[] {"e_user"}, new String[] {stdID});
    }

//
//    private void initializeCourseList(){
//        String eventsList[] = {"Event 1", "Event 2", "Event 3", "Event 4"};
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventsList);
//        lvCourses.setAdapter(arrayAdapter);
//
//        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                String item = (String) parent.getItemAtPosition(position);
//                System.out.println(item);
//            }
//        });
//    }
//
//    private void initializeExamList(){
//        String eventsList[] = {"Event 1", "Event 2", "Event 3", "Event 4"};
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventsList);
//        lvExams.setAdapter(arrayAdapter);
//
//        lvExams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                String item = (String) parent.getItemAtPosition(position);
//                System.out.println(item);
//            }
//        });
//    }


//    private void initializeCustomCourseList(){
//        KeyValueDB db = new KeyValueDB(this);
//        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
//        if (rows.getCount() == 0){
//            return;
//        }
//
//        courses = new ArrayList<>();
//
//        int i = 0;
//        while (rows.moveToNext()){
//            String key = rows.getString(0);
//            String eventData = rows.getString(1);
//
//            String[] fieldValues = eventData.split(":-;-:");
//            String semester = fieldValues[0];
//            String courseCode = fieldValues[1];
//            String section = fieldValues[2];
//            String credit = fieldValues[3];
//            String room = fieldValues[4];
//            String timeSlot = fieldValues[5];
//            String faculty = fieldValues[6];
//
//            Course e = new Course(semester, courseCode, section, credit, room, timeSlot, faculty);
//            courses.add(e);
//        }
//        db.close();
//
//        c_adapter = new CustomCourseAdapter(this, courses);
//        lvCourses.setAdapter(c_adapter);
//
//        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                // String item = (String) parent.getItemAtPosition(position);
//                System.out.println(position);
//
//                Intent i = new Intent(schedule_activity.this, add_course_activity.class);
//                i.putExtra("EventKey", courses.get(position).key);
//                startActivity(i);
//            }
//        });

//        lvCourses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                String message = "Do you want to delete - "+courses.get(position).courseCode+" ?";
//                showDialog(message, "Delete event", position);
//                return true;
//            }
//        });

//    }

//
//    private void initializeCustomExamList(){
//        KeyValueDB db = new KeyValueDB(this);
//        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
//        if (rows.getCount() == 0){
//            return;
//        }
//
//        exams = new ArrayList<>();
//
//        int i = 0;
//        while (rows.moveToNext()){
//            String key = rows.getString(0);
//            String eventData = rows.getString(1);
//
//            String[] fieldValues = eventData.split(":-;-:");
//            String courseCode = fieldValues[0];
//            String section = fieldValues[1];
//            String examRoom = fieldValues[2];
//            String examTimeSlot = fieldValues[3];
//
//            Exam e = new Exam(key, courseCode, section, examRoom, examTimeSlot);
//            exams.add(e);
//        }
//        db.close();
//
//        adapter = new CustomExamAdapter(this, exams);
//        lvExams.setAdapter(adapter);
//
//        lvExams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                System.out.println(position);
//                Intent i = new Intent(schedule_activity.this, add_exam_activity.class);
//                i.putExtra("EventKey", exams.get(position).key);
//                startActivity(i);
//            }
//        });
//
//        lvExams.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                String message = "Do you want to delete exam of - "+exams.get(position).courseCode+" ?";
//                showDialog(message, "Delete event", position);
//                return true;
//            }
//        });
//
//    }

    private void showDialog(String message, String title, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message) .setTitle(title);
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private Course getCourseFromValues(String eventData){
        String[] fieldValues = eventData.split(":-;-:");
        String semester = fieldValues[0];
        String courseCode = fieldValues[1];
        String section = fieldValues[2];
        String credit = fieldValues[3];
        String room = "Room: "+fieldValues[4];
        String timeSlot = fieldValues[5];
        String faculty = fieldValues[6];
        Course e = new Course(semester, courseCode, section, credit, room, timeSlot, faculty);
        return e;
    }

    private Exam getExamFromValues(String eventData){
        String[] fieldValues = eventData.split(":-;-:");
        String courseCode = fieldValues[0];
        String section = fieldValues[1];
        String examRoom = "Room: "+fieldValues[2];
        String examTimeSlot = fieldValues[3];
        Exam e = new Exam(courseCode, section, examRoom, examTimeSlot);
        return e;
    }

    private void setCourseScheduleView(ArrayList<Course> courses){
        CustomCourseAdapter adapter = new CustomCourseAdapter(this, courses);
        lvCourses.setAdapter(adapter);
    }

    private void setExamScheduleView(ArrayList<Exam> exams){
        CustomExamAdapter adapter = new CustomExamAdapter(this, exams);
        lvExams.setAdapter(adapter);
    }


    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String keys[], final String values[]) {

        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(Void... param) {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for(int i=0; i<keys.length; i++) {
                        params.add(new BasicNameValuePair(keys[i], values[i]));
                    }
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/getCourse.php", "POST", params);

                    return jObj;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject jObj) {
                String msg = "Failed to send request";
                if(jObj != null) {
                    try {
                        courses = new ArrayList<>();
                        JSONArray eventsJson = jObj.getJSONArray("events");
                        for(int i=0; i<eventsJson.length(); i++){
                            JSONObject row = eventsJson.getJSONObject(i);
                            String user = row.getString("user");
                            String values = row.getString("value");
                            if(!user.isEmpty() && !values.isEmpty()) {
                                courses.add(getCourseFromValues(values));
                            }
                        }

                        setCourseScheduleView(courses);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void httpRequest2(final String keys[], final String values[]) {
        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(Void... param) {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for(int i=0; i<keys.length; i++) {
                        params.add(new BasicNameValuePair(keys[i], values[i]));
                    }
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/getExam.php", "POST", params);

                    return jObj;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject jObj) {
                String msg = "Failed to send request";
                if(jObj != null) {
                    try {
                        exams = new ArrayList<>();
                        JSONArray eventsJson = jObj.getJSONArray("events");
                        for(int i=0; i<eventsJson.length(); i++){
                            JSONObject row = eventsJson.getJSONObject(i);
                            String user = row.getString("user");
                            String values = row.getString("value");
                            if(!user.isEmpty() && !values.isEmpty()) {
                                exams.add(getExamFromValues(values));
                            }
                        }

                        setExamScheduleView(exams);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

}






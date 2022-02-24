package edu.ewubd.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class add_course_activity extends Activity {
    private EditText semesterTF, courseCodeTF, sectionTF, creditTF, roomTF, timeSlotTF, labRoomTF, labTimeSlotTF, facultyTF;
    private CheckBox hasLabCB;
    String stdID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent i = getIntent();
        stdID = i.getStringExtra("StudentID");


        semesterTF = findViewById(R.id.etSemester);
        setEditTextMaxLength(semesterTF,12);
        String s = "Summer-2018";
        semesterTF.setText(s);
        courseCodeTF = findViewById(R.id.etCourseCode);
        setEditTextMaxLength(courseCodeTF, 6);
        sectionTF = findViewById(R.id.etSection);
        setEditTextMaxLength(sectionTF, 2);
        creditTF = findViewById(R.id.etCredit);
        setEditTextMaxLength(creditTF, 4);
        roomTF = findViewById(R.id.etRoom);
        setEditTextMaxLength(roomTF, 8);
        timeSlotTF = findViewById(R.id.etTimeSlot);
        setEditTextMaxLength(timeSlotTF, 20);
//        labRoomTF = findViewById(R.id.etLabRoom);
//        setEditTextMaxLength(labRoomTF, 8);
//        labTimeSlotTF = findViewById(R.id.etLabTimeSlot);
//        setEditTextMaxLength(labTimeSlotTF, 20);
        facultyTF = findViewById(R.id.etFaculty);
        setEditTextMaxLength(facultyTF, 40);

//        hasLabCB = findViewById(R.id.cbHasLab);

//        initializeFormWithExistingData(existingKey);



        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_course_activity.this.finish();
            }
        });


        findViewById(R.id.btnAddCourse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semester = semesterTF.getText().toString();
                String courseCode = courseCodeTF.getText().toString();
                String section = sectionTF.getText().toString();
                String credit = creditTF.getText().toString();
                String room = roomTF.getText().toString();
                String timeSlot = timeSlotTF.getText().toString();
//                String labRoom = labRoomTF.getText().toString();
//                String labTimeSlot = labTimeSlotTF.getText().toString();
                String faculty = facultyTF.getText().toString();


                System.out.println("Semester: "+semester);
                System.out.println("Course Code: "+courseCode);
                System.out.println("Section: "+section);
                System.out.println("Credit: "+credit);
                System.out.println("Room: "+room);
                System.out.println("Time Slot: "+timeSlot);
//                System.out.println("Lab Room: "+labRoom);
//                System.out.println("Lab Time Slot: "+labTimeSlot);
                System.out.println("Faculty: "+faculty);

                String errorMsg = "";
                if (semester.isEmpty() || semester.length() < 4){
                    errorMsg += "Semester name can't be less than four characters.";
                }
                if (courseCode == null || courseCode.length() != 6) {
                    errorMsg += "Course code must have six characters.";
                }
                if (section.isEmpty()) {
                    errorMsg += "Section can't be empty.";
                }
                if (credit.isEmpty()) {
                    errorMsg += "Credit can't be empty.";
                }
                if (timeSlot.isEmpty() || timeSlot.length() < 5) {
                    errorMsg += "Time Slot can't be less than five characters.";
                }
//                if (labRoom.isEmpty() || labRoom.length() < 2) {
//                    errorMsg += "Lab Room number can't be less than two characters.";
//                }
//                if (labTimeSlot.isEmpty() || labTimeSlot.length() < 5) {
//                    errorMsg += "Lab Time Slot can't be less than eight characters.";
//                }
                if (errorMsg.isEmpty()){
                    // save data in database
                    String value = semester+":-;-:"+courseCode+":-;-:"+section+
                            ":-;-:"+credit+":-;-:"+room+":-;-:"
                            +timeSlot+":-;-:"+faculty;

                    String key = stdID;


                    System.out.println("Key: "+key);
                    System.out.println("Value: "+value);

                    httpRequest(new String[] {"key", "value"}, new String[] {key, value});

//                    Util.getInstance().setKeyValue(add_course_activity.this, key, value);

//                    showDialog("Course information has been saved successfully.","Info", "Ok", false);

                } else{
                    showDialog(errorMsg, "Error!!!!", "Back", true);
                }
            }
        });
    }

   private void initializeFormWithExistingData(String eventkey) {
       String value = Util.getInstance().getValueByKey(this, eventkey);
       System.out.println("Value: " + value);

       if (value != null) {
           String[] fieldValues = value.split(":-;-:");

           String semester = fieldValues[0];
           String courseCode = fieldValues[1];
           String section = fieldValues[2];
           String credit = fieldValues[3];
           String room = fieldValues[4];
           String timeSlot = fieldValues[5];
//           String labRoom = fieldValues[6];
//           String labTimeSlot = fieldValues[7];
           String faculty = fieldValues[6];

           semesterTF.setText(semester);
           courseCodeTF.setText(courseCode);
           sectionTF.setText(section);
           creditTF.setText(credit);
           roomTF.setText(room);
           timeSlotTF.setText(timeSlot);
//           labRoomTF.setText(labRoom);
//           labTimeSlotTF.setText(labTimeSlot);
           facultyTF.setText(faculty);
       }
   }
    public void setEditTextMaxLength(EditText et, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }

    private void showDialog(String message, String title, String buttonLabel, Boolean closeDialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message) .setTitle(title);
        builder.setCancelable(false)
                .setNegativeButton(buttonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(closeDialog){
                            dialog.cancel();
                        }
                        else {
                            finish();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
                    System.out.println(param);
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/addCourse.php", "POST", params);
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
                        msg = jObj.getString("msg");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                showDialog(msg, "Info", "Ok", false);
            }
        }.execute();
    }
}
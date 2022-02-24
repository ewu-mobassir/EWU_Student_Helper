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
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class add_exam_activity extends Activity {
    private EditText  courseCodeTF, sectionTF, examRoomTF, examTimeSlotTF;

    private String existingKey = null;
    String stdID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        Intent i = getIntent();
        stdID = i.getStringExtra("StudentID");


        courseCodeTF = findViewById(R.id.etCourseCode);
        setEditTextMaxLength(courseCodeTF, 6);
        sectionTF = findViewById(R.id.etSection);
        setEditTextMaxLength(sectionTF, 2);
        examRoomTF= findViewById(R.id.etExamRoom);
        setEditTextMaxLength(examRoomTF, 8);
        examTimeSlotTF = findViewById(R.id.etExamTimeSlot);
        setEditTextMaxLength(examTimeSlotTF, 30);

//        initializeFormWithExistingData(existingKey);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_exam_activity.this.finish();
            }
        });


        findViewById(R.id.btnAddExam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = courseCodeTF.getText().toString();
                String section = sectionTF.getText().toString();
                String examRoom = examRoomTF.getText().toString();
                String examTimeSlot = examTimeSlotTF.getText().toString();

                System.out.println("Course Code: "+courseCode);
                System.out.println("Section: "+section);
                System.out.println("Exam Room: "+examRoom);
                System.out.println("Exam Time Slot: "+examTimeSlot);

                String errorMsg = "";
                if (courseCode == null || courseCode.length() != 6) {
                    errorMsg += "Course code must have six characters.";
                }
                if (section == null) {
                    errorMsg += "Section can't be empty.";
                }
                if (examRoom == null || examRoom.length() < 2) {
                    errorMsg += "Exam Room number can't be less than two characters.";
                }
                if (examTimeSlot == null || examTimeSlot.length() < 5) {
                    errorMsg += "Exam Time Slot can't be less than eight characters.";
                }
                if (errorMsg.isEmpty()){
                    // save data in database
                    String value = courseCode+":-;-:"+section+ ":-;-:"+examRoom+":-;-:"+examTimeSlot;

                    String key = stdID;
                    System.out.println("STUDENTID:" +key);


                    System.out.println("Key: "+key);
                    System.out.println("Value: "+value);

                    httpRequest(new String[] {"key", "value"}, new String[] {key, value});

//                    Util.getInstance().setKeyValue(add_exam_activity.this, key, value);

//                    showDialog("Exam information has been saved successfully.","Info", "Ok", false);

                } else{
                    showDialog(errorMsg, "Error!!!!", "Back", true);
                }
            }
        });
    }

//   private void initializeFormWithExistingData(String eventkey) {
//       String value = Util.getInstance().getValueByKey(this, eventkey);
//       System.out.println("Value: " + value);
//
//       if (value != null) {
//           String[] fieldValues = value.split(":-;-:");
//
//           String courseCode = fieldValues[0];
//           String section = fieldValues[1];
//           String examRoom = fieldValues[2];
//           String examTimeSlot = fieldValues[3];
//
//
//           courseCodeTF.setText(courseCode);
//           sectionTF.setText(section);
//           examRoomTF.setText(examRoom);
//           examTimeSlotTF.setText(examTimeSlot);
//       }
//   }


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
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/addExam.php", "POST", params);
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
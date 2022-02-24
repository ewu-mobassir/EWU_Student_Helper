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
import android.widget.RadioButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class add_semester_activity extends Activity {
    private EditText yearTF, semesterTF, course1TF, course2TF, course3TF, course4TF, credit1TF, credit2TF, credit3TF, credit4TF, grade1TF, grade2TF, grade3TF, grade4TF;

    private String existingKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_custom_semester);

        yearTF = findViewById(R.id.etYear);
        setEditTextMaxLength(yearTF,4);
        semesterTF = findViewById(R.id.etSemester);
        setEditTextMaxLength(semesterTF, 10);
        course1TF = findViewById(R.id.etCourse1);
        setEditTextMaxLength(course1TF, 6);
        course2TF = findViewById(R.id.etCourse2);
        setEditTextMaxLength(course2TF, 6);
        course3TF = findViewById(R.id.etCourse3);
        setEditTextMaxLength(course3TF, 6);
        course4TF = findViewById(R.id.etCourse4);
        setEditTextMaxLength(course4TF, 6);
        credit1TF = findViewById(R.id.etCredit1);
        setEditTextMaxLength(credit1TF, 3);
        credit2TF = findViewById(R.id.etCredit2);
        setEditTextMaxLength(credit2TF, 3);
        credit3TF = findViewById(R.id.etCredit3);
        setEditTextMaxLength(credit3TF, 3);
        credit4TF = findViewById(R.id.etCredit4);
        setEditTextMaxLength(credit4TF, 3);
        grade1TF = findViewById(R.id.etGrade1);
        setEditTextMaxLength(grade1TF, 2);
        grade2TF = findViewById(R.id.etGrade2);
        setEditTextMaxLength(grade2TF, 2);
        grade3TF = findViewById(R.id.etGrade3);
        setEditTextMaxLength(grade3TF, 2);
        grade4TF = findViewById(R.id.etGrade4);
        setEditTextMaxLength(grade4TF, 2);


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_semester_activity.this.finish();
            }
        });


        findViewById(R.id.btnAddSemester).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = yearTF.getText().toString();
                String semester = semesterTF.getText().toString();
                String course1 = course1TF.getText().toString();
                String course2 = course2TF.getText().toString();
                String course3 = course3TF.getText().toString();
                String course4 = course4TF.getText().toString();
                String credit1 = credit1TF.getText().toString();
                String credit2 = credit2TF.getText().toString();
                String credit3 = credit3TF.getText().toString();
                String credit4 = credit4TF.getText().toString();
                String grade1 = grade1TF.getText().toString();
                String grade2 = grade2TF.getText().toString();
                String grade3 = grade3TF.getText().toString();
                String grade4 = grade4TF.getText().toString();


                System.out.println("Year: "+year);
                System.out.println("Semester: "+semester);
                System.out.println("Course1: "+course1);
                System.out.println("Credit1: "+credit1);
                System.out.println("Grade1: "+grade1);
                System.out.println("Course2: "+course2);
                System.out.println("Credit2: "+credit2);
                System.out.println("Grade2: "+grade2);
                System.out.println("Course3: "+course3);
                System.out.println("Credit3: "+credit3);
                System.out.println("Grade3: "+grade3);
                System.out.println("Course4: "+course4);
                System.out.println("Credit4: "+credit4);
                System.out.println("Grade4: "+grade4);

                String errorMsg = "";
                if (year.isEmpty() || year.length() != 4){
                    errorMsg += "Year must have four digits.";
                }
                if (semester.isEmpty()) {
                    errorMsg += "Please Enter a Proper Semester Name";
                }
                if (course1.isEmpty()){
                    errorMsg += "Semester must have at least 1 course";
                }



                if (errorMsg.isEmpty()){
                    // save data in database
                    String value = year+":-;"+semester+":-;"
                            +course1+ ":-;"+credit1+":-;"+grade1+":-;"
                            +course2+":-;"+credit2+":-;"+grade2+":-;"
                            +course3+":-;"+credit3+":-;"+grade3+":-;"
                            +course4+":-;"+credit4+":-;"+grade4;

                    String key = null;
                    if (existingKey != null){
                        key = existingKey;
                    }
                    else{
                        key = semester + "_" + year;
                    }

                    System.out.println("Key: "+key);
                    System.out.println("Value: "+value);

//                    Util.getInstance().setKeyValue(add_semester_activity.this, key, value);
                    httpRequest(new String[] {"key", "value"},new String[] {key, value});
//                    showDialog("Custom semester has been added successfully.","Info", "Ok", false);

                } else{
                    //((TextView)findViewById(R.id.tvErrormsg)).setText(errorMsg);
                    showDialog(errorMsg, "Error in Semester Data", "Back", true);
                }
            }
        });
    }



    public void setEditTextMaxLength( EditText et, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }

    private void showDialog(String message, String title, String buttonLabel, Boolean closeDialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message) .setTitle(title);

        //Setting message manually and performing action on button click
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
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/addSemester.php", "POST", params);
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
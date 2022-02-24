package edu.ewubd.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends Activity {
    private TextView nameTF, emailTF, phoneTF, IdTF, cgpaTF, creditTF, deptTF, dobTF, bloodTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        nameTF = findViewById(R.id.etName);
//        setEditTextMaxLength(nameTF,30);
        emailTF = findViewById(R.id.etEmail);
//        setEditTextMaxLength(emailTF, 30);
        phoneTF = findViewById(R.id.etPhone);
//        setEditTextMaxLength(phoneTF, 11);
        IdTF = findViewById(R.id.etId);
//        setEditTextMaxLength(IdTF, 13);
        cgpaTF = findViewById(R.id.etCgpa);
//        setEditTextMaxLength(cgpaTF, 4);
        creditTF = findViewById(R.id.etCredit);
//        setEditTextMaxLength(creditTF, 3);
        deptTF = findViewById(R.id.etDept);
//        setEditTextMaxLength(deptTF,30);
        dobTF = findViewById(R.id.etDob);
//        setEditTextMaxLength(dobTF,12);
        bloodTF = findViewById(R.id.etBlood);
//        setEditTextMaxLength(bloodTF,2);

        Intent i = getIntent();
        String stdID = i.getStringExtra("StudemtID");
        httpRequest(new String[] {"e_id"}, new String[] {stdID});


        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, Home_activity.class);
                i.putExtra("StudentID",stdID);
                startActivity(i);
                finish();
            }
        });
    }


    public void setEditTextMaxLength( EditText et, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }

    private void showAlertDialog(String message, String title, String buttonLabel, boolean closeDialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message) .setTitle(title);
        //Setting message manually and performing action on button click
        builder.setCancelable(false)
                .setNegativeButton(buttonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(closeDialog) {
                            dialog.cancel();
                        }
                        else {
                            finish();
                        }
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
//        alert.setTitle("Error Dialog");
        alert.setTitle(title);
        alert.show();
    }

    private void setProfileInfo(String values){
        String[] fieldValues = values.split(":-;");
        nameTF.setText(fieldValues[0]);
        IdTF.setText(fieldValues[1]);
        cgpaTF.setText(fieldValues[2]);
        creditTF.setText(fieldValues[3]);
        deptTF.setText(fieldValues[4]);
        phoneTF.setText(fieldValues[5]);
        emailTF.setText(fieldValues[6]);
        dobTF.setText(fieldValues[7]);
        bloodTF.setText(fieldValues[8]);
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
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/getStudentProfileInfo.php", "POST", params);
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
                        JSONArray profileJson = jObj.getJSONArray("std_info");
                        JSONObject profile = profileJson.getJSONObject(0);
                        String key = profile.getString("id");
                        String value = profile.getString("value");
                        if(!key.isEmpty() && !value.isEmpty()) {
                            setProfileInfo(value);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

}
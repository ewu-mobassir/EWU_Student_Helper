package edu.ewubd.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home_activity extends Activity {

    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor perfsEditor;
    String stdId;
    TextView infoArea;


    @Override
    public void onStart() {
        super.onStart();
        Intent i = getIntent();
        stdId = i.getStringExtra("StudentID");

        sharedPreferences = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String remember_val = sharedPreferences.getString("remember", "");
        if(remember_val.equals("login") && stdId==null){
            stdId = sharedPreferences.getString("user_id", null);
        }

        if(stdId != null){
            setContentView(R.layout.home);
            infoArea = findViewById(R.id.infoAreaTF);
            httpRequest(new String[] {"e_id"}, new String[] {stdId});

            findViewById(R.id.btnReviews).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Home_activity.this, view_review_activity.class);
                    startActivity(i);
                }
            });


            findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(remember_val.equals("login")){
                        perfsEditor = sharedPreferences.edit();
                        perfsEditor.putString("remember", null);
                        perfsEditor.putString("user_id", null);
                        perfsEditor.apply();
                    }
                    finish();
                    Intent i = new Intent(Home_activity.this, Home_activity.class);
                    startActivity(i);
                }
            });

            findViewById(R.id.btnProfile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(stdId);
                    Intent i = new Intent(Home_activity.this, ProfileActivity.class);
                    i.putExtra("StudemtID", stdId);
                    startActivity(i);
                    finish();
                }
            });

            findViewById(R.id.btnSchedule).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Home_activity.this, schedule_activity.class);
                    i.putExtra("StudentID",stdId);
                    startActivity(i);
                }
            });
        }



//      Not signed in View buttons
        else
        {
            setContentView(R.layout.home1);
            findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Home_activity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            findViewById(R.id.btnAboutUs).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Home_activity.this, about_us.class);
                    startActivity(i);
//                    finish();
                }
            });
        }

        // BUTTON FOR WITH AND WITHOUT LOGIN
        findViewById(R.id.btnFlowchart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_activity.this, FlowchartActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnAcadC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_activity.this, CalendarActivity.class);
                startActivity(i);
            }
        });


        findViewById(R.id.btnCgpa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_activity.this, CGPACalcActivity.class);
                startActivity(i);
            }
        });


        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    private void setProfileInfo(String values){
        String[] fieldValues = values.split(":-;");
        String s = "Welcome "+fieldValues[0]+"\n"+fieldValues[1];
//        +"\nCGPA: "+fieldValues[2];
        infoArea.setText(s);
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
package edu.ewubd.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CGPACalcActivity extends Activity {
    TextView cgpaTV;
    ArrayList<String> gpa_values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa_calc);


        httpRequest(new String[] {""}, new String[] {""});


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnAddSemester).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CGPACalcActivity.this, add_semester_activity.class);
                startActivity(i);
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        httpRequest(new String[] {""}, new String[] {""});
//
//    }

    public double convertGrade(String grade){
        if(grade.equals("A")||grade.equals("A+")){
            return 4.0;
        } else if(grade.equals("A-")){
            return 3.7;
        }else if(grade.equals("B+")){
            return 3.3;
        }else if(grade.equals("B")){
            return 3.0;
        }else if(grade.equals("B-")){
            return 2.7;
        }else if(grade.equals("C+")){
            return 2.3;
        }else if(grade.equals("C")){
            return 2.0;
        }else if(grade.equals("C-")){
            return 1.7;
        } else if(grade.equals("D+")) {
            return 1.3;
        } else if(grade.equals("D")) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    public void calcCGPA(ArrayList<String> values){

        String res = "";
        String[] fieldValues;
        double cgpa = 0;
        double totGpacr = 0;
        double totCredComplete = 0;
        double semGpacr, semCredComplete, semGpa;
        String semGpaStr, cgpaStr;
        for (int i = 0; i < values.size() ; i++) {
            semGpacr = 0;
            semCredComplete = 0;
            if (values.get(i) != null) {
                fieldValues = values.get(i).split(":-;");
                System.out.println(fieldValues.length);
                res+="                              "+fieldValues[1]+" "+fieldValues[0]+"\n";
                res+="\n"+"Course Code"+"            "+"Credits"+"           "+"Grade";
                for (int j = 2; j < fieldValues.length; j=j+3) {
                    semGpacr += Double.parseDouble((fieldValues[j+1])) * convertGrade(fieldValues[j+2]);
                    semCredComplete += Double.parseDouble(fieldValues[j+1]);
                    res+="\n"+fieldValues[j]+"                      "+fieldValues[j+1]+"                   "+fieldValues[j+2];
                }
                totGpacr += semGpacr;
                totCredComplete += semCredComplete;

                semGpa = semGpacr / semCredComplete;
                cgpa = totGpacr / totCredComplete;
                System.out.println(semCredComplete);
                System.out.println(totCredComplete);
                semGpaStr = String.format("%2.2f", semGpa);
                cgpaStr = String.format("%2.2f", cgpa);

                res +="\n\n" +"Credits Completed: "+ totCredComplete+
                        "\nSemester GPA = "+semGpaStr + "           CGPA = "+cgpaStr+"\n\n";

            }
        }
        cgpaTV = (TextView)findViewById(R.id.cgpaTextView);
        cgpaTV.setText(res);
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
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/getSemester.php", "POST", params);

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
                        gpa_values = new ArrayList<>();
                        JSONArray eventsJson = jObj.getJSONArray("events");
                        for(int i=0; i<eventsJson.length(); i++){
                            JSONObject row = eventsJson.getJSONObject(i);
                            String name = row.getString("key");
                            String val = row.getString("value");
                            if(!name.isEmpty() && !val.isEmpty()) {
                                gpa_values.add(val);
                                System.out.println(val);
                            }
                        }
                        calcCGPA(gpa_values);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
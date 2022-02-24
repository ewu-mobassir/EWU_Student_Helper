package edu.ewubd.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CalendarActivity extends Activity {

    private ListView lvAcadCal;
    private ArrayList<Calendar> calEvents;
    private CustomCalEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_cal);
        lvAcadCal = findViewById(R.id.lvCalEvents);
        httpRequest(new String[] {"date"}, new String[] {""});

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setCalenderView(ArrayList<Calendar> calEvents){
        CustomCalEventAdapter adapter = new CustomCalEventAdapter(this, calEvents);
        lvAcadCal.setAdapter(adapter);
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
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/getCalender.php", "POST", params);

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
                        calEvents = new ArrayList<>();
                        JSONArray eventsJson = jObj.getJSONArray("events");
                        for(int i=0; i<eventsJson.length(); i++){
                            JSONObject row = eventsJson.getJSONObject(i);
                            String date = row.getString("date");
                            String day = row.getString("day");
                            String event = row.getString("event");
                            if(!date.isEmpty() && !day.isEmpty()) {
                                calEvents.add(new Calendar(date, day, event));
                            }
                        }
//                        initializeCustomEventList();
//                        adapter.notifyDataSetChanged();
                        setCalenderView(calEvents);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
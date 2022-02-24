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
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private EditText useridTF, passwordTF;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor perfsEditor;
    private CheckBox rememberUserCheck, rememberLoginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        useridTF = findViewById(R.id.etUserId);
        setEditTextMaxLength(useridTF, 13);

        passwordTF = findViewById(R.id.etPassword);
        setEditTextMaxLength(passwordTF, 20);

        rememberUserCheck = findViewById(R.id.cbRemUserId);
        rememberLoginCheck = findViewById(R.id.cbRemLogin);


        sharedPreferences = getSharedPreferences("LoginSharedPref", MODE_PRIVATE);
        String remember_val = sharedPreferences.getString("remember", null);
        if (remember_val == null);
        else if(remember_val.equals("user")){
            rememberUserCheck.setChecked(true);
            useridTF.setText(sharedPreferences.getString("user_id", null));
        }
        else if(remember_val.equals("login")){
//            Intent i = new Intent(LoginActivity.this, UpcomingEventsActivity.class);
//            startActivity(i);
//            finish();
        }

        rememberUserCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rememberLoginCheck.setChecked(false);
            }
        });

        rememberLoginCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rememberUserCheck.setChecked(false);
            }
        });


        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userid = useridTF.getText().toString();
                String password = passwordTF.getText().toString();

                String errMsg = "";
                if (userid == null || userid.length()<13){
                    errMsg+= "User ID Must have exactly 13 characters\n";
                }
                if (password == null || password.length() <4 ){
                    errMsg+= "Password must have at least 4 characters\n";
                }

                if (errMsg.isEmpty()){
//                    String user = sharedPreferences.getString("user_id", null);
//                    String pass = sharedPreferences.getString("password", null);
                    httpRequest(new String[] {"u_id"}, new String[] {userid});
                }
                else {
                    showAlertDialog(errMsg, "Login Error", "Back", true);
                }
            }
        });

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Home_activity.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void setEditTextMaxLength(EditText et, int length){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }

    private void completeLogin( String user, String pass){
        String userid = useridTF.getText().toString();
        String password = passwordTF.getText().toString();

        if (user.equals(userid) && pass.equals(password)){

            perfsEditor = sharedPreferences.edit();
            perfsEditor.putString("user_id", userid);
            if (rememberUserCheck.isChecked()) {
                perfsEditor.putString("remember","user");
            } else if(rememberLoginCheck.isChecked()){
                perfsEditor.putString("remember","login");
            }
            else{
                perfsEditor.putString("remember", null);
                perfsEditor.putString("user_id", null);
            }
            perfsEditor.apply();
            Intent i = new Intent(LoginActivity.this, Home_activity.class);
            i.putExtra("StudentID",userid);
            startActivity(i);
            finish();
        }
        else{
            showAlertDialog("Wrong User ID or Password", "Login Error", "Back", true);
        }
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
                    JSONObject jObj = JSONParser.getInstance().makeHttpRequest("http://10.0.2.2:8080/stdHelper/getStudentLogin.php", "POST", params);
                    System.out.println(jObj);
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
                        JSONArray profileJson = jObj.getJSONArray("login_info");
                        JSONObject profile = profileJson.getJSONObject(0);
                        String user = profile.getString("id");
                        String pass = profile.getString("pass");
                        if(!user.isEmpty() && !pass.isEmpty()) {
                            completeLogin(user, pass);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
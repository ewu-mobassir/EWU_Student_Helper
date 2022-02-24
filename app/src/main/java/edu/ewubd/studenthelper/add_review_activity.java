package edu.ewubd.studenthelper;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;




public class add_review_activity extends Activity {
    private EditText courseCodeTF, reviewTF;

    private String existingKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        courseCodeTF = findViewById(R.id.editCourseCode);
        setEditTextMaxLength(courseCodeTF,6);
        reviewTF = findViewById(R.id.editAddCourseReview);
        setEditTextMaxLength(reviewTF, 500);




        Intent i = getIntent();
        existingKey = i.getStringExtra("EventKey");

        initializeFormWithExistingData(existingKey);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_review_activity.this.finish();
            }
        });


        findViewById(R.id.btnSubmitReview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = courseCodeTF.getText().toString();
                String review = reviewTF.getText().toString();


                System.out.println("Course Code: "+courseCode);
                System.out.println("Place: "+review);

                String errorMsg = "";
                if (courseCode == null || courseCode.length() != 6){
                    errorMsg += "Course code must have 6 characters.";
                }
                if (review == null || review.length() < 10) {
                    errorMsg += "Review can't be less than ten characters.";
                }
                if (errorMsg.isEmpty()){
                    // save data in database
                    String value = courseCode+":-:-:"+review;

                    String key = "";
                    if (existingKey != null){
                        key = existingKey;
                    }
                    else{
                        key = courseCode + "_" + System.currentTimeMillis();
                    }

                    System.out.println("Key: "+key);
                    System.out.println("Value: "+value);

                    Util.getInstance().setKeyValue(add_review_activity.this, key, value);

                    showDialog("Review has been submit successfully.","Review", "Ok", false);

                } else{
                    //((TextView)findViewById(R.id.tvErrormsg)).setText(errorMsg);
                    showDialog(errorMsg, "Error in Review", "Back", true);
                }
            }
        });
    }

   private void initializeFormWithExistingData(String eventkey) {
       String value = Util.getInstance().getValueByKey(this, eventkey);
       System.out.println("Value: " + value);

       if (value != null) {
           String[] fieldValues = value.split(":-:-:");

           String courseCode = fieldValues[0];
           String review = fieldValues[1];

           courseCodeTF.setText(courseCode);
           reviewTF.setText(review);

       }
   }
    public void setEditTextMaxLength(EditText et, int length) {
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

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();


    }


}
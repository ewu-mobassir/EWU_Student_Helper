package edu.ewubd.studenthelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class about_us extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_us.this.finish();
            }
        });
    }
}
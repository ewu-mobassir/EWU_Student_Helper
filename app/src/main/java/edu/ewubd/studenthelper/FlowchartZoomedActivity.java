package edu.ewubd.studenthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class FlowchartZoomedActivity extends Activity {

    int sem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        sem = i.getIntExtra("semester", 1);

        switch (sem){
            case 1:
                setContentView(R.layout.act_layour_sem1);
                break;
            case 2:
                setContentView(R.layout.act_layour_sem2);
                break;
            case 3:
                setContentView(R.layout.act_layour_sem3);
                break;
            case 4:
                setContentView(R.layout.act_layour_sem4);
                break;
            case 5:
                setContentView(R.layout.act_layour_sem5);
                break;
            case 6:
                setContentView(R.layout.act_layour_sem6);
                break;
            case 7:
                setContentView(R.layout.act_layour_sem7);
                break;
            case 8:
                setContentView(R.layout.act_layour_sem8);
                break;
            case 9:
                setContentView(R.layout.act_layour_sem9);
                break;
            case 10:
                setContentView(R.layout.act_layour_sem10);
                break;
            case 11:
                setContentView(R.layout.act_layour_sem11);
                break;
            case 12:
                setContentView(R.layout.act_layour_sem12);
                break;
        }

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sem<12){
                Intent i = new Intent(FlowchartZoomedActivity.this, FlowchartZoomedActivity.class);
                i.putExtra("semester", sem+1);
                startActivity(i);
                finish();
                } else {
                    finish();
                }
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
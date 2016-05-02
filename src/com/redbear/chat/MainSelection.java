package com.redbear.chat;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainSelection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selection);

        Button trainingButton = (Button)findViewById(R.id.training_button);
        Button practiceButton = (Button)findViewById(R.id.practice_button);
        Button statisticsButton = (Button)findViewById(R.id.statistics_button);

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(),
                        InitialTraining.class);
                startActivity(newIntent);
            }
        });
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(),
                        Chat.class);
                newIntent.putExtra(Device.EXTRA_DEVICE_ADDRESS,getIntent().getStringExtra("ADDRESS"));
                newIntent.putExtra(Device.EXTRA_DEVICE_NAME, getIntent().getStringExtra("NAME"));
                startActivity(newIntent);
            }
        });
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

}

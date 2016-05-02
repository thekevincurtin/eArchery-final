package com.redbear.chat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Practice extends Activity {
    private final static String TAG = Practice.class.getSimpleName();
    private ListView listView;
    private boolean isRecording = false;
    private ArrayList<String> accelData;
    long startTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        accelData = new ArrayList<String>();

        final Button shotButton = (Button)findViewById(R.id.shot_button);
        shotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRecording){
                    startTime = System.currentTimeMillis();
                    //begin recording
                    isRecording = true;
                    shotButton.setText("Stop Shot");
                    //append data to array
                }
                else if(isRecording){
                    //stop recording & analyze data
                    isRecording = false;
                    shotButton.setText("Start Shot");
                }
            }
        });

        listView = (ListView) findViewById(R.id.list_view);
        String[] values = new String[]{"Good","Test2","test420"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        //update
        values = new String[]{"Good Release","Good Release","Dead Release","Good Release","Dead Release","Dead Release","Good Release","Dead Release","Pluck Release","Pluck Release",
                "Pluck Release","Pluck Release","Pluck Release","Good Release"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
    }
    private void displayData(byte[] byteArray) {

        if (byteArray != null) {
            String data = new String(byteArray);
            Log.i(TAG, data);

            accelData.add(data);
            data += new String(byteArray);
            if (data.contains("*")) {
                data = data.substring(0, data.length());
                accelData.add((System.currentTimeMillis() - startTime) + ": " + data);
                //tv.append((System.currentTimeMillis() - startTime) + ": " + data + "\n");
                Log.i("debug", (System.currentTimeMillis() - startTime) + ": " + data);
                data = "";
            }
        }
    }
}

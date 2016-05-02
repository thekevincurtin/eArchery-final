package com.redbear.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Practice extends Activity {
    private ListView listView;
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        final Button shotButton = (Button)findViewById(R.id.shot_button);
        shotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRecording){
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

}

package com.redbear.chat;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ViewFlipper;

public class InitialTraining extends Activity {
    ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_training);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
    }
    public void flipView(View view){
        viewFlipper.showNext();
    }

}

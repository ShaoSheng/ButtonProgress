package com.lss.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ButtonProgress mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mBtn = findViewById(R.id.btn);
        mBtn.setText("按钮二");
        mBtn.setOnClickListener(onClickListener);
        mBtn.setOnButtonProgressChangeListener(onButtonProgressChangeListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "onClick()");
        }
    };

    private ButtonProgress.OnButtonProgressChangeListener onButtonProgressChangeListener = new ButtonProgress.OnButtonProgressChangeListener() {

        @Override
        public void onStopTrackingTouch(ButtonProgress buttonProgress) {
            Log.e(TAG, "onStopTrackingTouch()");
        }

        @Override
        public void onStartTrackingTouch(ButtonProgress buttonProgress) {
            Log.e(TAG, "onStartTrackingTouch()");
        }

        @Override
        public void onProgressChanged(ButtonProgress buttonProgress, int progress) {
            Log.e(TAG, "onProgressChanged()  progress = " + progress);
        }
    };
}

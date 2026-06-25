package com.oyekaleblessing.scientificcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedbackhelper.attachFeedback(this, findViewById(android.R.id.content));
        applyCurrentTheme();
        Log.d(TAG, "onCreate called");

        Button btnBasic = findViewById(R.id.btnBasic);
        Button btnScientific = findViewById(R.id.btnScientific);
        Button btnMatrix = findViewById(R.id.btnMatrix);
        Button btnStats = findViewById(R.id.btnStats);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnBasic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, basiccalculatoractivity.class));
				}
			});

        btnScientific.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, scientificactivity.class));
				}
			});

        btnMatrix.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, matrixactivity.class));
				}
			});

        btnStats.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, statsactivity.class));
				}
			});

        btnSettings.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, settingsactivity.class));
				}
			});
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCurrentTheme();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    private void applyCurrentTheme() {
        View root = findViewById(R.id.rootContainer);
        TextView tvTitle = findViewById(R.id.tvTitle);
        root.setBackgroundColor(themehelper.getRootBackgroundColor(this));
        tvTitle.setTextColor(themehelper.getPrimaryTextColor(this));
    }
}

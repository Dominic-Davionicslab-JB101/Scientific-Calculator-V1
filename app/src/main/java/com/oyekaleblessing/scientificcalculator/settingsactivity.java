package com.oyekaleblessing.scientificcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class settingsactivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        feedbackhelper.attachFeedback(this, findViewById(android.R.id.content));

        final ToggleButton switchSound = findViewById(R.id.switchSound);
        final ToggleButton switchHaptic = findViewById(R.id.switchHaptic);
        final ToggleButton switchTheme = findViewById(R.id.switchTheme);

        switchSound.setChecked(feedbackhelper.isSoundEnabled(this));
        switchHaptic.setChecked(feedbackhelper.isHapticEnabled(this));
        switchTheme.setChecked(themehelper.isDarkMode(this));

        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					feedbackhelper.setSoundEnabled(settingsactivity.this, isChecked);
				}
			});

        switchHaptic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					feedbackhelper.setHapticEnabled(settingsactivity.this, isChecked);
				}
			});

        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					themehelper.setDarkMode(settingsactivity.this, isChecked);
					applyCurrentTheme();
				}
			});

        applyCurrentTheme();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCurrentTheme();
    }

    private void applyCurrentTheme() {
        View root = findViewById(R.id.rootContainer);
        TextView tvTitle = findViewById(R.id.tvSettingsTitle);
        root.setBackgroundColor(themehelper.getRootBackgroundColor(this));
        tvTitle.setTextColor(themehelper.getPrimaryTextColor(this));
    }
}

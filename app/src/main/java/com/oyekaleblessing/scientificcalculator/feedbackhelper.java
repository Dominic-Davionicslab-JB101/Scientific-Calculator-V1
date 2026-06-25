package com.oyekaleblessing.scientificcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class feedbackhelper {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_SOUND = "sound_enabled";
    private static final String KEY_HAPTIC = "haptic_enabled";

    public static boolean isSoundEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_SOUND, true);
    }

    public static boolean isHapticEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_HAPTIC, true);
    }

    public static void setSoundEnabled(Context context, boolean enabled) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply();
    }

    public static void setHapticEnabled(Context context, boolean enabled) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_HAPTIC, enabled).apply();
    }

    public static void triggerFeedback(Context context) {
        if (isSoundEnabled(context)) {
            try {
                ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 60);
                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 40);
            } catch (Exception e) {
                // Some devices restrict tone generation - fail silently
            }
        }
        if (isHapticEnabled(context)) {
            try {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(35);
                }
            } catch (Exception e) {
                // Fail silently if vibration unavailable
            }
        }
    }

    // Recursively attaches feedback to every Button under the given view
    public static void attachFeedback(final Context context, View view) {
        if (view instanceof Button) {
            view.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							triggerFeedback(context);
						}
						return false; // let the normal click still happen
					}
				});
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                attachFeedback(context, group.getChildAt(i));
            }
        }
    }
}

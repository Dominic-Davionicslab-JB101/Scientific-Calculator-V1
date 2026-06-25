package com.oyekaleblessing.scientificcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class statsactivity extends Activity {

    private EditText etN, etR, etDataset;
    private TextView tvCombResult;
    private LinearLayout containerStatsResult;
	
	@Override
	protected void onResume() {
		super.onResume();
		applyCurrentTheme();
	}
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
		feedbackhelper.attachFeedback(this, findViewById(android.R.id.content));
		applyCurrentTheme();
		
        etN = findViewById(R.id.etN);
        etR = findViewById(R.id.etR);
        etDataset = findViewById(R.id.etDataset);
        tvCombResult = findViewById(R.id.tvCombResult);
        containerStatsResult = findViewById(R.id.containerStatsResult);

        findViewById(R.id.btnPermutation).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { calculatePermutation(); }
			});

        findViewById(R.id.btnCombination).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { calculateCombination(); }
			});

        findViewById(R.id.btnFactorial).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { calculateFactorial(); }
			});

        findViewById(R.id.btnCalculateStats).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { calculateStats(); }
			});
    }

    private void calculatePermutation() {
        Integer n = parseInt(etN.getText().toString());
        Integer r = parseInt(etR.getText().toString());

        if (n == null || r == null) {
            tvCombResult.setText("Enter valid n and r");
            return;
        }
        if (r > n || n < 0 || r < 0) {
            tvCombResult.setText("Need: n ≥ r ≥ 0");
            return;
        }
        double result = factorial(n) / factorial(n - r);
        tvCombResult.setText("P(" + n + ", " + r + ") = " + formatNumber(result));
    }

    private void calculateCombination() {
        Integer n = parseInt(etN.getText().toString());
        Integer r = parseInt(etR.getText().toString());

        if (n == null || r == null) {
            tvCombResult.setText("Enter valid n and r");
            return;
        }
        if (r > n || n < 0 || r < 0) {
            tvCombResult.setText("Need: n ≥ r ≥ 0");
            return;
        }
        double result = factorial(n) / (factorial(r) * factorial(n - r));
        tvCombResult.setText("C(" + n + ", " + r + ") = " + formatNumber(result));
    }
	
	private void applyCurrentTheme() {
		findViewById(R.id.rootContainer).setBackgroundColor(themehelper.getRootBackgroundColor(this));
		((TextView) findViewById(R.id.tvStatsTitle)).setTextColor(themehelper.getPrimaryTextColor(this));
	}

    private void calculateFactorial() {
        Integer n = parseInt(etN.getText().toString());

        if (n == null || n < 0) {
            tvCombResult.setText("Enter a valid n ≥ 0");
            return;
        }
        if (n > 20) {
            tvCombResult.setText("n too large (max 20)");
            return;
        }
        double result = factorial(n);
        tvCombResult.setText(n + "! = " + formatNumber(result));
    }

    private double factorial(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private Integer parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void calculateStats() {
        String input = etDataset.getText().toString().trim();
        if (input.isEmpty()) {
            showStatsError("Enter a dataset first");
            return;
        }

        String[] parts = input.split(",");
        List<Double> values = new ArrayList<>();

        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) continue;
            try {
                values.add(Double.parseDouble(trimmed));
            } catch (NumberFormatException e) {
                showStatsError("Invalid number: \"" + trimmed + "\"");
                return;
            }
        }

        if (values.isEmpty()) {
            showStatsError("No valid numbers found");
            return;
        }

        double mean = calculateMean(values);
        double median = calculateMedian(values);
        String mode = calculateMode(values);
        double variance = calculateVariance(values, mean);
        double stdDev = Math.sqrt(variance);
        double minVal = Collections.min(values);
        double maxVal = Collections.max(values);
        double range = maxVal - minVal;

        showStatsResult(values.size(), mean, median, mode, variance, stdDev, minVal, maxVal, range);
    }

    private double calculateMean(List<Double> values) {
        double sum = 0;
        for (double v : values) sum += v;
        return sum / values.size();
    }

    private double calculateMedian(List<Double> values) {
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int n = sorted.size();
        if (n % 2 == 0) {
            return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        } else {
            return sorted.get(n / 2);
        }
    }

    private String calculateMode(List<Double> values) {
        java.util.Map<Double, Integer> freq = new java.util.HashMap<>();
        for (double v : values) {
            Integer count = freq.get(v);
            freq.put(v, count == null ? 1 : count + 1);
        }

        int maxFreq = 0;
        for (Integer count : freq.values()) {
            if (count > maxFreq) maxFreq = count;
        }

        if (maxFreq <= 1) return "None (all unique)";

        List<Double> modes = new ArrayList<>();
        for (java.util.Map.Entry<Double, Integer> entry : freq.entrySet()) {
            if (entry.getValue() == maxFreq) modes.add(entry.getKey());
        }
        Collections.sort(modes);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modes.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(formatNumber(modes.get(i)));
        }
        return sb.toString();
    }

    private double calculateVariance(List<Double> values, double mean) {
        double sumSquaredDiff = 0;
        for (double v : values) {
            double diff = v - mean;
            sumSquaredDiff += diff * diff;
        }
        return sumSquaredDiff / values.size();
    }

    private void showStatsResult(int count, double mean, double median, String mode,
								 double variance, double stdDev, double min, double max, double range) {
        containerStatsResult.removeAllViews();

        addStatRow("Count (n)", String.valueOf(count));
        addStatRow("Mean", formatNumber(mean));
        addStatRow("Median", formatNumber(median));
        addStatRow("Mode", mode);
        addStatRow("Variance", formatNumber(variance));
        addStatRow("Std Deviation", formatNumber(stdDev));
        addStatRow("Min", formatNumber(min));
        addStatRow("Max", formatNumber(max));
        addStatRow("Range", formatNumber(range));
    }

    private void addStatRow(String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = dpToPx(6);
        row.setLayoutParams(rowParams);

        TextView tvLabel = new TextView(this);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        labelParams.weight = 1;
        tvLabel.setLayoutParams(labelParams);
        tvLabel.setText(label);
        tvLabel.setTextColor(0xFF888888);
        tvLabel.setTextSize(14);

        TextView tvValue = new TextView(this);
        LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        valueParams.weight = 1;
        tvValue.setLayoutParams(valueParams);
        tvValue.setText(value);
        tvValue.setTextColor(0xFF00E676);
        tvValue.setTextSize(14);
        tvValue.setGravity(Gravity.END);

        row.addView(tvLabel);
        row.addView(tvValue);
        containerStatsResult.addView(row);
    }

    private void showStatsError(String message) {
        containerStatsResult.removeAllViews();
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setText(message);
        tv.setTextColor(0xFFFF5252);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER);
        containerStatsResult.addView(tv);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private String formatNumber(double num) {
        if (num == Math.floor(num) && !Double.isInfinite(num)) {
            return String.valueOf((long) num);
        }
        return String.valueOf(Math.round(num * 1000.0) / 1000.0);
    }
}

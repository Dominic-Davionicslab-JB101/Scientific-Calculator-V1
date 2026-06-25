package com.oyekaleblessing.scientificcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class matrixactivity extends Activity {

    private int matrixSize = 2;
    private EditText[][] cellsA = new EditText[4][4];
    private EditText[][] cellsB = new EditText[4][4];
    private LinearLayout containerA, containerB, containerResult;
    private TextView tvSizeLabel;
	
	@Override
	protected void onResume() {
		super.onResume();
		applyCurrentTheme();
	}
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
		feedbackhelper.attachFeedback(this, findViewById(android.R.id.content));

        containerA = findViewById(R.id.containerMatrixA);
        containerB = findViewById(R.id.containerMatrixB);
        containerResult = findViewById(R.id.containerResult);
        tvSizeLabel = findViewById(R.id.tvSizeLabel);

        buildMatrixGrid(containerA, cellsA, matrixSize);
        buildMatrixGrid(containerB, cellsB, matrixSize);

        findViewById(R.id.btnSize2).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { changeSize(2); }
			});

        findViewById(R.id.btnSize3).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { changeSize(3); }
			});

        findViewById(R.id.btnSize4).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { changeSize(4); }
			});

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { doOperation("add"); }
			});

        findViewById(R.id.btnSubtract).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { doOperation("subtract"); }
			});

        findViewById(R.id.btnMultiply).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { doOperation("multiply"); }
			});

        findViewById(R.id.btnDetA).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { doOperation("detA"); }
			});

        findViewById(R.id.btnDetB).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { doOperation("detB"); }
			});

        findViewById(R.id.btnClearMatrix).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { clearAll(); }
			});
    }

    private void changeSize(int size) {
        matrixSize = size;
        tvSizeLabel.setText("Matrix size: " + size + "\u00D7" + size);
        buildMatrixGrid(containerA, cellsA, matrixSize);
        buildMatrixGrid(containerB, cellsB, matrixSize);
        containerResult.removeAllViews();
    }
	
	private void applyCurrentTheme() {
		findViewById(R.id.rootContainer).setBackgroundColor(themehelper.getRootBackgroundColor(this));
		((TextView) findViewById(R.id.tvMatrixTitle)).setTextColor(themehelper.getPrimaryTextColor(this));
		((TextView) findViewById(R.id.tvOperationsLabel)).setTextColor(themehelper.getPrimaryTextColor(this));
		((TextView) findViewById(R.id.tvResultLabel)).setTextColor(themehelper.getPrimaryTextColor(this));
		tvSizeLabel.setTextColor(themehelper.getSecondaryTextColor(this));
	}

    private void buildMatrixGrid(LinearLayout container, EditText[][] cells, int size) {
        container.removeAllViews();
        for (int r = 0; r < size; r++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);

            for (int c = 0; c < size; c++) {
                EditText et = new EditText(this);
                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(0, dpToPx(48));
                cellParams.weight = 1;
                cellParams.setMargins(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3));
                et.setLayoutParams(cellParams);
                et.setBackgroundColor(0xFF333333);
                et.setTextColor(0xFFFFFFFF);
                et.setHintTextColor(0xFF888888);
                et.setHint("0");
                et.setGravity(Gravity.CENTER);
                et.setTextSize(16);
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                row.addView(et);
                cells[r][c] = et;
            }
            container.addView(row);
        }
    }

    private void doOperation(String op) {
        double[][] a, b;
        try {
            a = getValues(cellsA, matrixSize);
            b = getValues(cellsB, matrixSize);
        } catch (NumberFormatException e) {
            showError("Invalid input - numbers only");
            return;
        }

        if (op.equals("add")) {
            showMatrixResult(addMatrices(a, b, matrixSize), matrixSize);
        } else if (op.equals("subtract")) {
            showMatrixResult(subtractMatrices(a, b, matrixSize), matrixSize);
        } else if (op.equals("multiply")) {
            showMatrixResult(multiplyMatrices(a, b, matrixSize), matrixSize);
        } else if (op.equals("detA")) {
            showScalarResult(determinant(a, matrixSize));
        } else if (op.equals("detB")) {
            showScalarResult(determinant(b, matrixSize));
        }
    }

    private double[][] getValues(EditText[][] cells, int n) {
        double[][] vals = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String text = cells[i][j].getText().toString().trim();
                vals[i][j] = text.isEmpty() ? 0 : Double.parseDouble(text);
            }
        }
        return vals;
    }

    private double[][] addMatrices(double[][] a, double[][] b, int n) {
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = a[i][j] + b[i][j];
        return result;
    }

    private double[][] subtractMatrices(double[][] a, double[][] b, int n) {
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = a[i][j] - b[i][j];
        return result;
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b, int n) {
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    private double determinant(double[][] m, int n) {
        if (n == 1) return m[0][0];
        if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0];

        double det = 0;
        for (int col = 0; col < n; col++) {
            double[][] minor = getMinor(m, n, 0, col);
            double sign = (col % 2 == 0) ? 1 : -1;
            det += sign * m[0][col] * determinant(minor, n - 1);
        }
        return det;
    }

    private double[][] getMinor(double[][] m, int n, int excludeRow, int excludeCol) {
        double[][] minor = new double[n - 1][n - 1];
        int mi = 0;
        for (int i = 0; i < n; i++) {
            if (i == excludeRow) continue;
            int mj = 0;
            for (int j = 0; j < n; j++) {
                if (j == excludeCol) continue;
                minor[mi][mj] = m[i][j];
                mj++;
            }
            mi++;
        }
        return minor;
    }

    private void showMatrixResult(double[][] result, int n) {
        containerResult.removeAllViews();
        for (int i = 0; i < n; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);

            for (int j = 0; j < n; j++) {
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(0, dpToPx(48));
                cellParams.weight = 1;
                cellParams.setMargins(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3));
                tv.setLayoutParams(cellParams);
                tv.setBackgroundColor(0xFF1C1C1E);
                tv.setTextColor(0xFF00E676);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(15);
                tv.setText(formatNumber(result[i][j]));
                row.addView(tv);
            }
            containerResult.addView(row);
        }
    }

    private void showScalarResult(double value) {
        containerResult.removeAllViews();
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(28);
        tv.setPadding(0, dpToPx(12), 0, dpToPx(12));
        tv.setText(formatNumber(value));
        containerResult.addView(tv);
    }

    private void showError(String message) {
        containerResult.removeAllViews();
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(0xFFFF5252);
        tv.setTextSize(16);
        tv.setText(message);
        containerResult.addView(tv);
    }

    private void clearAll() {
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                cellsA[i][j].setText("");
                cellsB[i][j].setText("");
            }
        }
        containerResult.removeAllViews();
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

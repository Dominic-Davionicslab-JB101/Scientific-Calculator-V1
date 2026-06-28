package com.AjibolaDavidChinedu.scientificcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class basiccalculatoractivity extends Activity {

    private TextView tvExpression, tvResult;
    private StringBuilder currentNumber = new StringBuilder();
    private double firstNumber = 0;
    private String operator = "";
	
	@Override
	protected void onResume() {
		super.onResume();
		applyCurrentTheme();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);
		feedbackhelper.attachFeedback(this, findViewById(android.R.id.content));
		applyCurrentTheme();

        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        int[] digitIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        for (int i = 0; i < digitIds.length; i++) {
            Button b = findViewById(digitIds[i]);
            b.setOnClickListener(new View.OnClickListener() {
				    @Override
                    public void onClick(View v) {
                        onDigit(((Button) v).getText().toString());
                    }
                });
        }

        findViewById(R.id.btnDecimal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onDecimal(); }
            });

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onClear(); }
            });

        findViewById(R.id.btnBackspace).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onBackspace(); }
            });

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onOperator("+"); }
            });

        findViewById(R.id.btnSubtract).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onOperator("−"); }
            });

        findViewById(R.id.btnMultiply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onOperator("×"); }
            });

        findViewById(R.id.btnDivide).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onOperator("÷"); }
            });

        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onEquals(); }
            });
    }

    private void onDigit(String digit) {
        currentNumber.append(digit);
        tvResult.setText(currentNumber.toString());
    }
	
	private void applyCurrentTheme() {
		findViewById(R.id.rootContainer).setBackgroundColor(themehelper.getRootBackgroundColor(this));
	}

    private void onDecimal() {
        if (!currentNumber.toString().contains(".")) {
            if (currentNumber.length() == 0) currentNumber.append("0");
            currentNumber.append(".");
            tvResult.setText(currentNumber.toString());
        }
    }

    private void onClear() {
        currentNumber.setLength(0);
        firstNumber = 0;
        operator = "";
        tvResult.setText("0");
        tvExpression.setText("");
    }

    private void onBackspace() {
        if (currentNumber.length() > 0) {
            currentNumber.deleteCharAt(currentNumber.length() - 1);
            tvResult.setText(currentNumber.length() == 0 ? "0" : currentNumber.toString());
        }
    }

    private void onOperator(String op) {
        if (currentNumber.length() == 0 && operator.isEmpty()) return;

        if (operator.isEmpty()) {
            firstNumber = Double.parseDouble(currentNumber.toString());
        } else if (currentNumber.length() > 0) {
            firstNumber = compute(firstNumber, Double.parseDouble(currentNumber.toString()), operator);
            if (Double.isNaN(firstNumber)) {
                tvResult.setText("undefined");
                tvExpression.setText("");
                operator = "";
                currentNumber.setLength(0);
                return;
            }
            tvResult.setText(formatNumber(firstNumber));
        }

        operator = op;
        tvExpression.setText(formatNumber(firstNumber) + " " + operator);
        currentNumber.setLength(0);
    }

    private void onEquals() {
        if (operator.isEmpty() || currentNumber.length() == 0) return;

        double secondNumber = Double.parseDouble(currentNumber.toString());
        double result = compute(firstNumber, secondNumber, operator);

        tvExpression.setText(formatNumber(firstNumber) + " " + operator + " " + formatNumber(secondNumber) + " =");

        if (Double.isNaN(result)) {
            tvResult.setText("undefined");
            firstNumber = 0;
            currentNumber.setLength(0);
            operator = "";
            return;
        }

        tvResult.setText(formatNumber(result));
        firstNumber = result;
        currentNumber.setLength(0);
        currentNumber.append(formatNumber(result));
        operator = "";
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "−": return a - b;
            case "×": return a * b;
            case "÷":
                if (b == 0) {
                    return Double.NaN;
                }
                return a / b;
            default: return b;
        }
    }

    private String formatNumber(double num) {
        if (num == Math.floor(num) && !Double.isInfinite(num)) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }
}

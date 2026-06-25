package com.oyekaleblessing.scientificcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class scientificactivity extends Activity {

    private TextView tvExpression, tvResult;
    private StringBuilder currentNumber = new StringBuilder();
    private double firstNumber = 0;
    private String operator = "";
    private boolean isDegreeMode = true;

    private int pendingFunctionId = 0;
    private String pendingFunctionName = "";
	
	@Override
	protected void onResume() {
		super.onResume();
		applyCurrentTheme();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scientific);
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

        findViewById(R.id.btnPower).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { onOperator("^"); }
			});

        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { onEquals(); }
			});

        int[] funcIds = {R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnSinh, R.id.btnCosh,
			R.id.btnTanh, R.id.btnLog, R.id.btnLn, R.id.btnSqrt, R.id.btnSquare, R.id.btnInverse};

        for (int i = 0; i < funcIds.length; i++) {
            final int id = funcIds[i];
            findViewById(id).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) { applyFunction(id); }
				});
        }

        findViewById(R.id.btnPi).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					currentNumber.setLength(0);
					currentNumber.append(formatNumber(Math.PI));
					tvResult.setText(currentNumber.toString());
				}
			});

        findViewById(R.id.btnE).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					currentNumber.setLength(0);
					currentNumber.append(formatNumber(Math.E));
					tvResult.setText(currentNumber.toString());
				}
			});

        final Button btnDegRad = findViewById(R.id.btnDegRad);
        btnDegRad.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					isDegreeMode = !isDegreeMode;
					btnDegRad.setText(isDegreeMode ? "DEG" : "RAD");
				}
			});
    }

    // Returns the display name for a function button, or null if id isn't a function button
    private String functionName(int id) {
        if (id == R.id.btnSin) return "sin";
        if (id == R.id.btnCos) return "cos";
        if (id == R.id.btnTan) return "tan";
        if (id == R.id.btnSinh) return "sinh";
        if (id == R.id.btnCosh) return "cosh";
        if (id == R.id.btnTanh) return "tanh";
        if (id == R.id.btnLog) return "log";
        if (id == R.id.btnLn) return "ln";
        if (id == R.id.btnSqrt) return "sqrt";
        if (id == R.id.btnSquare) return "sqr";
        if (id == R.id.btnInverse) return "inv";
        return null;
    }

    // Called when a function button is pressed
    private void applyFunction(int id) {
        String name = functionName(id);
        if (name == null) return;

        // If a different function was already waiting on a number, finish it first
        finalizePendingFunction();

        if (currentNumber.length() > 0) {
            // Number-first: apply immediately to what's been typed
            double val = Double.parseDouble(currentNumber.toString());
            computeFunctionAndDisplay(id, name, val);
        } else {
            // Function-first: remember it, wait for the number
            pendingFunctionId = id;
            pendingFunctionName = name;
            tvExpression.setText(name + "(...)");
            tvResult.setText("0");
        }
    }

    // If a function is waiting on a number and one has now been typed, compute it
    private void finalizePendingFunction() {
        if (pendingFunctionId != 0 && currentNumber.length() > 0) {
            double val = Double.parseDouble(currentNumber.toString());
            computeFunctionAndDisplay(pendingFunctionId, pendingFunctionName, val);
            pendingFunctionId = 0;
            pendingFunctionName = "";
        }
    }

    private void computeFunctionAndDisplay(int id, String name, double val) {
        double result;

        if (id == R.id.btnSin) {
            result = Math.sin(isDegreeMode ? Math.toRadians(val) : val);
        } else if (id == R.id.btnCos) {
            result = Math.cos(isDegreeMode ? Math.toRadians(val) : val);
        } else if (id == R.id.btnTan) {
            result = Math.tan(isDegreeMode ? Math.toRadians(val) : val);
        } else if (id == R.id.btnSinh) {
            result = Math.sinh(val);
        } else if (id == R.id.btnCosh) {
            result = Math.cosh(val);
        } else if (id == R.id.btnTanh) {
            result = Math.tanh(val);
        } else if (id == R.id.btnLog) {
            result = Math.log10(val);
        } else if (id == R.id.btnLn) {
            result = Math.log(val);
        } else if (id == R.id.btnSqrt) {
            result = Math.sqrt(val);
        } else if (id == R.id.btnSquare) {
            result = val * val;
        } else if (id == R.id.btnInverse) {
            result = 1 / val;
        } else {
            return;
        }

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            tvResult.setText("undefined");
            currentNumber.setLength(0);
            return;
        }

        tvExpression.setText(name + "(" + formatNumber(val) + ")");
        tvResult.setText(formatNumber(result));
        currentNumber.setLength(0);
        currentNumber.append(formatNumber(result));
    }

    private void onDigit(String digit) {
        currentNumber.append(digit);
        tvResult.setText(currentNumber.toString());
        if (pendingFunctionId != 0) {
            tvExpression.setText(pendingFunctionName + "(" + currentNumber.toString() + ")");
        }
    }
	
	private void applyCurrentTheme() {
		findViewById(R.id.rootContainer).setBackgroundColor(themehelper.getRootBackgroundColor(this));
	}

    private void onDecimal() {
        if (!currentNumber.toString().contains(".")) {
            if (currentNumber.length() == 0) currentNumber.append("0");
            currentNumber.append(".");
            tvResult.setText(currentNumber.toString());
            if (pendingFunctionId != 0) {
                tvExpression.setText(pendingFunctionName + "(" + currentNumber.toString() + ")");
            }
        }
    }

    private void onClear() {
        currentNumber.setLength(0);
        firstNumber = 0;
        operator = "";
        pendingFunctionId = 0;
        pendingFunctionName = "";
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
        finalizePendingFunction();

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
        finalizePendingFunction();

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
                if (b == 0) return Double.NaN;
                return a / b;
            case "^": return Math.pow(a, b);
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

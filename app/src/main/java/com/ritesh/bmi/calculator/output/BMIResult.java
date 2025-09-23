package com.ritesh.bmi.calculator.output;

import android.widget.TextView;

import java.util.Locale;

public class BMIResult {
    private final float bmi;
    private final TextView textViewResult;

    public BMIResult(float bmi, TextView textViewResult) {
        this.bmi = bmi;
        this.textViewResult = textViewResult;
    }

    /**
     * Displays the BMI result
     */
    public void displayResult() {
        EBMICategory bmiCategory = EBMICategory.fromBmi(bmi);
        String resultText = String.format(Locale.getDefault(), "Your BMI: %.2f kg/m²\nCategory: %s", bmi, bmiCategory.getDisplayName());
        textViewResult.setText(resultText);
        textViewResult.setTextColor(bmiCategory.getColor());
    }
}

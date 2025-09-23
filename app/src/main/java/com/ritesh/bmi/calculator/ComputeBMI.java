package com.ritesh.bmi.calculator;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ritesh.bmi.calculator.output.BMIResult;

public class ComputeBMI {
    private final EditText editTextWeight;
    private final EditText editTextHeight;
    private final TextView textViewResult;
    private boolean isHeightInCM;

    public ComputeBMI(EditText editTextWeight,
                      EditText editTextHeight,
                      TextView textViewResult
    ) {
        this.editTextWeight = editTextWeight;
        this.editTextHeight = editTextHeight;
        this.textViewResult = textViewResult;
    }

    public void setIsHeightInCM(boolean isHeightInCM) {
        this.isHeightInCM = isHeightInCM;
    }

    /**
     * Computes the BMI and displays the result.<br>
     *
     * Show error message if either weight or height is empty, or if height is zero.<br>
     * Calculate the BMI using the formula: weight (kg) / (height (m))^2.<br>
     *
     * @param mainActivity Activity to show error messages
     */
    public void compute(MainActivity mainActivity) {
        String weightStr = editTextWeight.getText().toString();
        String heightStr = editTextHeight.getText().toString();

        if (weightStr.isEmpty()) {
            Toast.makeText(mainActivity, R.string.please_enter_weight, Toast.LENGTH_SHORT).show();
            return;
        }

        if (heightStr.isEmpty()) {
            Toast.makeText(mainActivity, R.string.please_enter_height, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float weight = Float.parseFloat(weightStr);
            int height = Integer.parseInt(heightStr);

            if (height <= 0) {
                Toast.makeText(mainActivity, "Height must be positive value greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            float heightInMeters;
            if (isHeightInCM) {
                heightInMeters = (float) height / 100; // Convert cm to meters
            } else {
                heightInMeters = height * 0.0254f; // Convert inches to meters
            }

            // Calculate BMI: weight (kg) / (height (m))^2
            float bmi = weight / (heightInMeters * heightInMeters);
            BMIResult bmiResult = new BMIResult(bmi, textViewResult);
            bmiResult.displayResult();

        } catch (NumberFormatException e) {
            Toast.makeText(mainActivity, R.string.please_enter_valid_numbers, Toast.LENGTH_SHORT).show();
        }
    }
}

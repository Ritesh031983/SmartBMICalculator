package com.ritesh.bmi.calculator;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ritesh.bmi.calculator.output.BMIResult;
import com.ritesh.bmi.calculator.util.Conversion;

public class ComputeBMI {
    private final EditText editTextWeight;
    private final EditText editTextHeight;
    private final TextView textViewResult;
    private final View layoutBMIResult;
    private final ImageView imageViewIndicator;
    private boolean isHeightInCM;
    private boolean isWeightInKg;

    public ComputeBMI(EditText editTextWeight,
                      EditText editTextHeight,
                      TextView textViewResult,
                      View layoutBMIResult,
                      ImageView imageViewIndicator
    ) {
        this.editTextWeight = editTextWeight;
        this.editTextHeight = editTextHeight;
        this.textViewResult = textViewResult;
        this.layoutBMIResult = layoutBMIResult;
        this.imageViewIndicator = imageViewIndicator;
    }

    public void setIsHeightInCM(boolean isHeightInCM) {
        this.isHeightInCM = isHeightInCM;
    }

    public void setIsWeightInKg(boolean isWeightInKg) {
        this.isWeightInKg = isWeightInKg;
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
        var weightStr = editTextWeight.getText().toString();
        var heightStr = editTextHeight.getText().toString();

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
            float height = Float.parseFloat(heightStr);

            if (height <= 0) {
                Toast.makeText(mainActivity, "Height must be positive value greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            float weightInKg = isWeightInKg ? weight : Conversion.convertPoundsToKilograms.apply(weight);
            float heightInMeters = isHeightInCM ? Conversion.convertCmToMeters.apply(height) : Conversion.convertInchesToMeters.apply(height);

            // Calculate BMI: weight (kg) / (height (m))^2
            BMIResult bmiResult = new BMIResult(
                    Conversion.calculateBMI.apply(weightInKg, heightInMeters),
                    textViewResult,
                    layoutBMIResult,
                    imageViewIndicator
            );
            bmiResult.displayResult();

        } catch (NumberFormatException e) {
            Toast.makeText(mainActivity, R.string.please_enter_valid_numbers, Toast.LENGTH_SHORT).show();
        }
    }
}

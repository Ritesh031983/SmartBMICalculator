package com.ritesh.bmi.calculator.output;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public record BMIResult(float bmi, TextView textViewResult, View layoutBMIResult, ImageView imageViewIndicator) {

    /**
     * Displays the BMI result
     */
    public void displayResult() {
        layoutBMIResult.setVisibility(View.VISIBLE);
        
        // Calculate position for the indicator
        // Assuming the scale is roughly linear from BMI 15 to 40
        var minBmi = 15f;
        var maxBmi = 40f;
        var calcPercentage = (bmi - minBmi) / (maxBmi - minBmi);
        if (calcPercentage < 0) calcPercentage = 0;
        if (calcPercentage > 1) calcPercentage = 1;

        final float percentage = calcPercentage;

        layoutBMIResult.post(() -> {
            // Set pivot to bottom center for rotation
            imageViewIndicator.setPivotX(imageViewIndicator.getWidth() / 2f);
            imageViewIndicator.setPivotY(imageViewIndicator.getHeight());

            // Calculate rotation angle (-90 to 90 degrees)
            float rotation = (percentage * 180f) - 90f;
            
            // Clamp rotation to avoid going out of boundary at extreme values
            if (rotation < -85f) rotation = -85f;
            if (rotation > 85f) rotation = 85f;
            
            imageViewIndicator.setRotation(rotation);
        });

        EBMICategory bmiCategory = EBMICategory.fromBmi(bmi);
        String resultText = String.format(Locale.getDefault(), "Your BMI: %.2f kg/m²\nCategory: %s", bmi, bmiCategory.getDisplayName());
        textViewResult.setText(resultText);
        textViewResult.setTextColor(bmiCategory.getColor());
    }
}

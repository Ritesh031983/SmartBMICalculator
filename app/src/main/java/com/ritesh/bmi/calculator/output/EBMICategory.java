package com.ritesh.bmi.calculator.output;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Enum for BMI categories.
 * 0 < Underweight <= 18.5
 * 18.5 < Normal weight <= 24.9
 * 24.9 < Overweight <= 29.9
 * 29.9 < Obese Class 1 <= 34.9
 * 34.9 < Obese Class 2 <= 39.9
 * 39.9 < Obese Class 3 / Severe Obesity
 */
public enum EBMICategory {
    UNKNOWN("Unknown", -1f, -1f, Color.BLACK),
    UNDERWEIGHT("Underweight", 0f, 18.5f, Color.BLUE),
    NORMAL_WEIGHT("Normal weight", 18.5f, 24.9f, Color.GREEN),
    OVERWEIGHT("Overweight", 24.9f, 29.9f, Color.rgb(250, 197, 5)),
    OBESE_1("Obese Class 1", 29.9f, 34.9f, Color.rgb(255, 165, 0)),  //Orange
    OBESE_2("Obese Class 2", 34.9f, 39.9f, Color.RED),
    OBESE_3("Obese Class 3 / Severe Obesity", 39.9f, Float.MAX_VALUE, Color.rgb(128, 0, 128)); //Purple

    private final String displayName;
    private final float lowerValue;
    private final float upperValue;

    private final int color;

    EBMICategory(String displayName, float lowerValue, float upperValue, int color) {
        this.displayName = displayName;
        this.lowerValue = lowerValue;
        this.upperValue = upperValue;
        this.color = color;
    }

    String getDisplayName() {
        return displayName;
    }

    int getColor() {
        return color;
    }

    // Static method to get category from BMI value
    public static EBMICategory fromBmi(float bmi) {
        return Arrays.stream(EBMICategory.values())
                .filter(category -> bmi >= category.lowerValue && bmi < category.upperValue)
                .findFirst()
                .orElse(UNKNOWN);
    }
}

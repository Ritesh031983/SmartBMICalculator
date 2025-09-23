package com.ritesh.bmi.calculator.util;

/**
 * Utility class
 */
public class Conversion {
    private Conversion() {}

    public static float convertInchesToMeters(float inches) {
        return inches * 0.0254f;
    }

    public static float convertPoundsToKilograms(float pounds) {
        return pounds * 0.453592f;
    }

    public static float convertCmToMeters(int cm) {
        return (float) cm / 100;
    }

    /**
     * Calculate BMI
     * @param weight weight in kg
     * @param height height in meters
     * @return weight (kg) / (height (m))^2
     */
    public static float calculateBMI(float weight, float height) {
        return weight / (height * height);
    }
}

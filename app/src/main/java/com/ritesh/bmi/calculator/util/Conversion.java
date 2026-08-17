package com.ritesh.bmi.calculator.util;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Utility class using functional interfaces.
 */
public final class Conversion {
    private Conversion() {}

    public static final BiFunction<Double, Double, Double> convertFeetAndInchesToInches = (feet, inches) -> (feet * 12) + inches;

    public static final UnaryOperator<Float> convertInchesToMeters = inches -> inches * 0.0254f;

    public static final UnaryOperator<Float> convertPoundsToKilograms = pounds -> pounds * 0.453592f;

    public static final Function<Float, Float> convertCmToMeters = cm -> cm / 100f;

    /**
     * Calculate BMI
     * Formula: weight (kg) / (height (m))^2
     */
    public static final BinaryOperator<Float> calculateBMI = (weight, height) -> weight / (height * height);
}

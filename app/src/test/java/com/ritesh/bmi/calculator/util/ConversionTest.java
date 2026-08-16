package com.ritesh.bmi.calculator.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConversionTest {

    @Test
    public void testConvertFeetAndInchesToInches() {
        assertEquals(70, Conversion.convertFeetAndInchesToInches.apply(5.0, 10.0), 0.001);
        assertEquals(60, Conversion.convertFeetAndInchesToInches.apply(5.0, 0.0), 0.001);
        assertEquals(12, Conversion.convertFeetAndInchesToInches.apply(1.0, 0.0), 0.001);
    }

    @Test
    public void testConvertInchesToMeters() {
        assertEquals(1.778f, Conversion.convertInchesToMeters.apply(70.0f), 0.001f);
    }

    @Test
    public void testConvertPoundsToKilograms() {
        assertEquals(45.3592f, Conversion.convertPoundsToKilograms.apply(100.0f), 0.001f);
    }

    @Test
    public void testConvertCmToMeters() {
        assertEquals(1.7f, Conversion.convertCmToMeters.apply(170), 0.001f);
    }

    @Test
    public void testCalculateBMI() {
        // weight 70kg, height 1.75m -> 70 / (1.75 * 1.75) = 22.857
        assertEquals(22.857f, Conversion.calculateBMI.apply(70.0f, 1.75f), 0.001f);
    }
}

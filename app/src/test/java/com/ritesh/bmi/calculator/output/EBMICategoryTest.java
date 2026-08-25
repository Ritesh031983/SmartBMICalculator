package com.ritesh.bmi.calculator.output;

import org.junit.Test;
import static org.junit.Assert.*;

public class EBMICategoryTest {

    @Test
    public void testFromBmi() {
        assertEquals(EBMICategory.UNDERWEIGHT, EBMICategory.fromBmi(18.4f));
        assertEquals(EBMICategory.NORMAL_WEIGHT, EBMICategory.fromBmi(18.5f));
        assertEquals(EBMICategory.NORMAL_WEIGHT, EBMICategory.fromBmi(24.8f));
        assertEquals(EBMICategory.OVERWEIGHT, EBMICategory.fromBmi(24.9f));
        assertEquals(EBMICategory.OBESE_1, EBMICategory.fromBmi(30.0f));
        assertEquals(EBMICategory.OBESE_2, EBMICategory.fromBmi(35.0f));
        assertEquals(EBMICategory.OBESE_3, EBMICategory.fromBmi(40.0f));
        assertEquals(EBMICategory.UNKNOWN, EBMICategory.fromBmi(-1f));
    }
}

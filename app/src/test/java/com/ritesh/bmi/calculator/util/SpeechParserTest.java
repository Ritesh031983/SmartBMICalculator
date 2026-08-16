package com.ritesh.bmi.calculator.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class SpeechParserTest {

    @Test
    public void testParseWeight() {
        SpeechParser.ParsingResult result = SpeechParser.parse("I weigh 70 kg");
        assertEquals("70", result.weightStr);
        assertEquals("kg", result.weightUnit);

        result = SpeechParser.parse("weight is 150 pound");
        assertEquals("150", result.weightStr);
        assertEquals("pound", result.weightUnit);
    }

    @Test
    public void testParseHeightCm() {
        SpeechParser.ParsingResult result = SpeechParser.parse("my height is 170 cm");
        assertEquals("170", result.heightStr);
        assertEquals("cm", result.heightUnit);
    }

    @Test
    public void testParseHeightFeetAndInches() {
        // 5 ft 10 in -> 70.0 inches
        SpeechParser.ParsingResult result = SpeechParser.parse("I am 5 foot 10 inches tall");
        assertEquals("70.0", result.heightStr);
        assertEquals("inch", result.heightUnit);

        result = SpeechParser.parse("5 ft 6");
        assertEquals("66.0", result.heightStr);
        assertEquals("inch", result.heightUnit);
    }

    @Test
    public void testParseBoth() {
        SpeechParser.ParsingResult result = SpeechParser.parse("My weight is 80kg and height is 6 feet");
        assertEquals("80", result.weightStr);
        assertEquals("kg", result.weightUnit);
        assertEquals("72.0", result.heightStr);
        assertEquals("inch", result.heightUnit);
    }
}

package com.ritesh.bmi.calculator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechParser {

    public static class ParsingResult {
        public final String weightStr;
        public final String weightUnit;
        public final String heightStr;
        public final String heightUnit;

        public ParsingResult(String weightStr, String weightUnit, String heightStr, String heightUnit) {
            this.weightStr = weightStr;
            this.weightUnit = weightUnit;
            this.heightStr = heightStr;
            this.heightUnit = heightUnit;
        }
    }

    public static ParsingResult parse(String spokenText) {
        Pattern weightPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(kg|kilogram|pound|pond)");
        Pattern heightPattern = Pattern.compile("(?:(\\d+(?:\\.\\d+)?)\\s*(?:ft|foot|feet)(?:\\s*(?:and\\s*)?(\\d+(?:\\.\\d+)?)\\s*(?:in|inch|inches)?)?)|(?:(\\d+(?:\\.\\d+)?)\\s*(cm|in|inch|inches))");

        Matcher weightMatcher = weightPattern.matcher(spokenText.toLowerCase());
        Matcher heightMatcher = heightPattern.matcher(spokenText.toLowerCase());

        String weightStr = null;
        String weightUnit = null;
        String heightStr = null;
        String heightUnit = null;

        if (weightMatcher.find()) {
            weightStr = weightMatcher.group(1);
            weightUnit = weightMatcher.group(2);
        }

        if (heightMatcher.find()) {
            if (heightMatcher.group(1) != null) { // Foot and inch case
                try {
                    double feet = Double.parseDouble(heightMatcher.group(1));
                    double inches = 0;
                    if (heightMatcher.group(2) != null) {
                        inches = Double.parseDouble(heightMatcher.group(2));
                    }
                    heightStr = String.valueOf(Conversion.convertFeetAndInchesToInches.apply(feet, inches));
                    heightUnit = "inch";
                } catch (NumberFormatException e) {
                    heightStr = null;
                }
            } else if (heightMatcher.group(3) != null) { // cm or inch case
                heightStr = heightMatcher.group(3);
                String unit = heightMatcher.group(4);
                if (unit != null && unit.equalsIgnoreCase("cm")) {
                    heightUnit = "cm";
                } else {
                    heightUnit = "inch";
                }
            }
        }

        return new ParsingResult(weightStr, weightUnit, heightStr, heightUnit);
    }
}

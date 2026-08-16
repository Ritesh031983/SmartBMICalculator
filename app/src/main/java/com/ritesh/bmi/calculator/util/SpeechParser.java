package com.ritesh.bmi.calculator.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechParser {

    public sealed interface ParsingResult {
        String weightStr();
        String weightUnit();
        String heightStr();
        String heightUnit();

        record Success(String weightStr, String weightUnit, String heightStr, String heightUnit) implements ParsingResult {}
        record Failure(String weightStr, String weightUnit, String heightStr, String heightUnit) implements ParsingResult {}
    }

    @SuppressWarnings("all")
    public static ParsingResult parse(String spokenText) {
        Pattern weightPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(kg|kilogram|pound|pond)");
        Pattern heightPattern = Pattern.compile("(?:(\\d+(?:\\.\\d+)?)\\s*(?:ft|foot|feet|feat|fit)(?:\\s*(?:and\\s*)?(\\d+(?:\\.\\d+)?)\\s*(?:in|inch|inches)?)?)|(?:(\\d+(?:\\.\\d+)?)\\s*(cm|in|inch|inches))");

        Matcher weightMatcher = weightPattern.matcher(spokenText.toLowerCase());
        Matcher heightMatcher = heightPattern.matcher(spokenText.toLowerCase());

        String weightStr = null;
        String weightUnit = null;
        if (weightMatcher.find()) {
            weightStr = weightMatcher.group(1);
            String rawUnit = weightMatcher.group(2);
            if ("pound".equals(rawUnit) || "pond".equals(rawUnit)) {
                weightUnit = "pound";
            } else {
                weightUnit = "kg";
            }
        }

        String heightStr = null;
        String heightUnit = null;
        if (heightMatcher.find()) {
            if (Objects.nonNull(heightMatcher.group(1))) { // Foot and inch case
                try {
                    double feet = Double.parseDouble(Objects.requireNonNull(heightMatcher.group(1)));
                    double inches = Double.parseDouble(Objects.requireNonNullElse(heightMatcher.group(2), "0"));
                    
                    heightStr = String.valueOf(Conversion.convertFeetAndInchesToInches.apply(feet, inches));
                    heightUnit = "inch";
                } catch (NumberFormatException ignored) {
                }
            } else if (Objects.nonNull(heightMatcher.group(3))) { // cm or inch case
                heightStr = heightMatcher.group(3);
                String unit = heightMatcher.group(4);
                if ("cm".equals(unit)) {
                    heightUnit = "cm";
                } else {
                    heightUnit = "inch";
                }
            }
        }

        if (Objects.nonNull(weightStr) && Objects.nonNull(heightStr)) {
            return new ParsingResult.Success(weightStr, weightUnit, heightStr, heightUnit);
        } else {
            return new ParsingResult.Failure(weightStr, weightUnit, heightStr, heightUnit);
        }
    }
}

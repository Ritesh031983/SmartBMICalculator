package com.ritesh.bmi.calculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private boolean isHeightInCM = true; // Default to true as "cm" is checked by default
    private ComputeBMI computeBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        TextView textViewResult = findViewById(R.id.textViewResult);
        RadioGroup radioGroupHeightUnit = findViewById(R.id.radioGroupHeightUnit);
        this.computeBMI = new ComputeBMI(this.editTextWeight, this.editTextHeight, textViewResult);

        // Set a listener on the RadioGroup to update isHeightInCM
        radioGroupHeightUnit.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            if (checkedId == R.id.radioButtonCm) {
                // "cm" is selected
                isHeightInCM = true;
                editTextHeight.setHint("Enter height (cm)");
                // Perform actions for cm
            } else if (checkedId == R.id.radioButtonInch) {
                // "inch" is selected
                isHeightInCM = false;
                editTextHeight.setHint("Enter height (inch)");
                // Perform actions for inch
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonCalculate.setOnClickListener(v -> {
            this.computeBMI.setIsHeightInCM(this.isHeightInCM);
            this.computeBMI.compute(MainActivity.this);
        });
    }

    public void invokeAIAssistant(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.suggestion);

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.speech_recognition_not_supported, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    String spokenText = result.get(0);
                    parseSpokenTextAndCalculateBmi(spokenText);
                }
            }
        }
    }

    /**
     * Parse the user spoken text into weight and height for calculating BMI.
     *
     * @param spokenText Text spoken by the user.
     */
    private void parseSpokenTextAndCalculateBmi(String spokenText) {
        // Regex to find weight and height values
        // This is a basic regex, you might need to make it more robust for different phrasing
        Pattern weightPattern = Pattern.compile("(?:weight|wait) (?:is\\s*)?(\\d+)\\s*kg"); // Matches "weight is 60 kg" or "weight 60.5 kg" or "wait is 50 kg"
        Pattern heightPattern = Pattern.compile("height (?:is\\s*)?(\\d+)\\s*(cm|inch)"); // Matches "height is 160" or "height is 160.5"

        Matcher weightMatcher = weightPattern.matcher(spokenText.toLowerCase());
        Matcher heightMatcher = heightPattern.matcher(spokenText.toLowerCase());

        String weightStr = null;
        String heightStr = null;
        String heightUnit = null;

        if (weightMatcher.find()) {
            weightStr = weightMatcher.group(1);
        }

        if (heightMatcher.find()) {
            heightStr = heightMatcher.group(1);
            heightUnit = heightMatcher.group(2);
        }

        if (weightStr != null && heightStr != null) {
            editTextWeight.setText(weightStr);
            editTextHeight.setText(heightStr);
            if ((heightUnit != null) && heightUnit.equalsIgnoreCase("inch")) {
                RadioButton radioButtonInch = findViewById(R.id.radioButtonInch);
                radioButtonInch.setChecked(true);
                isHeightInCM = false;
            }
            else {
                RadioButton radioButtonCm = findViewById(R.id.radioButtonCm);
                radioButtonCm.setChecked(true);
                isHeightInCM = true;
            }
            this.computeBMI.setIsHeightInCM(this.isHeightInCM);
            this.computeBMI.compute(this);
        } else {
            Toast.makeText(this, R.string.could_not_understand_weight_height, Toast.LENGTH_LONG).show();
            // You could also try to parse more complex sentences or provide more specific feedback
            if (weightStr == null) {
                Toast.makeText(this, R.string.could_not_find_weight, Toast.LENGTH_SHORT).show();
            }
            if (heightStr == null) {
                Toast.makeText(this, R.string.could_not_find_height, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

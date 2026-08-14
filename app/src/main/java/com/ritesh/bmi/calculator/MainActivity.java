package com.ritesh.bmi.calculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText editTextWeight;
    private EditText editTextHeight;
    private boolean isWeightInKg = true; // Default to true as "kg" is checked by default
    private boolean isHeightInCM = true; // Default to true as "cm" is checked by default
    private ComputeBMI computeBMI;

    private final ActivityResultLauncher<Intent> speechResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> resultArray = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (resultArray != null && !resultArray.isEmpty()) {
                        String spokenText = resultArray.get(0);
                        parseSpokenTextAndCalculateBmi(spokenText);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        TextView textViewResult = findViewById(R.id.textViewResult);
        View layoutBMIResult = findViewById(R.id.layoutBMIResult);
        ImageView imageViewIndicator = findViewById(R.id.imageViewIndicator);
        RadioGroup radioGroupHeightUnit = findViewById(R.id.radioGroupHeightUnit);
        RadioGroup radioGroupWeightUnit = findViewById(R.id.radioGroupWeightUnit);

        computeBMI = new ComputeBMI(
                this.editTextWeight,
                this.editTextHeight,
                textViewResult,
                layoutBMIResult,
                imageViewIndicator
        );

        // Set a listener on the RadioGroup to update isWeightInKg
        radioGroupWeightUnit.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton checked
            if (checkedId == R.id.radioButtonKg) {
                // "kg" is checked
                isWeightInKg = true;
                editTextWeight.setHint(R.string.enter_weight_kg_hint);
            } else if (checkedId == R.id.radioButtonPound) {
                // "pound" is checked
                isWeightInKg = false;
                editTextWeight.setHint(R.string.enter_weight_pound_hint);
            }
        });

        // Set a listener on the RadioGroup to update isHeightInCM
        radioGroupHeightUnit.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton checked
            if (checkedId == R.id.radioButtonCm) {
                // "cm" is checked
                isHeightInCM = true;
                editTextHeight.setHint(R.string.enter_height_cm_hint);
            } else if (checkedId == R.id.radioButtonInch) {
                // "inch" is checked
                isHeightInCM = false;
                editTextHeight.setHint(R.string.enter_height_inch_hint);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonCalculate.setOnClickListener(v -> {
            computeBMI.setIsHeightInCM(this.isHeightInCM);
            computeBMI.setIsWeightInKg(this.isWeightInKg);
            computeBMI.compute(this);
        });
    }

    public void invokeAIAssistant(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.suggestion);

        try {
            speechResultLauncher.launch(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.speech_recognition_not_supported, Toast.LENGTH_SHORT).show();
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
        Pattern weightPattern = Pattern.compile("(?:weight|wait|where) (?:is\\s*)?(\\d+)\\s*(kg|pound|pond)"); // Matches "weight is 60 kg" or "weight 60.5 pound" or "wait is 50 pound"
        Pattern heightPattern = Pattern.compile("height (?:is\\s*)?(\\d+)\\s*(cm|inch)"); // Matches "height is 160" or "height is 160.5"

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
            heightStr = heightMatcher.group(1);
            heightUnit = heightMatcher.group(2);
        }

        if (weightStr != null && heightStr != null) {
            editTextWeight.setText(weightStr);
            editTextHeight.setText(heightStr);

            if ((weightUnit != null) && (weightUnit.equalsIgnoreCase("pound") || weightUnit.equalsIgnoreCase("pond"))) {
                RadioButton radioButtonPound = findViewById(R.id.radioButtonPound);
                radioButtonPound.setChecked(true);
                isWeightInKg = false;
            }
            else {
                RadioButton radioButtonKg = findViewById(R.id.radioButtonKg);
                radioButtonKg.setChecked(true);
                isWeightInKg = true;
            }

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
            computeBMI.setIsWeightInKg(this.isWeightInKg);
            computeBMI.setIsHeightInCM(this.isHeightInCM);
            computeBMI.compute(this);
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

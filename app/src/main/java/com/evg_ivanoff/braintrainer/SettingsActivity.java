package com.evg_ivanoff.braintrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBack;
    private EditText editTextMinValue;
    private EditText editTextMaxValue;
    private TextView textViewBestScore;
    private Button btnReset;

    private int min;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = findViewById(R.id.btnBack);
        editTextMaxValue = findViewById(R.id.editTextMaxValue);
        editTextMinValue = findViewById(R.id.editTextMinValue);
        textViewBestScore = findViewById(R.id.textViewBestScore);
        btnReset = findViewById(R.id.btnReset);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        min = preferences.getInt("min_pref", 5);
        max = preferences.getInt("max_pref", 30);
        editTextMinValue.setText(Integer.toString(min));
        editTextMaxValue.setText(Integer.toString(max));
        int maxScore = preferences.getInt("max", 0);
        String result = String.format("Best score: %s", maxScore);
        textViewBestScore.setText(result);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putInt("max", 0).apply();
                Toast.makeText(SettingsActivity.this, "Best score deleted", Toast.LENGTH_SHORT).show();
                int maxScore = preferences.getInt("max", 0);
                String result = String.format("Best score: %s", maxScore);
                textViewBestScore.setText(result);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
                if (editTextMinValue.getText().equals(null)) {
                    int min_pref = 0;
                    preferences.edit().putInt("min_pref", min_pref).apply();
                } else {
                    int min_pref = Integer.parseInt(editTextMinValue.getText().toString());
                    preferences.edit().putInt("min_pref", min_pref).apply();
                }

                if (editTextMaxValue.getText().equals(null)) {
                    int max_pref = 30;
                    preferences.edit().putInt("max_pref", max_pref).apply();
                } else {
                    int max_pref = Integer.parseInt(editTextMaxValue.getText().toString());
                    preferences.edit().putInt("max_pref", max_pref).apply();
                }
                startActivity(intent);
                finish();
            }
        });
    }
}
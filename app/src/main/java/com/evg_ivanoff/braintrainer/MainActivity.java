package com.evg_ivanoff.braintrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuestion;
    private TextView textViewAnswer0;
    private TextView textViewAnswer1;
    private TextView textViewAnswer2;
    private TextView textViewAnswer3;
    private ArrayList<TextView> opinions = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;
    private int countQuestions = 0;
    private int countRightAnswers = 0;
    private boolean gameOver = false;
    private CountDownTimer timer;
    private long timerLeft = 20;

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewAnswer0 = findViewById(R.id.textViewAnswer0);
        textViewAnswer1 = findViewById(R.id.textViewAnswer1);
        textViewAnswer2 = findViewById(R.id.textViewAnswer2);
        textViewAnswer3 = findViewById(R.id.textViewAnswer3);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        min = preferences.getInt("min_pref", 5);
        max = preferences.getInt("max_pref", 30);

        opinions.add(textViewAnswer0);
        opinions.add(textViewAnswer1);
        opinions.add(textViewAnswer2);
        opinions.add(textViewAnswer3);

        playNext();


        timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {
                timerLeft = l;
                textViewTimer.setText(getTime(l));
                if (l <= 10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (countRightAnswers >= max) {
                    preferences.edit().putInt("max", countRightAnswers).apply();
                }
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("result", countRightAnswers);
                startActivity(intent);
                finish();
            }
        };
        timer.start();
    }

    private void playNext() {
        generateQuestion();
        for (int i = 0; i < opinions.size(); i++) {
            if (i == rightAnswerPosition) {
                opinions.get(i).setText(Integer.toString(rightAnswer));
            } else {
                opinions.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s", countRightAnswers, countQuestions);
        textViewScore.setText(score);
    }

    private String getTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = (int) (seconds / 60);
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        textViewQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random() * 4);
        countQuestions++;
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int choosenAnswer = Integer.parseInt(answer);
            if (choosenAnswer == rightAnswer) {
                Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show();
                countRightAnswers++;
            } else {
                Toast.makeText(this, "Неверно!", Toast.LENGTH_SHORT).show();
            }
            playNext();
        }
    }

}
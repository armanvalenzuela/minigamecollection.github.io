
// Winactivity.java

package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinScreen extends AppCompatActivity {

    private TextView currentStreak;
    private TextView highestScore;
    private Button playNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);

        currentStreak = findViewById(R.id.currentStreakWin);
        highestScore = findViewById(R.id.highestScoreWin);
        playNextButton = findViewById(R.id.playNextButton);

        int streak = getIntent().getIntExtra("currentStreak", 0);
        int highest = getIntent().getIntExtra("highestStreak", 0);

        String gameMode = getIntent().getStringExtra("gameMode");

        currentStreak.setText("Current Streak: " + streak);
        highestScore.setText("Highest Score: " + highest);

        playNextButton.setOnClickListener(v -> {
            Intent intent;

            switch (gameMode) {
                case "normal":
                    intent = new Intent(this, NormalMode.class);
                    break;
                case "hard":
                    intent = new Intent(this, HardMode.class);
                    break;
                case "easy":
                default:
                    intent = new Intent(this, EasyMode.class);
                    break;
            }

            intent.putExtra("currentStreak", streak);
            intent.putExtra("highestStreak", highest);
            intent.putExtra("gameMode", gameMode);
            startActivity(intent);
            finish();
        });
    }
}

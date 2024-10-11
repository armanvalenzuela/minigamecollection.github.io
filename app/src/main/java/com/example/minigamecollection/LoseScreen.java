
// Loseactivity.java

package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class LoseScreen extends AppCompatActivity {

    private TextView currentStreak;
    private TextView highestScore;
    private Button playAgainButton;
    private String gameMode;

    private static final String MODE_EASY = "easy";
    private static final String MODE_NORMAL = "normal";
    private static final String MODE_HARD = "hard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_screen);

        currentStreak = findViewById(R.id.currentStreakLose);
        highestScore = findViewById(R.id.highestScoreLose);
        playAgainButton = findViewById(R.id.playAgainButton);

        int streak = getIntent().getIntExtra("currentStreak", 0);
        int highest = getIntent().getIntExtra("highestStreak", 0);

        gameMode = getSharedPreferences("GamePrefs", MODE_PRIVATE).getString("gameMode", MODE_EASY);

        currentStreak.setText("Current Streak: " + streak);
        highestScore.setText("Highest Score: " + highest);

        saveHighestStreak(gameMode, highest);

        playAgainButton.setOnClickListener(v -> {
            Intent intent;

            switch (gameMode) {
                case MODE_NORMAL:
                    intent = new Intent(this, NormalMode.class);
                    break;
                case MODE_HARD:
                    intent = new Intent(this, HardMode.class);
                    break;
                case MODE_EASY:
                default:
                    intent = new Intent(this, EasyMode.class);
                    break;
            }

            intent.putExtra("currentStreak", 0);
            intent.putExtra("highestStreak", highest);
            startActivity(intent);
            finish();
        });
    }

    private void saveHighestStreak(String gameMode, int highest) {
        getSharedPreferences("GamePrefs", MODE_PRIVATE)
                .edit()
                .putInt("highestStreak" + capitalizeFirstLetter(gameMode), highest)
                .apply();
    }

    private String capitalizeFirstLetter(String mode) {
        if (mode == null || mode.isEmpty()) {
            return "";
        }
        return mode.substring(0, 1).toUpperCase() + mode.substring(1);
    }
}

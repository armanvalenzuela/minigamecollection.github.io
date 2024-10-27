package com.example.minigamecollection;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinScreen extends AppCompatActivity {

    MediaPlayer bgmPlayer;

    private TextView currentStreak;
    private TextView highestScore;
    private Button playNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);

        bgmPlayer = MediaPlayer.create(this, R.raw.win_sound);
        bgmPlayer.start();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

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

    @Override
    protected void onPause() {
        super.onPause();
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause(); // Pause the music when the activity goes into the background
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgmPlayer != null) {
            bgmPlayer.start(); // Resume the music when the activity comes back into the foreground
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgmPlayer != null) {
            bgmPlayer.release(); // Release MediaPlayer resources when the activity is destroyed
            bgmPlayer = null;
        }
    }
}

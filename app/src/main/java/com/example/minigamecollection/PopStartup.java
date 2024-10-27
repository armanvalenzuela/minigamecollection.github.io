package com.example.minigamecollection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PopStartup extends AppCompatActivity {

    private static final String PREFS_NAME = "PopItGamePrefs";
    private static final String HIGH_SCORE_KEY = "HighScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_startup);

        BackgroundMusicPlayer.startBackgroundMusic(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent Easyintent = new Intent(PopStartup.this, GameList.class);
            startActivity(Easyintent);
            finish();
        });

        Button startGameButton = findViewById(R.id.startGameButton);
        Button instructionsButton = findViewById(R.id.instructionsButton);
        Button resetHighScoreButton = findViewById(R.id.resetHighScoreButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopStartup.this, PopGameplay.class);
                startActivity(intent);
            }
        });

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopStartup.this, PopInstructions.class);
                startActivity(intent);
            }
        });

        resetHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetHighScore();
            }
        });
    }

    private void resetHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGH_SCORE_KEY, 0);
        editor.apply();
        Toast.makeText(this, "High Score has been reset!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundMusicPlayer.pauseBackgroundMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusicPlayer.startBackgroundMusic(this);
    }
}

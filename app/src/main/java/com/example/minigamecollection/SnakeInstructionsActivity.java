package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SnakeInstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snake_instructions); // Set the new instructions layout

        BackgroundMusicPlayer.startBackgroundMusic(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Back to Main Menu button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Go back to StartupActivity (Main Menu)
            Intent intent = new Intent(SnakeInstructionsActivity.this, Snake.class);
            startActivity(intent);
            finish(); // Close the instructions activity
        });
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

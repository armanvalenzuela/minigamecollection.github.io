package com.example.minigamecollection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PopGameplay extends AppCompatActivity {

    MediaPlayer bgmPlayer;

    private static final String PREFS_NAME = "PopItGamePrefs";
    private static final String HIGH_SCORE_KEY = "HighScore";
    private List<Button> tiles = new ArrayList<>();
    private List<Integer> sequence = new ArrayList<>();
    private int currentStep = 0;
    private int score = 0;
    private int highScore = 0;
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean playerTurn = false;
    private TextView scoreText;
    private TextView highScoreText;
    private TextView messageText;
    private Button startButton;
    private Button mainMenuButton;
    private Button fetchLeaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_gameplay);

        bgmPlayer = MediaPlayer.create(this, R.raw.clock_tower);
        bgmPlayer.setLooping(true);
        bgmPlayer.start();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        GridLayout tileGrid = findViewById(R.id.tileGrid);
        scoreText = findViewById(R.id.scoreText);
        highScoreText = findViewById(R.id.highScoreText);
        messageText = findViewById(R.id.messageText);
        startButton = findViewById(R.id.startButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);
        fetchLeaderboardButton = findViewById(R.id.fetchLeaderboardButton);

        // Load the high score from SharedPreferences
        loadHighScore();

        // Add all buttons to the tiles list
        for (int i = 0; i < tileGrid.getChildCount(); i++) {
            Button tile = (Button) tileGrid.getChildAt(i);
            tiles.add(tile);
            final int index = i;

            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerTurn) {
                        highlightTile(index); // Highlight when the player presses the button
                        checkPlayerInput(index);
                    }
                }
            });
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                startButton.setText("Restart");
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the startup activity
                Intent intent = new Intent(PopGameplay.this, PopStartup.class);
                BackgroundMusicPlayer.resetBackgroundMusic(PopGameplay.this);
                startActivity(intent);
                finish(); // Close the current game screen
            }
        });

        fetchLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open leaderboard activity
                Intent intent = new Intent(PopGameplay.this, LeaderboardActivity.class);
                intent.putExtra("USER_SCORE", score);
                startActivity(intent);
                finish(); // Close the current game screen
            }
        });
    }

    private void restartGame() {
        sequence.clear();
        currentStep = 0;
        score = 0;
        updateScore();
        updateMessage("");
        playerTurn = false;
        addToSequence();
        showSequence();
    }

    private void addToSequence() {
        int nextTile = random.nextInt(tiles.size());
        sequence.add(nextTile);
    }

    private void showSequence() {
        playerTurn = false;
        currentStep = 0;

        for (int i = 0; i < sequence.size(); i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    highlightTile(sequence.get(index));
                }
            }, 500 * i);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerTurn = true;
                updateMessage("Your turn!");
            }
        }, 500 * sequence.size());
    }

    private void highlightTile(int tileIndex) {
        Button tile = tiles.get(tileIndex);
        tile.setAlpha(0.5f); // Highlight the tile by reducing its opacity

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tile.setAlpha(1.0f); // Reset the opacity back to normal
            }
        }, 150);
    }

    private void checkPlayerInput(int tileIndex) {
        if (sequence.get(currentStep) == tileIndex) {
            currentStep++;
            if (currentStep == sequence.size()) {
                score++; // Increase score after each successful round
                updateScore(); // Update the score display
                updateMessage("Good job! Get ready for the next round!");

                playerTurn = false;

                // Add a 1-second delay before starting the next round
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addToSequence();
                        showSequence();
                    }
                }, 1000); // 1-second delay after finishing a round
            }
        } else {
            updateMessage("Wrong tile! Game over.");
            playerTurn = false;
            if (score > highScore) {
                highScore = score; // Update high score if the current score is greater
                saveHighScore(); // Save the high score to SharedPreferences
                updateHighScore(); // Update the high score display
                updateMessage("New High Score: " + highScore + "! Congratulations!"); // Display high score beaten message
            }
            // Do not reset the score here to maintain it after losing
            startButton.setText("Start Game"); // Change button text back to Start Game if the game is over
        }
    }

    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    private void updateHighScore() {
        highScoreText.setText("High Score: " + highScore);
    }

    private void updateMessage(String message) {
        messageText.setText(message);
    }

    private void saveHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }

    private void loadHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0);
        updateHighScore(); // Update the high score display
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgmPlayer != null) {
            bgmPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgmPlayer != null) {
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }
}

package com.example.minigamecollection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private static final String PREFS_NAME = "PopItGamePrefs";
    private static final String HIGH_SCORE_KEY = "HighScore";

    private List<Button> tiles = new ArrayList<>();
    private List<Integer> sequence = new ArrayList<>();
    private int currentStep = 0;
    private int score = 0; // Track player's score
    private int highScore = 0; // Track player's high score
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean playerTurn = false;
    private TextView scoreText; // TextView to display the score
    private TextView highScoreText; // TextView to display the high score
    private TextView messageText; // TextView to display messages
    private Button startButton; // Button for starting/restarting the game
    private Button mainMenuButton; // Button to return to the main menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_gameplay);

        GridLayout tileGrid = findViewById(R.id.tileGrid);
        scoreText = findViewById(R.id.scoreText); // Find the scoreText TextView
        highScoreText = findViewById(R.id.highScoreText); // Find the highScoreText TextView
        messageText = findViewById(R.id.messageText); // Find the messageText TextView
        startButton = findViewById(R.id.startButton); // Find the start/restart button
        mainMenuButton = findViewById(R.id.mainMenuButton); // Find the main menu button

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
                startButton.setText("Restart"); // Change the button text to Restart after starting the game
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the startup activity
                Intent intent = new Intent(PopGameplay.this, PopStartup.class);
                startActivity(intent);
                finish(); // Close the current game screen
            }
        });
    }

    private void restartGame() {
        sequence.clear();
        currentStep = 0;
        score = 0; // Reset the score when restarting the game
        updateScore(); // Update the score display
        updateMessage(""); // Clear the message text
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
            }, 1000 * i);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerTurn = true;
                updateMessage("Your turn!");
            }
        }, 1000 * sequence.size());
    }

    private void highlightTile(int tileIndex) {
        Button tile = tiles.get(tileIndex);
        tile.setAlpha(0.5f); // Highlight the tile by reducing its opacity

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tile.setAlpha(1.0f); // Reset the opacity back to normal
            }
        }, 500);
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
}

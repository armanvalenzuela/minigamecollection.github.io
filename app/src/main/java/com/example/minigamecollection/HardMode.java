// Hardmode.java

package com.example.minigamecollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HardMode extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 5;
    private static final String PREFS_NAME_HARD = "WordGuessPrefsHard";
    private static final String KEY_HIGHEST_STREAK_HARD = "highestStreakHard";

    private final String[] wordList = {"CHAIR", "TABLE", "CLOTH", "PAPER", "GLASS"};
    private String currentWord;
    private int remainingAttempts;
    private int currentStreak;
    private int highestStreak;
    private int activeRow = 0;

    private GridLayout gridLayout;
    private GridLayout keyboardLayout;
    private final TextView[] guessBoxes = new TextView[MAX_ATTEMPTS * WORD_LENGTH];
    private TextView streakLabel;
    private TextView highestStreakLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hard_mode);

        initializeViews();
        loadHighScoreHard();

        getSharedPreferences("GamePrefs", MODE_PRIVATE)
                .edit()
                .putString("gameMode", "hard")
                .apply();

        currentStreak = getIntent().getIntExtra("currentStreak", 0);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent mainIntent = new Intent(HardMode.this, WordGuess.class);
            startActivity(mainIntent);
            finish();
        });

        setupKeyboard();
        setupGrid();
        initializeGame();
    }

    private void saveHighScoreHard() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME_HARD, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_HIGHEST_STREAK_HARD, highestStreak);
        editor.apply();
    }

    private void loadHighScoreHard() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME_HARD, MODE_PRIVATE);
        highestStreak = preferences.getInt(KEY_HIGHEST_STREAK_HARD, 0);
        updateStreakLabels();
    }

    private void initializeViews() {
        gridLayout = findViewById(R.id.gridLayout);
        keyboardLayout = findViewById(R.id.keyboardLayout);
        Button submitButton = findViewById(R.id.submitButton);
        streakLabel = findViewById(R.id.currentStreak);
        highestStreakLabel = findViewById(R.id.highestScore);

        submitButton.setOnClickListener(v -> checkGuess());
    }

    private void setupGrid() {
        gridLayout.removeAllViews();
        gridLayout.setBackgroundColor(Color.BLACK);

        int boxSize = 150;

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            for (int j = 0; j < WORD_LENGTH; j++) {
                LinearLayout square = new LinearLayout(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = boxSize;
                params.height = boxSize;
                params.setMargins(25, 10, 10, 50);
                square.setLayoutParams(params);
                square.setBackgroundColor(Color.DKGRAY);
                square.setOrientation(LinearLayout.HORIZONTAL);
                square.setGravity(Gravity.CENTER);

                TextView letterView = new TextView(this);
                letterView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                letterView.setTextSize(30);
                letterView.setGravity(Gravity.CENTER);
                letterView.setTextColor(Color.WHITE);
                letterView.setTypeface(null, Typeface.BOLD);

                square.addView(letterView);
                gridLayout.addView(square);
                guessBoxes[i * WORD_LENGTH + j] = letterView;
            }
        }
    }

    private void setupKeyboard() {
        String[] letters = {
                "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                "A", "S", "D", "F", "G", "H", "J", "K", "L",
                "Z", "X", "C", "V", "B", "N", "M", "DEL"
        };

        for (String letter : letters) {
            Button button = new Button(this);
            button.setText(letter);
            button.setBackgroundColor(Color.LTGRAY);
            button.setTextColor(Color.WHITE);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 150;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(4, 4, 4, 4);

            button.setLayoutParams(params);
            button.setPadding(16, 16, 16, 16);
            button.setOnClickListener(v -> handleKeyboardInput(letter));
            keyboardLayout.addView(button);
        }
    }

    private void handleKeyboardInput(String input) {
        if (input.equals("DEL")) {
            deleteLastInput();
        } else {
            int[] emptySquare = getFirstEmptySquareInActiveRow();
            if (emptySquare != null) {
                int row = emptySquare[0];
                int col = emptySquare[1];
                TextView letterView = guessBoxes[row * WORD_LENGTH + col];
                if (letterView.getText().length() < WORD_LENGTH) {
                    letterView.setText(input);
                }
            }
        }
    }

    private void deleteLastInput() {
        for (int i = WORD_LENGTH - 1; i >= 0; i--) {
            TextView letterView = guessBoxes[activeRow * WORD_LENGTH + i];
            if (!letterView.getText().toString().isEmpty()) {
                letterView.setText("");
                return;
            }
        }
    }

    private int[] getFirstEmptySquareInActiveRow() {
        for (int j = 0; j < WORD_LENGTH; j++) {
            TextView letterView = guessBoxes[activeRow * WORD_LENGTH + j];
            if (letterView.getText().toString().isEmpty()) {
                return new int[]{activeRow, j};
            }
        }
        return null;
    }

    private void checkGuess() {
        StringBuilder guess = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            guess.append(guessBoxes[activeRow * WORD_LENGTH + i].getText().toString());
        }

        if (guess.length() != WORD_LENGTH) {
            Toast.makeText(this, "Please complete your guess.", Toast.LENGTH_SHORT).show();
            return;
        }

        guess = new StringBuilder(guess.toString().toUpperCase());

        if (isWordGuessed(guess.toString())) {
            displayWinMessage();
        } else {
            remainingAttempts--;
            displayGuessedLetters(guess.toString());
            if (remainingAttempts == 0) {
                displayLossMessage();
                updateStreakLabels();
            } else {
                activeRow++;
                if (activeRow >= MAX_ATTEMPTS) {
                    activeRow = MAX_ATTEMPTS - 1;
                }
            }
        }
    }


    private boolean isWordGuessed(String guess) {
        return guess.equals(currentWord);
    }

    private void displayGuessedLetters(String guess) {
        boolean[] usedInWord = new boolean[WORD_LENGTH];
        boolean[] usedInGuess = new boolean[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            TextView letterView = guessBoxes[(MAX_ATTEMPTS - remainingAttempts - 1) * WORD_LENGTH + i];
            if (currentWord.charAt(i) == guess.charAt(i)) {
                letterView.setBackgroundColor(Color.parseColor("#6BBF4A")); // GREEN
                usedInWord[i] = true;
                usedInGuess[i] = true;
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            TextView letterView = guessBoxes[(MAX_ATTEMPTS - remainingAttempts - 1) * WORD_LENGTH + i];
            if (!usedInGuess[i]) {
                char guessedChar = guess.charAt(i);
                for (int j = 0; j < WORD_LENGTH; j++) {
                    if (!usedInWord[j] && currentWord.charAt(j) == guessedChar) {
                        letterView.setBackgroundColor(Color.parseColor("#BDAA3B")); //YELLOW
                        usedInWord[j] = true;
                        break;
                    } else {
                        letterView.setBackgroundColor(Color.parseColor("#77766D")); // GRAY
                    }
                }
            }
        }
    }

    private void displayWinMessage() {
        int index = (MAX_ATTEMPTS - remainingAttempts) * WORD_LENGTH;
        for (int i = 0; i < WORD_LENGTH; i++) {
            guessBoxes[index + i].setBackgroundColor(Color.parseColor("#6BBF4A")); // GREEN
            guessBoxes[index + i].setText(String.valueOf(currentWord.charAt(i)));
        }

        currentStreak++;
        if (currentStreak > highestStreak) {
            highestStreak = currentStreak;
            saveHighScoreHard();
        }

        Toast.makeText(this, "Current Streak: " + currentStreak, Toast.LENGTH_SHORT).show();
        updateStreakLabels();

        Intent hardIntent = new Intent(HardMode.this, WinScreen.class);
        hardIntent.putExtra("currentStreak", currentStreak);
        hardIntent.putExtra("highestStreak", highestStreak);
        hardIntent.putExtra("gameMode", "hard");
        startActivity(hardIntent);
        finish();
    }


    private void displayLossMessage() {
        Toast.makeText(this, "You've run out of attempts! The word was: " + currentWord, Toast.LENGTH_LONG).show();

        Intent loseIntent = new Intent(HardMode.this, LoseScreen.class);
        loseIntent.putExtra("currentStreak", currentStreak);
        loseIntent.putExtra("highestStreak", highestStreak);
        startActivity(loseIntent);
        finish();
    }

    private void updateStreakLabels() {
        streakLabel.setText("Current Streak: " + currentStreak);
        highestStreakLabel.setText("Highest Streak: " + highestStreak);
    }

    private void initializeGame() {
        remainingAttempts = MAX_ATTEMPTS;
        currentWord = wordList[(int) (Math.random() * wordList.length)];
        activeRow = 0;
        updateStreakLabels();
    }
}

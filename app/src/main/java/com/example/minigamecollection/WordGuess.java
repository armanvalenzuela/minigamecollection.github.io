package com.example.minigamecollection;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;




public class WordGuess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.word_guess);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent backintent = new Intent(WordGuess.this, GameList.class);
            startActivity(backintent);
        });

        RadioGroup difficultyGroup = findViewById(R.id.difficultyGroup);
        Button playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(v -> {
            int selectedId = difficultyGroup.getCheckedRadioButtonId();
            RadioButton selectedButton = findViewById(selectedId);
            if (selectedButton != null) {
                Intent intent;
                String difficulty = selectedButton.getText().toString().toLowerCase(); // Convert to lowercase

                // Store game mode in SharedPreferences
                getSharedPreferences("GamePrefs", MODE_PRIVATE)
                        .edit()
                        .putString("gameMode", difficulty) // Save game mode for consistency
                        .apply();

                // Use if-else instead of switch
                if (difficulty.equals("easy")) {
                    intent = new Intent(WordGuess.this, EasyMode.class);
                } else if (difficulty.equals("normal")) {
                    intent = new Intent(WordGuess.this, NormalMode.class);
                } else if (difficulty.equals("hard")) {
                    intent = new Intent(WordGuess.this, HardMode.class);
                } else {
                    Toast.makeText(WordGuess.this, "Unknown difficulty level", Toast.LENGTH_SHORT).show();
                    return; // Exit if something goes wrong
                }

                // Start the selected difficulty mode
                startActivity(intent);
            } else {
                Toast.makeText(WordGuess.this, "Please select a difficulty level", Toast.LENGTH_SHORT).show();
            }
        });








    }
}
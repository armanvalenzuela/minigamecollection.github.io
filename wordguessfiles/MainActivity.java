// MainActivity.java

package com.example.wordguessgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    intent = new Intent(MainActivity.this, Easymode.class);
                } else if (difficulty.equals("normal")) {
                    intent = new Intent(MainActivity.this, Normalmode.class);
                } else if (difficulty.equals("hard")) {
                    intent = new Intent(MainActivity.this, Hardmode.class);
                } else {
                    Toast.makeText(MainActivity.this, "Unknown difficulty level", Toast.LENGTH_SHORT).show();
                    return; // Exit if something goes wrong
                }

                // Start the selected difficulty mode
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please select a difficulty level", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

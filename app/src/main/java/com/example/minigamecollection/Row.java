package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Row extends AppCompatActivity {

    private RadioGroup difficultyGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_menu);

        ImageView backButton = findViewById(R.id.back_arrow);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameList.class);
            startActivity(intent);
        });

        difficultyGroup = findViewById(R.id.difficultyGroup);
        Button playBotButton = findViewById(R.id.playBotButton);
        Button playFriendButton = findViewById(R.id.playFriendButton);

        playBotButton.setOnClickListener(view -> {
            int selectedId = difficultyGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(Row.this, "Please select a difficulty level!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = findViewById(selectedId);
            String difficulty = selectedButton.getText().toString();

            Intent intent;
            if ("Easy".equalsIgnoreCase(difficulty)) {
                intent = new Intent(Row.this, EasyRow.class);
            } else if ("Normal".equalsIgnoreCase(difficulty)) {
                intent = new Intent(Row.this, NormalRow.class);
            } else if ("Hard".equalsIgnoreCase(difficulty)) {
                intent = new Intent(Row.this, HardRow.class);
            } else {
                Toast.makeText(Row.this, "This difficulty is not implemented yet.", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("MODE", "BOT");
            intent.putExtra("DIFFICULTY", difficulty);
            startActivity(intent);
        });

        playFriendButton.setOnClickListener(view -> {
            Intent intent = new Intent(Row.this, VsPlayer.class);
            intent.putExtra("MODE", "FRIEND");
            startActivity(intent);
        });
    }
}
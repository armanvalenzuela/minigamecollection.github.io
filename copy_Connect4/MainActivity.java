package com.example.connect4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup difficultyGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficultyGroup = findViewById(R.id.difficultyGroup);
        Button playBotButton = findViewById(R.id.playBotButton);
        Button playFriendButton = findViewById(R.id.playFriendButton);

        playBotButton.setOnClickListener(view -> {
            int selectedId = difficultyGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(MainActivity.this, "Please select a difficulty level!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = findViewById(selectedId);
            String difficulty = selectedButton.getText().toString();

            Intent intent;
            if ("Easy".equalsIgnoreCase(difficulty)) {
                intent = new Intent(MainActivity.this, Playerxbot_easymode.class);
            } else if ("Normal".equalsIgnoreCase(difficulty)) {
                intent = new Intent(MainActivity.this, Playerxbot_normalmode.class);
            } else if ("Hard".equalsIgnoreCase(difficulty)) {
                intent = new Intent(MainActivity.this, Playerxbot_hardmode.class);
            } else {
                Toast.makeText(MainActivity.this, "This difficulty is not implemented yet.", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("MODE", "BOT");
            intent.putExtra("DIFFICULTY", difficulty);
            startActivity(intent);
        });

        playFriendButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Playerxplayer.class);
            intent.putExtra("MODE", "FRIEND");
            startActivity(intent);
        });
    }
}

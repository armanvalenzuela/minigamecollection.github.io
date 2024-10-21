package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerSetup extends AppCompatActivity {

    private EditText player1;
    private EditText player2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.player_setup);

        player1 = findViewById(R.id.player1Name);
        player2 = findViewById(R.id.player2Name);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent Easyintent = new Intent(PlayerSetup.this, TicTacToe.class);
            startActivity(Easyintent);
            finish();
        });

    }

    public void submitButtonClick(View view) {
        String player1Name = player1.getText().toString().trim();
        String player2Name = player2.getText().toString().trim();

        // Check if both player names are entered
        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            // Show a message if either field is empty
            Toast.makeText(this, "Both players must enter their names!", Toast.LENGTH_SHORT).show();
        } else {
            // If both names are entered, proceed to the next activity
            Intent intent = new Intent(this, PlayerMode.class);
            intent.putExtra("PLAYER_NAMES", new String[]{player1Name, player2Name});
            startActivity(intent);
        }
    }
}
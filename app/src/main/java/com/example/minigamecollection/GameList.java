package com.example.minigamecollection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class GameList extends AppCompatActivity {

    TextView welcomeTextView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_list);

        // Retrieve the username passed from UserActivity
        welcomeTextView = findViewById(R.id.welcomeText);
        String username = getIntent().getStringExtra("USERNAME");

          // Connect to the TextView that shows the welcome message
        if (username != null) {
            welcomeTextView.setText("Welcome, " + username + "." + " \nHave fun and enjoy our game!");
        } else {
            welcomeTextView.setText("Welcome back!");
        }
    }

    public void WordGuessButton(View view){
        Intent intent = new Intent(this, WordGuess.class);
        startActivity(intent);
    }

    public void TicTacToeButton(View view){
        Intent intent = new Intent(this, TicTacToe.class);
        startActivity(intent);
    }

    public void SnakeButton(View view){
        Intent intent = new Intent(this, Snake.class);
        startActivity(intent);
    }

    public void PopButton(View view){
        Intent intent = new Intent(this, PopStartup.class);
        startActivity(intent);
    }
    public void RowButton(View view){
        Intent intent = new Intent(this, Row.class);
        startActivity(intent);
    }
}


package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class PlayerMode extends AppCompatActivity {

    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.player_mode);

        Button playAgain = findViewById(R.id.playAgain);
        Button home = findViewById(R.id.homeButton);
        TextView playerTurn = findViewById(R.id.playerDisplay);

        playAgain.setVisibility(View.GONE);
        home.setVisibility(View.GONE);


        String[] playerNames = getIntent().getStringArrayExtra("PLAYER_NAMES");

        if (playerNames != null){
            playerTurn.setText((playerNames[0] + " 's Turn"));
        }

        board = findViewById(R.id.board);

        board.setUpGame(playAgain, home, playerTurn, playerNames);

    }

    public void playAgainButtonClick(View view){
        board.resetGame();
        board.invalidate();
    }

    public void homeButtonClick (View view) {
        Intent intent = new Intent(this, TicTacToe.class);
        startActivity(intent);
    }


}
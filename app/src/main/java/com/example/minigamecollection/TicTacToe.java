package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tic_tac_toe);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent backintent = new Intent(TicTacToe.this, GameList.class);
            startActivity(backintent);
        });

    }

    public void playButton2(View view){
        Intent intent = new Intent(this, PlayerSetup.class);
        startActivity(intent);
    }

}
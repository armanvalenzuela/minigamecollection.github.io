package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tic_tac_toe);
    }

    public void playButton(View view){
        Intent intent = new Intent(this, PlayerSetup.class);
        startActivity(intent);
    }

    public void playButton2(View view){
        Intent intent = new Intent(this, PlayerSetup.class);
        startActivity(intent);
    }

}
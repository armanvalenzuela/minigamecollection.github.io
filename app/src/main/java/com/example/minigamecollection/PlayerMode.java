package com.example.minigamecollection;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class PlayerMode extends AppCompatActivity {

    MediaPlayer bgmPlayer;

    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.player_mode);

        bgmPlayer = MediaPlayer.create(this, R.raw.overworld); // replace with your music file
        bgmPlayer.setLooping(true); // Enable looping if you want continuous background music
        bgmPlayer.start(); // Start playing the music


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

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
        BackgroundMusicPlayer.resetBackgroundMusic(this);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause(); // Pause the music when the activity goes into the background
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgmPlayer != null) {
            bgmPlayer.start(); // Resume the music when the activity comes back into the foreground
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgmPlayer != null) {
            bgmPlayer.release(); // Release MediaPlayer resources when the activity is destroyed
            bgmPlayer = null;
        }
    }

}
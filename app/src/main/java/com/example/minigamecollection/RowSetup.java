package com.example.minigamecollection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RowSetup extends AppCompatActivity {

    private EditText player1;
    private EditText player2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.row_setup);

        BackgroundMusicPlayer.startBackgroundMusic(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent Easyintent = new Intent(RowSetup.this, Row.class);
            startActivity(Easyintent);
            finish();
        });

    }

    public void submitButton(View view) {
        String player1Name = player1.getText().toString().trim();
        String player2Name = player2.getText().toString().trim();

        // Check if both player names are entered
        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            // Show a message if either field is empty
            Toast.makeText(this, "Both players must enter their names!", Toast.LENGTH_SHORT).show();
        } else {
            // If both names are entered, proceed to the next activity
            Intent intent = new Intent(this, VsPlayer.class);
            intent.putExtra("PLAYER_NAMES", new String[]{player1Name, player2Name});
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundMusicPlayer.pauseBackgroundMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusicPlayer.startBackgroundMusic(this);
    }
}
package com.example.minigamecollection;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class HardRow extends AppCompatActivity {

    MediaPlayer bgmPlayer;

    private static final int ROWS = 6;
    private static final int COLS = 6;
    private char[][] board;
    private char currentPlayer;
    private boolean isPlayerTurn;
    private boolean gameOver;
    private TextView player1Turn;
    private TextView hardBotTurn;
    private GridLayout gameBoard;
    private Random random;

    private char lastWinner = 'R';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hard_row);

        bgmPlayer = MediaPlayer.create(this, R.raw.pop);
        bgmPlayer.setLooping(true);
        bgmPlayer.start();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(HardRow.this, Row.class);
            BackgroundMusicPlayer.resetBackgroundMusic(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        board = new char[ROWS][COLS];
        currentPlayer = 'R';
        player1Turn = findViewById(R.id.player1_turn);
        hardBotTurn = findViewById(R.id.player2_turn);
        gameBoard = findViewById(R.id.game_board);
        gameOver = false;
        random = new Random();

        isPlayerTurn = true;
        player1Turn.setVisibility(View.VISIBLE);
        hardBotTurn.setVisibility(View.INVISIBLE);

        initializeBoard();
    }

    private void initializeBoard() {
        gameBoard.removeAllViews();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = '.';
                final int column = c;
                ImageView circle = new ImageView(this);
                circle.setLayoutParams(new GridLayout.LayoutParams(
                        GridLayout.spec(r), GridLayout.spec(c)));
                circle.setImageResource(R.drawable.empty_circle);
                circle.setOnClickListener(v -> {
                    if (isPlayerTurn && !gameOver) {
                        dropPiece(column);
                    }
                });
                gameBoard.addView(circle);
            }
        }
    }

    private void dropPiece(int col) {
        if (gameOver) return;

        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == '.') {
                board[r][col] = currentPlayer;
                ImageView circle = (ImageView) gameBoard.getChildAt(r * COLS + col);
                circle.setImageResource(currentPlayer == 'R' ? R.drawable.red_circle : R.drawable.yellow_circle);
                if (checkWinForPlayer(currentPlayer)) {
                    showWinner();
                } else if (checkDraw()) {
                    showDraw();
                } else {
                    switchPlayer();
                }
                return;
            }
        }
    }

    private boolean checkDraw() {
        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == '.') {
                return false;
            }
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R';

        player1Turn.setVisibility(currentPlayer == 'R' ? View.VISIBLE : View.INVISIBLE);
        hardBotTurn.setVisibility(currentPlayer == 'Y' ? View.VISIBLE : View.INVISIBLE);


        if (currentPlayer == 'Y') {
            isPlayerTurn = false;
            hardBotTurn.setText("Bot's Turn");
            hardBotTurn.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(this::makeHardBotMove, 1000);
        } else {
            isPlayerTurn = true;
            hardBotTurn.setVisibility(View.INVISIBLE);
        }
    }

    private void makeHardBotMove() {
        int col = findBestMove();
        dropPiece(col);
    }

    private int findBestMove() {
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(col)) {
                if (willMoveWin(col, 'Y')) {
                    return col;
                }
                if (willMoveWin(col, 'R')) {
                    return col;
                }
            }
        }

        return randomMove();
    }

    private boolean willMoveWin(int col, char player) {
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == '.') {
                board[r][col] = player;
                boolean result = checkWinForPlayer(player);
                board[r][col] = '.';
                return result;
            }
        }
        return false;
    }

    private boolean checkWinForPlayer(char player) {
        return checkHorizontal(player) || checkVertical(player) || checkDiagonal(player);
    }

    private boolean checkHorizontal(char player) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == player && board[r][c + 1] == player &&
                        board[r][c + 2] == player && board[r][c + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVertical(char player) {
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS - 3; r++) {
                if (board[r][c] == player && board[r + 1][c] == player &&
                        board[r + 2][c] == player && board[r + 3][c] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal(char player) {
        for (int r = 3; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == player && board[r - 1][c + 1] == player &&
                        board[r - 2][c + 2] == player && board[r - 3][c + 3] == player) {
                    return true;
                }
            }
        }
        for (int r = 0; r < ROWS - 3; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == player && board[r + 1][c + 1] == player &&
                        board[r + 2][c + 2] == player && board[r + 3][c + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private int randomMove() {
        int col;
        do {
            col = random.nextInt(COLS);
        } while (!isValidMove(col));
        return col;
    }

    private boolean isValidMove(int col) {
        return board[0][col] == '.';
    }

    private void showDraw() {
        gameOver = true;
        String drawMessage = "It's a draw!";
        player1Turn.setVisibility(View.INVISIBLE);
        hardBotTurn.setVisibility(View.INVISIBLE);

        new android.os.Handler().postDelayed(() -> {
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage(drawMessage)
                    .setCancelable(false)
                    .show();

            new android.os.Handler().postDelayed(() -> {
                dialog.dismiss();
                resetGame();
            }, 2000);
        }, 1500);
    }

    private void showWinner() {
        gameOver = true;
        lastWinner = currentPlayer;
        String winnerMessage = currentPlayer == 'R' ? "You win!" : "Bot wins!";
        player1Turn.setVisibility(View.INVISIBLE);
        hardBotTurn.setVisibility(View.INVISIBLE);

        new android.os.Handler().postDelayed(() -> {
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage(winnerMessage)
                    .setCancelable(false)
                    .show();

            new android.os.Handler().postDelayed(() -> {
                dialog.dismiss();
                resetGame();
            }, 2000);
        }, 1500);
    }

    private void resetGame() {
        initializeBoard();
        gameOver = false;

        if (lastWinner == 'R') {
            currentPlayer = 'Y';
            isPlayerTurn = false;
            player1Turn.setVisibility(View.INVISIBLE);
            hardBotTurn.setVisibility(View.VISIBLE);

            new android.os.Handler().postDelayed(this::makeHardBotMove, 1000);
        } else {
            currentPlayer = 'R';
            isPlayerTurn = true;
            player1Turn.setVisibility(View.VISIBLE);
            hardBotTurn.setVisibility(View.INVISIBLE);
        }
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
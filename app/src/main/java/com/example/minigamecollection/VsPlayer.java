
package com.example.minigamecollection;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;


public class VsPlayer extends AppCompatActivity {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private TextView player1Turn;
    private TextView player2Turn;
    private GridLayout gameBoard;
    private char lastWinner = ' ';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vs_player);

        ImageView backArrow = findViewById(R.id.back_arrow);

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(VsPlayer.this, Row.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        board = new char[ROWS][COLS];
        currentPlayer = 'R';
        player1Turn = findViewById(R.id.player1_turn);
        player2Turn = findViewById(R.id.player2_turn);
        gameBoard = findViewById(R.id.game_board);
        gameOver = false;

        player1Turn.setVisibility(View.VISIBLE);
        player2Turn.setVisibility(View.INVISIBLE);


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
                circle.setOnClickListener(v -> dropPiece(column));
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

                if (checkWin()) {
                    showWinner();
                } else if (isBoardFull()) {
                    showDraw();
                } else {
                    switchPlayer();
                }
                return;
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R';

        player1Turn.setVisibility(currentPlayer == 'R' ? View.VISIBLE : View.INVISIBLE);
        player2Turn.setVisibility(currentPlayer == 'Y' ? View.VISIBLE : View.INVISIBLE);

    }

    private boolean checkWin() {
        return checkHorizontal() || checkVertical() || checkDiagonal();
    }

    private boolean checkHorizontal() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == currentPlayer && board[r][c + 1] == currentPlayer &&
                        board[r][c + 2] == currentPlayer && board[r][c + 3] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVertical() {
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS - 3; r++) {
                if (board[r][c] == currentPlayer && board[r + 1][c] == currentPlayer &&
                        board[r + 2][c] == currentPlayer && board[r + 3][c] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal() {
        for (int r = 3; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == currentPlayer && board[r - 1][c + 1] == currentPlayer &&
                        board[r - 2][c + 2] == currentPlayer && board[r - 3][c + 3] == currentPlayer) {
                    return true;
                }
            }
        }
        for (int r = 0; r < ROWS - 3; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == currentPlayer && board[r + 1][c + 1] == currentPlayer &&
                        board[r + 2][c + 2] == currentPlayer && board[r + 3][c + 3] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinner() {
        gameOver = true;
        String winnerMessage = "Player " + (currentPlayer == 'R' ? "1 (Red)" : "2 (Yellow)") + " Wins!";

        lastWinner = currentPlayer;

        player1Turn.setVisibility(View.INVISIBLE);
        player2Turn.setVisibility(View.INVISIBLE);

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

    private void showDraw() {
        gameOver = true;
        String drawMessage = "It's a draw!";

        player1Turn.setVisibility(View.INVISIBLE);
        player2Turn.setVisibility(View.INVISIBLE);


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

    private void resetGame() {
        gameOver = false;
        initializeBoard();

        if (lastWinner == 'R') {
            currentPlayer = 'Y';
        } else {
            currentPlayer = 'R';
        }

        player1Turn.setVisibility(currentPlayer == 'R' ? View.VISIBLE : View.INVISIBLE);
        player2Turn.setVisibility(currentPlayer == 'Y' ? View.VISIBLE : View.INVISIBLE);

    }
}

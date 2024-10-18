package com.example.minigamecollection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class NormalRow extends AppCompatActivity {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] board;
    private char currentPlayer;
    private boolean isPlayerTurn;
    private boolean gameOver;
    private TextView player1Turn;
    private TextView normalBotTurn;
    private GridLayout gameBoard;
    private Random random;
    private char lastWinner = 'R';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_row);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(NormalRow.this, Row.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        board = new char[ROWS][COLS];
        currentPlayer = 'R';
        player1Turn = findViewById(R.id.player1_turn);
        normalBotTurn = findViewById(R.id.player2_turn);
        gameBoard = findViewById(R.id.game_board);
        gameOver = false;
        random = new Random();

        isPlayerTurn = true;
        player1Turn.setVisibility(View.VISIBLE);
        normalBotTurn.setVisibility(View.INVISIBLE);

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

    @SuppressLint("SetTextI18n")
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R';

        player1Turn.setVisibility(currentPlayer == 'R' ? View.VISIBLE : View.INVISIBLE);
        normalBotTurn.setVisibility(currentPlayer == 'Y' ? View.VISIBLE : View.INVISIBLE);


        if (currentPlayer == 'Y') {
            isPlayerTurn = false;
            normalBotTurn.setText("Normal Bot's (Yellow) Turn");
            normalBotTurn.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(this::makeNormalBotMove, 1000);
        } else {
            isPlayerTurn = true;
            normalBotTurn.setVisibility(View.INVISIBLE);
        }
    }

    private void makeNormalBotMove() {
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
        normalBotTurn.setVisibility(View.INVISIBLE);

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
        String winnerMessage = currentPlayer == 'R' ? "You win!" : "Normal Bot wins!";
        player1Turn.setVisibility(View.INVISIBLE);
        normalBotTurn.setVisibility(View.INVISIBLE);

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
            normalBotTurn.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(this::makeNormalBotMove, 1000);
        } else {
            currentPlayer = 'R';
            isPlayerTurn = true;
            player1Turn.setVisibility(View.VISIBLE);
            normalBotTurn.setVisibility(View.INVISIBLE);
        }
    }
}

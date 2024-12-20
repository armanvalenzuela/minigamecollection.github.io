package com.example.connect4;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Playerxbot_easymode extends AppCompatActivity {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private char[][] board;
    private char currentPlayer;
    private boolean isPlayerTurn;
    private boolean gameOver;
    private TextView player1Turn;
    private TextView easyBotTurn;
    private GridLayout gameBoard;
    private Random random;
    private View player1Box;
    private View easyBotBox;
    private char lastWinner = 'R';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerxbot_easymode);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(Playerxbot_easymode.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        board = new char[ROWS][COLS];
        currentPlayer = 'R';
        player1Turn = findViewById(R.id.player1_turn);
        easyBotTurn = findViewById(R.id.player2_turn);
        gameBoard = findViewById(R.id.game_board);
        gameOver = false;
        random = new Random();
        player1Box = findViewById(R.id.player1_box);
        easyBotBox = findViewById(R.id.player2_box);

        isPlayerTurn = true;
        player1Turn.setVisibility(View.VISIBLE);
        easyBotTurn.setVisibility(View.INVISIBLE);
        player1Box.setVisibility(View.VISIBLE);
        easyBotBox.setVisibility(View.INVISIBLE);

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
        easyBotTurn.setVisibility(currentPlayer == 'Y' ? View.VISIBLE : View.INVISIBLE);

        player1Box.setVisibility(currentPlayer == 'R' ? View.VISIBLE : View.INVISIBLE);
        easyBotBox.setVisibility(currentPlayer == 'Y' ? View.VISIBLE : View.INVISIBLE);

        if (currentPlayer == 'Y') {
            isPlayerTurn = false;
            easyBotTurn.setText("Easy Bot's (Yellow) Turn");
            easyBotTurn.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(this::makeEasyBotMove, 1000);
        } else {
            isPlayerTurn = true;
            easyBotTurn.setVisibility(View.INVISIBLE);
        }
    }

    private void makeEasyBotMove() {
        int col = randomMove();
        dropPiece(col);
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

    private void showDraw() {
        gameOver = true;
        String drawMessage = "It's a draw!";
        player1Turn.setVisibility(View.INVISIBLE);
        easyBotTurn.setVisibility(View.INVISIBLE);
        player1Box.setVisibility(View.INVISIBLE);
        easyBotBox.setVisibility(View.INVISIBLE);

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
        String winnerMessage = currentPlayer == 'R' ? "You win!" : "Easy Bot wins!";
        player1Turn.setVisibility(View.INVISIBLE);
        easyBotTurn.setVisibility(View.INVISIBLE);
        player1Box.setVisibility(View.INVISIBLE);
        easyBotBox.setVisibility(View.INVISIBLE);

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
            easyBotTurn.setVisibility(View.VISIBLE);
            player1Box.setVisibility(View.INVISIBLE);
            easyBotBox.setVisibility(View.VISIBLE);
            new android.os.Handler().postDelayed(this::makeEasyBotMove, 1000);
        } else {
            currentPlayer = 'R';
            isPlayerTurn = true;
            player1Turn.setVisibility(View.VISIBLE);
            easyBotTurn.setVisibility(View.INVISIBLE);
            player1Box.setVisibility(View.VISIBLE);
            easyBotBox.setVisibility(View.INVISIBLE);
        }
    }
}
package com.example.minigamecollection;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {
    private int [][] gameBoard;

    private String[] playerNames = {"Player 1", "Player 2"};

    //1st element --> row, 2nd element --> col, 3rd element --> line type
    private  int [] winType = {-1, -1, -1};

    private Button playAgain;
    private Button home;
    private TextView playerTurn;

    private int player = 1;

    GameLogic(){
        gameBoard = new int[5][5];
        for (int r = 0; r < 5; r++){
            for(int c = 0; c < 5; c++){
                gameBoard[r][c] = 0;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public boolean updateGameBoard (int row, int col){
        if (gameBoard[row -1][col - 1] == 0){
            gameBoard[row -1][col - 1] = player;

            if (player == 1){
                playerTurn.setText((playerNames[1] + " 's Turn"));
            }
            else {
                playerTurn.setText((playerNames[0] + " 's Turn"));
            }


            return true;
        }
        else{
            return false;
        }
    }

    @SuppressLint("SetTextI18n")
    public boolean winnerCheck(){
        boolean isWinner = false;

        //Horizontal
        for (int r = 0; r < 5; r++){
            if (gameBoard[r][0] == gameBoard[r][3] && gameBoard[r][0] == gameBoard[r][4] &&
                    gameBoard[r][0] != 0){
                winType = new int[] {r, 0, 1};
                isWinner = true;
            }
        }

        //Vertical
        for (int c = 0; c < 5; c++){
            if (gameBoard[0][c] == gameBoard[3][c] && gameBoard[4][c] == gameBoard[0][c] &&
                    gameBoard[0][c] != 0){
                winType = new int[] {0, c, 2};
                isWinner = true;
            }
        }

        //Negative Diagonal
        if (gameBoard[0][0] == gameBoard[3][3] && gameBoard[0][0] == gameBoard[4][4] &&
                gameBoard[0][0] != 0){
            winType = new int[] {0, 2, 3};
            isWinner = true;
        }

        //Positive Diagonal
        if (gameBoard[2][0] == gameBoard[1][1] && gameBoard[2][0] == gameBoard[0][2] &&
                gameBoard[2][0] != 0){
            winType = new int[] {2, 2, 4};
            isWinner = true;
        }

        int boardFilled = 0;

        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++){
                if (gameBoard[r][c] != 0){
                    boardFilled += 1;
                }
            }
        }


        if (isWinner){
            playAgain.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            playerTurn.setText((playerNames[player-1] + " Won!"));
            return true;
        }
        else if (boardFilled == 25){
            playAgain.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            playerTurn.setText("It's a Draw!");
            return  true;
        }
        else {
            return false;
        }
    }

    @SuppressLint("SetTextI18n")
    public void resetGame(){
        for (int r = 0; r < 5; r++){
            for(int c = 0; c < 5; c++){
                gameBoard[r][c] = 0;
            }
        }

        player = 1;

        playAgain.setVisibility(View.GONE);
        home.setVisibility(View.GONE);

        playerTurn.setText(playerNames[0] + " 's Turn");
    }

    public void setPlayAgain(Button playAgain) {
        this.playAgain = playAgain;
    }

    public void setHome(Button home) {
        this.home = home;
    }

    public void setPlayerTurn(TextView playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public int[] getWinType() {
        return winType;
    }
}

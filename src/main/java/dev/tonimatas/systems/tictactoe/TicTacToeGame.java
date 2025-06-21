package dev.tonimatas.systems.tictactoe;

public class TicTacToeGame {
    private final String userId;
    private final boolean vsBot;
    private final TicTacToeBoard board;
    private char currentTurn;

    public TicTacToeGame(String userId, boolean vsBot) {
        this.userId = userId;
        this.vsBot = vsBot;
        this.board = new TicTacToeBoard();
        this.currentTurn = 'X';
    }

    public boolean makeMove(int row, int col, char player) {
        if (board.makeMove(row, col, player)) {
            currentTurn = (currentTurn == 'X') ? 'O' : 'X';
            return true;
        }
        return false;
    }

    public boolean isOver() {
        return board.isFull() || board.checkWinner() != ' ';
    }

    public String getUserId() {
        return userId;
    }

    public boolean isVsBot() {
        return vsBot;
    }

    public TicTacToeBoard getBoard() {
        return board;
    }

    public char getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(char currentTurn) {
        this.currentTurn = currentTurn;
    }
}

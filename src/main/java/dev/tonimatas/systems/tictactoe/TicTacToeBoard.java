package dev.tonimatas.systems.tictactoe;

import java.util.Arrays;

public class TicTacToeBoard {
    /**
     * An array simulating a 3x3 board
     */
    private final char[][] board = new char[3][3];

    /**
     * Main constructor
     */
    public TicTacToeBoard() {
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
    }

    /**
     * It makes a move to a position according to the available cells
     * @param row according to the X axis (0-2)
     * @param col according to the Y axis (0-2)
     * @param symbol according to the players (X/O)
     * @return if the move is valid or not
     */
    public boolean makeMove(int row, int col, char symbol) {
        if (board[row][col] == ' ') {
            board[row][col] = symbol;
            return true;
        }
        return false;
    }

    /**
     * @return if the board is full or not
     */
    public boolean isFull() {
        for (char[] row : board) {
            for ( char c : row) {
                if (c == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * It checks all the possibilities to search if there is a winner
     * @return if there is a winner it returns the winner's symbol
     */
    public char checkWinner() {
        for (char[] row : board) {
            if (row[0] != ' ' && row[0] == row[1] && row[1] == row[2]) {
                return row[0];
            }
        }

        for (int col = 0; col < 3; col++) {
            char first = board[0][col];
            if (first != ' ' && first == board[1][col] && first == board[2][col]) {
                return first;
            }
        }

        char center = board [1][1];
        if (center != ' ') {
            if (board[0][0] == center && board[2][2] == center) {
                return center;
            }
            if (board[0][2] == center && board[2][0] == center) {
                return center;
            }
        }

        return ' ';
    }

    /**
     * How the board is going to be shown to the user
     * @return the string containing the board
     */
    public String render() {
        StringBuilder sb = new StringBuilder("```\n");
        for (char[] row : board) {
            sb.append("| --- | --- | --- |\n");
            sb.append("|");
            for (char c : row) {
                sb.append(" ").append(c == ' ' ? "_" : c).append("  |");
            }
            sb.append("\n");
        }
        return sb.append("| --- | --- | --- |\n").append("```").toString();
    }
}

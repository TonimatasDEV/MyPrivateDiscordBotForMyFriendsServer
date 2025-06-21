package dev.tonimatas.systems.tictactoe;

import java.util.Arrays;

public class TicTacToeBoard {
    private final char[][] board = new char[3][3];

    public TicTacToeBoard() {
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
    }

    public boolean makeMove(int row, int col, char symbol) {
        if (board[row][col] == ' ') {
            board[row][col] = symbol;
            return true;
        }
        return false;
    }

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

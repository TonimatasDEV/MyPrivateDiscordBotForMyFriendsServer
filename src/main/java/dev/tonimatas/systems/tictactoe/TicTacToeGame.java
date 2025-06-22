package dev.tonimatas.systems.tictactoe;

public class TicTacToeGame implements Runnable {
    private final Player playerX;
    private final Player playerO;
    private final boolean vsBot;

    private final TicTacToeBoard board = new TicTacToeBoard();
    private Player turn;
    private volatile boolean over = false;
    private volatile String resultMessage = "";

    public TicTacToeGame(Player playerX, Player playerO, boolean vsBot) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.vsBot = vsBot;
        this.turn = playerX;
    }

    public synchronized boolean makeMove(int row, int col, String userId) {
        if (over || !turn.getMember().getId().equals(userId)) return false;
        if (board.makeMove(row, col, turn.getSymbol())) {
            checkMatchAfterTurn();
            if (!over) advanceTurn();
            return true;
        }
        return false;
    }

    private void advanceTurn() {
        turn = (turn == playerX) ? playerO : playerX;
    }

    private void checkMatchAfterTurn() {
        char winner = board.checkWinner();
        if (winner != ' ') {
            over = true;
            resultMessage = (winner == playerX.getSymbol() ? playerX.getMention() : playerO.getMention()) + " wins.";
            return;
        }
        if (board.isFull()) {
            over = true;
            resultMessage = "Draw.";
        }
    }

    public Player getTurn() {
        return turn;
    }

    public boolean isOver() {
        return over;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public boolean isVsBot() {
        return vsBot;
    }

    public TicTacToeBoard getBoard() {
        return board;
    }

    @Override
    public void run() {
        //TODO: Implement bot AI :)
    }
}



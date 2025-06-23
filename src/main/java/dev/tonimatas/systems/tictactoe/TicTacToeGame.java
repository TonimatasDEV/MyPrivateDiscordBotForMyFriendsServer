package dev.tonimatas.systems.tictactoe;

/**
 * Game itself
 */
public class TicTacToeGame implements Runnable {
    /**
     * Player that creates the match and starts always
     */
    private final Player playerX;
    /**
     * Player 2
     */
    private final Player playerO;
    /**
     * Whether it uses the AI against the player or not
     */
    private final boolean vsBot;

    /**
     * Generate a new board for the game
     */
    private final TicTacToeBoard board = new TicTacToeBoard();
    /**
     * A cursor which it can be playerX/playerO to determine who's going to select a coordinate
     */
    private Player turn;
    /**
     * Determines when the match finish
     */
    private volatile boolean over = false;
    /**
     * If the match finish it will send this message
     */
    private volatile String resultMessage = "";

    /**
     * Main constructor
     * @param playerX Player 1
     * @param playerO Player 2
     * @param vsBot If you want to play against an AI
     */
    public TicTacToeGame(Player playerX, Player playerO, boolean vsBot) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.vsBot = vsBot;
        this.turn = playerX;
    }

    /**
     * Constructor to PVP
     * @param playerX Player 1
     * @param playerO Player 2
     */
    public TicTacToeGame(Player playerX, Player playerO) {
        this(playerX, playerO, false);
    }

    /**
     * Constructor to play against an AI
     * @param playerX Player 1
     */
    public TicTacToeGame(Player playerX) {
        this(playerX, null, true);
    }

    /**
     * @param row This stands for X axis (0-2)
     * @param col This stands for Y axis (0-2)
     * @param userId User identifier
     * @return If the move is valid or not
     */
    public synchronized boolean makeMove(int row, int col, String userId) {
        if (over || !turn.getMember().getId().equals(userId)) return false;
        if (board.makeMove(row, col, turn.getSymbol())) {
            checkMatchAfterTurn();
            if (!over) advanceTurn();
            return true;
        }
        return false;
    }

    /**
     * It chooses who's next turn
     */
    private void advanceTurn() {
        turn = (turn == playerX) ? playerO : playerX;
    }

    /**
     * It checks the winner after a turn is over, if the match is not over it will continue to the next turn
     */
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



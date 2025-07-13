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

        boolean moved = board.makeMove(row, col, turn.getSymbol());
        if (!moved) return false;

        checkMatchAfterTurn();
        if (!over) advanceTurn();
        return true;
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

    public Player getCurrentPlayer() {
        return turn;
    }

    public void switchTurn() {
        advanceTurn();
    }

    /**
     * Bot performs a move, prioritizing winning, then blocking, then first available.
     * @return An array with {row, col} of the move made, or null if no move possible.
     */
    public int[] botMakeMove() {
        if (over) return null;

        char botSymbol = turn.getSymbol();
        char playerSymbol = (botSymbol == 'X') ? 'O' : 'X';

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isEmpty(row, col)) {
                    board.makeMove(row, col, botSymbol);
                    if (board.checkWinner() == botSymbol) {
                        checkMatchAfterTurn();
                        if (!over) advanceTurn();
                        return new int[]{row, col};
                    }
                    board.undoMove(row, col);
                }
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.isEmpty(row, col)) {
                    board.makeMove(row, col, playerSymbol);
                    if (board.checkWinner() == playerSymbol) {
                        board.undoMove(row, col);
                        board.makeMove(row, col, botSymbol);
                        checkMatchAfterTurn();
                        if (!over) advanceTurn();
                        return new int[]{row, col};
                    }
                    board.undoMove(row, col);
                }
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.makeMove(row, col, botSymbol)) {
                    checkMatchAfterTurn();
                    if (!over) advanceTurn();
                    return new int[]{row, col};
                }
            }
        }

        return null;
    }

    public Player getOpponent(Player player) {
        return (player == playerX) ? playerO : playerX;
    }

    public boolean isBotTurn() {
        return vsBot && turn == playerO && !over;
    }

    @Override
    public void run() {

    }
}



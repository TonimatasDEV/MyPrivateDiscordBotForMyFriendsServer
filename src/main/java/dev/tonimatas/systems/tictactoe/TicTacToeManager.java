package dev.tonimatas.systems.tictactoe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TicTacToeManager {
    /**
     * Game storage
     */
    private final Map<String, TicTacToeGame> activePlayers = new ConcurrentHashMap<>();

    /**
     *
     * @param userId user main identification
     * @return If the match is still live, or it ended quite time ago
     */
    public boolean isPlaying(String userId) {
        return activePlayers.containsKey(userId);
    }

    public TicTacToeGame getGame(String userId) {
        return activePlayers.get(userId);
    }

    /**
     * Remove a game from the storage
     * @param userId user main identification
     */
    public void endGame(String userId) {
        activePlayers.remove(userId);
    }

    /**
     * Creates a new game and starts it
     * @param playerX player 1
     * @param playerO player 2
     * @param vsBot boolean
     * @return if the parameters are correct or if something went wrong
     */
    private TicTacToeGame createGame(Player playerX, Player playerO, boolean vsBot) {
        return (playerO == null) ? new TicTacToeGame(playerX) : new TicTacToeGame(playerX, playerO);
    }

    public void startGame(String userId, TicTacToeGame game) {
        activePlayers.put(userId, game);
    }
}

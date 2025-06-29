package dev.tonimatas.systems.tictactoe;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeManager {
    /**
     * Game storage
     */
    private final Map<String, TicTacToeGame> games = new HashMap<>();

    /**
     *
     * @param userId user main identification
     * @return If the match is still live, or it ended quite time ago
     */
    public boolean isPlaying(String userId) {
        return games.containsKey(userId);
    }

    public TicTacToeGame getGame(String userId) {
        return games.get(userId);
    }

    /**
     * Remove a game from the storage
     * @param userId user main identification
     */
    public void endGame(String userId) {
        games.remove(userId);
    }

    /**
     * Creates a new game and starts it
     * @param userId user main identification
     * @param playerX player 1
     * @param playerO player 2
     * @param vsBot boolean
     * @return if the parameters are correct or if something went wrong
     */
    public boolean startGame(String userId, Player playerX, Player playerO, boolean vsBot) {
    if (isPlaying(userId)) return false;
    if (!vsBot && isPlaying(playerO.getMember().getId())) return false;

    TicTacToeGame game = null;

    if (playerO == null) {
        game = new TicTacToeGame(playerX);
    } else {
        game = new TicTacToeGame(playerX, playerO);
    }

    games.put(userId, game);

    if (!vsBot) {
        games.put(playerO.getMember().getId(), game);
    }
    return true;
    }
}

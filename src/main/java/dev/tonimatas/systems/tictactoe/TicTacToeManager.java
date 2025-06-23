package dev.tonimatas.systems.tictactoe;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeManager {
    private final Map<String, TicTacToeGame> games = new HashMap<>();

    public boolean isPlaying(String userId) {
        return games.containsKey(userId);
    }

    public TicTacToeGame getGame(String userId) {
        return games.get(userId);
    }

    public void endGame(String userId) {
        games.remove(userId);
    }

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

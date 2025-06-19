package dev.tonimatas.systems.hangman;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.executors.ExecutorManager;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class HangmanManager {
    private final Map<String, HangmanGame> activeGames = new HashMap<>();

    public boolean isPlaying(String userId) {
        return activeGames.containsKey(userId);
    }

    public HangmanGame getGame(String userId) {
        return activeGames.get(userId);
    }

    public void endGame(String userId) {
        activeGames.remove(userId);
    }

    public boolean startGame(User user, String language) {
        String userId = user.getId();

        if (isPlaying(userId)) return false;
        if (BotFiles.BANK.getMoney(userId) > HangmanGame.COST) return false;

        HangmanGame game = new HangmanGame(user, language);
        activeGames.put(userId, game);

        ExecutorManager.submit(new HangmanGame(user, language));
        return true;
    }


}

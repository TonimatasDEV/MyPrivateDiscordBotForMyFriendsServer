package dev.tonimatas.systems.hangman;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HangmanGame {
    private static final int COST = 50;
    private static final int REWARD = 75;
    private static final Duration TIME_LIMIT = Duration.ofMinutes(3);
    private static final int MAX_ATTEMPTS = 6;

    private final User user;
    private final String language;
    private final String wordToGuess;
    private final Set<Character> guessedLetters = new HashSet<>();
    private int incorrectAttempts = 0;
    private final Instant startTime;

    public HangmanGame(User user, String language) {
        this.user = user;
        this.language = language;
        this.wordToGuess = pickRandomWord(language);
        this.startTime = Instant.now();
    }

    private String pickRandomWord(String language) {
        List<String> words = BotFiles.WORDS.getWords(language);

        if (words == null || words.isEmpty()) {
            // No se han encontrado palabras para ese lenguaje
        }

        SecureRandom random = new SecureRandom();
        return words.get(random.nextInt(words.size())).toLowerCase();
    }

    public boolean guess(char c) {
        c = Character.toLowerCase(c);
        if (guessedLetters.contains(c)) {
            return false;
        }

        guessedLetters.add(c);

        if (wordToGuess.indexOf(c) == -1) {
            incorrectAttempts++;
            return false;
        }

        return true;
    }

    public String getDisplayedWord() {
        StringBuilder display = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            if (guessedLetters.contains(c)) {
                display.append(c).append(' ');
            } else {
                display.append("_ ");
            }
        }
        return display.toString().trim();
    }

    public boolean isWon() {
        for (char c : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(c)) return false;
        }
        return true;
    }

    public boolean isLost() {
        return incorrectAttempts >= MAX_ATTEMPTS || isTimeOver();
    }

    public boolean isTimeOver() {
        return Instant.now().isAfter(startTime.plus(TIME_LIMIT));
    }

    public Duration getRemainingTime() {
        Duration elapsed = Duration.between(startTime, Instant.now());
        return TIME_LIMIT.minus(elapsed).isNegative() ? Duration.ZERO : TIME_LIMIT.minus(elapsed);
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public User getUser() {
        return user;
    }

    public void rewardIfWon() {
        if (isWon()) {
            BotFiles.BANK.addMoney(user.getId(), REWARD, "Won the HangMan MiniGame.");
        }
    }

    public void chargeEntry(SlashCommandInteractionEvent event) {
        if (BotFiles.BANK.getMoney(user.getId()) < COST) {
            event.reply("You don't have enough money to play HangMan.").setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        BotFiles.BANK.removeMoney(user.getId(), COST, "Charge for entrying HangMan MiniGame.");
    }
}
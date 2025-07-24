package dev.tonimatas.systems.tictactoe;

import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Player {
    /**
     * Member linked to this player
     */
    private final String userId;
    /**
     * Could be X as player 1 or O as player 2
     */
    private final char symbol;

    /**
     * Main constructor
     * @param userId Member linked to this player
     * @param symbol Could be X as player 1 or O as player 2
     */
    public Player(String userId, char symbol) {
        this.userId = userId;
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player other = (Player) o;
        return symbol == other.symbol && userId.equals(((Player) o).userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, symbol);
    }

    @Override
    public String toString() {
        return "Player{" +
                "member=" + userId +
                ", symbol=" + symbol +
                '}';
    }
}

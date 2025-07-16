package dev.tonimatas.systems.tictactoe;

import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Player {
    /**
     * Member linked to this player
     */
    private final Member member;
    /**
     * Could be X as player 1 or O as player 2
     */
    private final char symbol;

    /**
     * Main constructor
     * @param member Member linked to this player
     * @param symbol Could be X as player 1 or O as player 2
     */
    public Player(Member member, char symbol) {
        this.member = member;
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public Member getMember() {
        return member;
    }

    public String getMention() {
        return member.getAsMention();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player other = (Player) o;
        return symbol == other.symbol && Objects.equals(member, other.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, symbol);
    }

    @Override
    public String toString() {
        return "Player{" +
                "member=" + member.getId() +
                ", symbol=" + symbol +
                '}';
    }
}

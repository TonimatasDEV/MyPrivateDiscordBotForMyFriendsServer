package dev.tonimatas.systems.tictactoe;

import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Player {
    private final Member member;
    private final char symbol;

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
}

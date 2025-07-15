package dev.tonimatas.systems.roulette.bets;

public class NumberBet extends Bet {
    private int number;

    public NumberBet(String id, String number, long money) {
        super(id, money);

        try {
            this.number = Integer.parseInt(number);
        } catch (Exception i) {
            this.number = -1;
        }
    }

    @Override
    public String getTypePart() {
        return "number " + number;
    }

    @Override
    int getMultiplier() {
        return 36;
    }

    @Override
    boolean isWinner(int winner) {
        return winner == number;
    }

    @Override
    public boolean isValid() {
        return number <= 36 && number >= 0;
    }

    @Override
    public boolean canMerge(Bet bet) {
        return bet instanceof NumberBet numberBet && numberBet.number == number;
    }
}

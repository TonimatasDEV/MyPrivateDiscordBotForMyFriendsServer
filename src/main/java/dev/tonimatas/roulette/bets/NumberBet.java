package dev.tonimatas.roulette.bets;

public class NumberBet extends Bet {
    private int input;
    
    public NumberBet(String id, String number, long money) {
        super(id, money);
        
        try {
            this.input = Integer.parseInt(number);
        } catch (Exception i) {
            this.input = -1;
        }
    }

    @Override
    public String getTypePart() {
        return "number " + input;
    }

    @Override
    int getMultiplier() {
        return 36;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return winnerNumber == input;
    }

    @Override
    public boolean isValid() {
        return input <= 36  && input >= 0;
    }
}

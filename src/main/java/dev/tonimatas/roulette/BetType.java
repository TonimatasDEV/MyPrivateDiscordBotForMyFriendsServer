package dev.tonimatas.roulette;

public enum BetType {
    NUMBER,
    COLUMN,
    DOZEN,
    COLOR;
    
    public int getMultiplier(int userValue, int winner) {
        switch (this) {
            case NUMBER -> {
                if (userValue == winner) {
                    return 36;
                }
            }
            
            case COLUMN -> {
                int winnerColumn = RouletteUtils.getColumn(winner);
                
                if (userValue == winnerColumn) {
                    return 3;
                }
            }

            case DOZEN -> {
                int winnerColumn = RouletteUtils.getDozen(winner);

                if (winnerColumn == userValue) {
                    return 3;
                }
            }

            case COLOR -> {
                int winnerColor = RouletteUtils.getColor(winner);

                if (winnerColor == userValue) {
                    if (winnerColor == 0) {
                        return 36;
                    } else {
                        return 2;
                    }
                }
            }
        }
        
        return 0;
    }
    
    public boolean isCorrect(int value) {
        switch (this) {
            case NUMBER -> {
                if (value >= 0 && value < 37) {
                    return true;
                }
            }
            case COLUMN, COLOR, DOZEN -> {
                if (value > 0 && value < 4) {
                    return true;
                }
            }
        }
        
        return false;
    }
}

package dev.tonimatas.roulette;

import dev.tonimatas.schedules.RouletteManager;
import dev.tonimatas.utils.Getters;
import net.dv8tion.jda.api.entities.Member;

public record Bet(String id, long money, BetType type, int value) {
    public boolean isValid() {
        return type.isCorrect(value);
    }

    public void giveReward(int winNumber) {
        int multiplier = type.getMultiplier(value, winNumber);
        long currentMoney = RouletteManager.bankAccounts.getOrDefault(id, 0L);
        long profit = money * multiplier;
        RouletteManager.bankAccounts.put(id, currentMoney + profit);

        Member member = Getters.getGuild().getMemberById(id);
        if (member != null) {
            String text;
            if (multiplier > 0) {
                text = member.getEffectiveName() + " has ganado " + profit + "€ con tu apuesta al " + value + " (" + type + ").";
            } else {
                text = member.getEffectiveName() + " tu apuesta al " + value + " (" + type + ") no ha ganado esta vez.\nHas perdido " + money + ".";
            }
            Getters.getRouletteChannel().sendMessage(text).queue();
        }
    }
}

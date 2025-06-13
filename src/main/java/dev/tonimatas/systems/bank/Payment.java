package dev.tonimatas.systems.bank;

import dev.tonimatas.config.BankData;
import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class Payment implements Comparable<Payment> {
    private final Member sender;
    private final Member receiver;
    private final long amount;
    private final String reason;
    private final double feeRate = 0.05;
    private final LocalDateTime date;

    public Payment(Member sender, Member receiver, long amount, String reason) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.reason = (reason == null || reason.isBlank()) ? "No reason provided." : reason;
        this.date = LocalDateTime.now();
    }

    public Member getSender() {
        return sender;
    }

    public Member getReceiver() {
        return receiver;
    }

    public String getReason() {
        return reason;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isValid() {
        return sender != null && receiver != null && !sender.getId().equals(receiver.getId()) && amount > 0;
    }

    public long getFee() {
        return Math.round(amount * feeRate);
    }

    public long getTotalCost() {
        return amount + getFee();
    }

    public boolean canAfford() {
        return BotFiles.BANK.getMoney(sender.getId()) >= getTotalCost();
    }

    public void execute() {
        String senderId = sender.getId();
        String receiverId = receiver.getId();

        BotFiles.BANK.removeMoney(senderId,getTotalCost());
        BotFiles.BANK.addMoney(receiverId,amount);
    }

    @Override
    public int compareTo(@NotNull Payment o) {
        if (this.date.isAfter(o.date)) return -1;
        if (this.date.isBefore(o.date)) return 1;
        else return 0;
    }
}

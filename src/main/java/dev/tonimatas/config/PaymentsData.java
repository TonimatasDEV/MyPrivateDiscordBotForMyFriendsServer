package dev.tonimatas.config;

import dev.tonimatas.systems.bank.Payment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentsData extends JsonFile {
    private static final int MAX_PAYMENTS = 10;
    private Map<String, ArrayList<Payment>> payments = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/payments.json";
    }

    public void addPayment(Payment payment) {
        String userId = payment.getSender().getId();
        ArrayList<Payment> userPayments = payments.get(userId);

        if (userPayments == null) {
            userPayments = new ArrayList<>();
        }

        if (!userPayments.isEmpty()) {
            userPayments.sort(null);

            int deleteCount = payments.get(userId).size() - MAX_PAYMENTS;

            if (deleteCount > 1) {
                for (int i = 0; i < deleteCount; i++) {
                    userPayments.removeFirst();
                }
            }
        }

        userPayments.add(payment);
        payments.put(userId, userPayments);
        save();
    }

    public @NotNull List<Payment> getPayments(String userId) {
        ArrayList<Payment> userPayments = payments.get(userId);

        if (userPayments == null) {
            userPayments = new ArrayList<>();
        }

        userPayments.sort(null);

        return userPayments;
    }
}

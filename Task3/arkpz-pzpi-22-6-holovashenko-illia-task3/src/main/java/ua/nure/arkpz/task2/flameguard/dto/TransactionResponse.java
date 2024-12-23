package ua.nure.arkpz.task2.flameguard.dto;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Transaction;

public class TransactionResponse {
    private String description;
    private Amount amount;

    public TransactionResponse(Transaction transaction) {
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}

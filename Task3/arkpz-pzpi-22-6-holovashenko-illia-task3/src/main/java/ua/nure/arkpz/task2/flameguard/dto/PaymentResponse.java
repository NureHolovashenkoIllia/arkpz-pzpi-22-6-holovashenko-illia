package ua.nure.arkpz.task2.flameguard.dto;

import com.paypal.api.payments.Payment;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentResponse {
    private String id;
    private String state;
    private String intent;
    private List<TransactionResponse> transactions;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.state = payment.getState();
        this.intent = payment.getIntent();

        if (payment.getTransactions() != null) {
            this.transactions = payment.getTransactions().stream()
                    .map(TransactionResponse::new)
                    .collect(Collectors.toList());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }
}

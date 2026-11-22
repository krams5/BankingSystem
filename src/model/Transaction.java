package model;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionID;
    private String type;
    private double amount;
    private final LocalDateTime date;

    public int getTransactionID() {
        return transactionID;
    }

    public Transaction(int transactionID, String type, double amount) {
        this.transactionID = transactionID;
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }


    }


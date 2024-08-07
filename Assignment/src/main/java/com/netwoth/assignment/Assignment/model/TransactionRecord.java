package com.netwoth.assignment.Assignment.model;

import java.util.Date;

public class TransactionRecord {
    private Date date;
    private String transactionDetails;
    private String amount;

    public TransactionRecord(Date date, String transactionDetails, String amount) {
        this.date = date;
        this.transactionDetails = transactionDetails;
        this.amount = amount;
    }

    // Getters and setters

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

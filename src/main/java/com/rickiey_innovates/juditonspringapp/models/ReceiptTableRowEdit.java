package com.rickiey_innovates.juditonspringapp.models;

public class ReceiptTableRowEdit {
    int transactionId;
    int activity;
    double amount;

    public ReceiptTableRowEdit(int transactionId, int activity, double amount) {
        this.transactionId = transactionId;
        this.activity = activity;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ReceiptTableRowEdit{" +
               "transactionId=" + transactionId +
               ", activity=" + activity +
               ", amount=" + amount +
               '}';
    }
}

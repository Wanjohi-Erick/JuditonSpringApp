package com.rickiey_innovates.juditonspringapp.models;

public class ReceiptTableRow {
    int activity;
    double amount;

    public ReceiptTableRow(int activity, double amount) {
        this.activity = activity;
        this.amount = amount;
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
        return "ReceiptTableRow{" +
               "activity=" + activity +
               ", amount=" + amount +
               '}';
    }
}

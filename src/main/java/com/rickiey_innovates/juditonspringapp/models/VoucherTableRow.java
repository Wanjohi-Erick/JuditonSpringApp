package com.rickiey_innovates.juditonspringapp.models;

public class VoucherTableRow {
    private String particulars;
    private double quantity;
    private double rate;
    private double amount;

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "VoucherTableRow{" +
                "particulars='" + particulars + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", amount=" + amount +
                '}';
    }
}
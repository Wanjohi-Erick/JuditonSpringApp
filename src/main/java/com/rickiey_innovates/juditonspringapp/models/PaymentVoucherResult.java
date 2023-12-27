package com.rickiey_innovates.juditonspringapp.models;

import java.util.Date;

public class PaymentVoucherResult {
    private int pvId;
    private String voucherNumber, status;
    private Date date;
    private String payeeName;
    private String account;
    private String details;
    private double amount;


    public PaymentVoucherResult() {
    }

    public PaymentVoucherResult(int pvId, String voucherNumber, Date date, String status, String payeeName, String account, String details, double amount) {
        this.pvId = pvId;
        this.voucherNumber = voucherNumber;
        this.date = date;
        this.status = status;
        this.payeeName = payeeName;
        this.account = account;
        this.details = details;
        this.amount = amount;
    }

    public int getPvId() {
        return pvId;
    }

    public void setPvId(int pvId) {
        this.pvId = pvId;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}


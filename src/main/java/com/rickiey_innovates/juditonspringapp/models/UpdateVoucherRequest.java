package com.rickiey_innovates.juditonspringapp.models;

import java.time.LocalDate;
import java.util.List;

// DataDTO.java
public class UpdateVoucherRequest {
    private int pv;
    private LocalDate date;
    private int activity;
    private String payee;
    private String details;
    private int bank;
    private String transRef;
    private double withholding;
    private double vat;
    private double profFees;
    private Double amountBeforeTax;
    private Double totalPayable;
    private List<VoucherTableRow> voucherTableData;


    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public double getWithholding() {
        return withholding;
    }

    public void setWithholding(double withholding) {
        this.withholding = withholding;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getProfFees() {
        return profFees;
    }

    public void setProfFees(double profFees) {
        this.profFees = profFees;
    }

    public Double getAmountBeforeTax() {
        return amountBeforeTax;
    }

    public void setAmountBeforeTax(Double amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    public Double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(Double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public List<VoucherTableRow> getVoucherTableData() {
        return voucherTableData;
    }

    public void setVoucherTableData(List<VoucherTableRow> voucherTableData) {
        this.voucherTableData = voucherTableData;
    }

    @Override
    public String toString() {
        return "UpdateVoucherRequest{" +
                "pv=" + pv +
                ", date=" + date +
                ", activity=" + activity +
                ", payee='" + payee + '\'' +
                ", details='" + details + '\'' +
                ", bank=" + bank +
                ", transRef='" + transRef + '\'' +
                ", withholding=" + withholding +
                ", vat=" + vat +
                ", profFees=" + profFees +
                ", amountBeforeTax=" + amountBeforeTax +
                ", totalPayable=" + totalPayable +
                ", voucherTableData=" + voucherTableData +
                '}';
    }
}


package com.rickiey_innovates.juditonspringapp.models;

import java.util.Date;

public class PaymentVoucherMapper {
    private String pvId;
    private String voucherNumber;
    private Date date;
    private String payeeName;
    private String detailId;
    private String particulars;
    private int quantity;
    private double rate;
    private double total;
    private String account;
    private String details;
    private String activity;
    private double withholdingTax;
    private double vatTax;
    private double professionalFees;

    public PaymentVoucherMapper(String pvId, String voucherNumber, Date date, String payeeName, String detailId, String particulars, int quantity, double rate, String account, String details, String activity, double withholdingTax, double vatTax, double professionalFees) {
        this.pvId = pvId;
        this.voucherNumber = voucherNumber;
        this.date = date;
        this.payeeName = payeeName;
        this.detailId = detailId;
        this.particulars = particulars;
        this.quantity = quantity;
        this.rate = rate;
        this.account = account;
        this.details = details;
        this.activity = activity;
        this.withholdingTax = withholdingTax;
        this.vatTax = vatTax;
        this.professionalFees = professionalFees;
    }

    public PaymentVoucherMapper() {

    }

    public String getPvId() {
        return pvId;
    }

    public void setPvId(String pvId) {
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

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(double withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public double getVatTax() {
        return vatTax;
    }

    public void setVatTax(double vatTax) {
        this.vatTax = vatTax;
    }

    public double getProfessionalFees() {
        return professionalFees;
    }

    public void setProfessionalFees(double professionalFees) {
        this.professionalFees = professionalFees;
    }
}


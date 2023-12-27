package com.walgotech.juditonspringapp.models.inventory;

public class PurchaseBillModel {
    private int billid;
    private String billno;
    private String name;
    private String phone;
    private String item;
    private String amt;
    private String paid;
    private String pending;
    private String billdate;

    private String status;

    // Constructors, getters, and setters

    public PurchaseBillModel(int billid, String billno, String name, String phone, String item, String amt, String paid, String pending, String billdate, String status) {
        this.billid = billid;
        this.billno = billno;
        this.name = name;
        this.phone = phone;
        this.item = item;
        this.amt = amt;
        this.paid = paid;
        this.pending = pending;
        this.billdate = billdate;
        this.status = status;
    }

    public int getBillid() {
        return billid;
    }

    public void setBillid(int billid) {
        this.billid = billid;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

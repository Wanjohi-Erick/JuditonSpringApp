package com.rickiey_innovates.juditonspringapp.models;

import java.time.LocalDate;
import java.util.List;

public class ReceiptRequest {
    private LocalDate date;
    private String receivedFrom;
    private String details;
    private int bank;
    private String transRef;
    private List<ReceiptTableRow> receiptTableRowList;

    public List<ReceiptTableRow> getReceiptTableRowList() {
        return receiptTableRowList;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public void setReceiptTableRowList(List<ReceiptTableRow> receiptTableRowList) {
        this.receiptTableRowList = receiptTableRowList;
    }

    @Override
    public String toString() {
        return "ReceiptRequest{" +
                "date=" + date +
                ", receivedFrom='" + receivedFrom + '\'' +
                ", details='" + details + '\'' +
                ", bank=" + bank +
                ", transRef='" + transRef + '\'' +
                ", receiptTableRowList=" + receiptTableRowList +
                '}';
    }
}

package com.rickiey_innovates.juditonspringapp.models;

import java.time.LocalDate;
import java.util.List;

public class ReceiptRequestUpdate {
    private String referenceNumber;
    private LocalDate date;
    private String receivedFrom;
    private String details;
    private int bank;
    private String transRef;
    private List<ReceiptTableRowEdit> receiptTableRowList;

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public List<ReceiptTableRowEdit> getReceiptTableRowList() {
        return receiptTableRowList;
    }

    public void setReceiptTableRowList(List<ReceiptTableRowEdit> receiptTableRowList) {
        this.receiptTableRowList = receiptTableRowList;
    }

    @Override
    public String toString() {
        return "ReceiptRequestUpdate{" +
               "referenceNumber='" + referenceNumber + '\'' +
               ", date=" + date +
               ", receivedFrom='" + receivedFrom + '\'' +
               ", details='" + details + '\'' +
               ", bank=" + bank +
               ", transRef='" + transRef + '\'' +
               ", receiptTableRowList=" + receiptTableRowList +
               '}';
    }
}

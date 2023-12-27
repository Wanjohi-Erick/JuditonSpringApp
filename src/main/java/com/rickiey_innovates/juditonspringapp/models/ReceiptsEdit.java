package com.rickiey_innovates.juditonspringapp.models;

import java.time.LocalDate;
import java.util.List;

public class ReceiptsEdit {
    private String referenceNumber;

    private int bank;
    private LocalDate date;
    private String receivedFrom;
    private String details;
    private String transRef;

    List<ReceiptTableRowEdit> receiptTableRows;

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
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

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public List<ReceiptTableRowEdit> getReceiptTableRows() {
        return receiptTableRows;
    }

    public void setReceiptTableRows(List<ReceiptTableRowEdit> receiptTableRows) {
        this.receiptTableRows = receiptTableRows;
    }
}

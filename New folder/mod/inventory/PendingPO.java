package com.walgotech.juditonspringapp.models.inventory;

public class PendingPO {
    int poItemsId, poId;
    String company, expectedDate, itemName, quantity, quantityReceived, outstanding;
    public PendingPO(int poItemsId, int poId, String company, String expectedDate, String itemName, String quantity, String quantityReceived, String outstanding) {
        this.poItemsId = poItemsId;
        this.poId = poId;
        this.company = company;
        this.expectedDate = expectedDate;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityReceived = quantityReceived;
        this.outstanding = outstanding;
    }

    public int getPoItemsId() {
        return poItemsId;
    }

    public void setPoItemsId(int poItemsId) {
        this.poItemsId = poItemsId;
    }

    public int getPoId() {
        return poId;
    }

    public void setPoId(int poId) {
        this.poId = poId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(String quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }
}

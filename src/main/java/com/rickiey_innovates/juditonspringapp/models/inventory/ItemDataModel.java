package com.rickiey_innovates.juditonspringapp.models.inventory;

import java.text.DecimalFormat;

public class ItemDataModel {
    private int id, itemId;
    private String image;
    private String itemName;
    private String price;
    private String quantity;
    private String uom;
    private String total;
    private String details;

    public ItemDataModel(int requisition_id, String image, int itemId, String itemName, double price, String quantity, String uom, double total, String details) {
        this.id = requisition_id;
        this.image = image;
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = new DecimalFormat("#,###.##").format(price);
        this.quantity = quantity;
        this.uom = uom;
        this.total = new DecimalFormat("#,###.##").format(total);
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = new DecimalFormat("#,###.##").format(price);
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = new DecimalFormat("#,###.##").format(total);
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}


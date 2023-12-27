package com.walgotech.juditonspringapp.models.inventory;

public class ItemsModel {
    int itemId, reorderLevel;
    String image, units, itemGroup, itemName, itemsStatus;
    public ItemsModel(int itemId, String itemName, String itemGroup, int reorderLevel, String image, String units, String itemsStatus) {
        this.itemGroup = itemGroup;
        this.itemId = itemId;
        this.reorderLevel = reorderLevel;
        this.units = units;
        this.image = image;
        this.itemName = itemName;
        this.itemsStatus = itemsStatus;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemsStatus() {
        return itemsStatus;
    }

    public void setItemsStatus(String itemsStatus) {
        this.itemsStatus = itemsStatus;
    }
}

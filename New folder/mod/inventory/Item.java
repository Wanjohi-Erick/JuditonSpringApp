package com.walgotech.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity(name = "Item")
@Table(name = "items", schema = "church", indexes = {
        @Index(name = "FK_items_item_group", columnList = "item_group")
})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Size(max = 100)
    @NotNull
    @Column(name = "uom", nullable = false, length = 100)
    private String uom;

    @Size(max = 50)
    @Column(name = "item_group", length = 50)
    private String itemGroup;

    @Size(max = 50)
    @NotNull
    @Column(name = "item_category", nullable = false, length = 50)
    private String itemCategory;

    @NotNull
    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    @NotNull
    @Column(name = "reoder_level", nullable = false)
    private Integer reoderLevel;

    @Column(name = "preferred_vendor")
    private Integer preferredVendor;

    @Size(max = 500)
    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "returnable")
    private Integer returnable;

    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "church")
    private Integer church;

    public Integer getchurch() {
        return church;
    }

    public void setchurch(Integer church) {
        this.church = church;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getReoderLevel() {
        return reoderLevel;
    }

    public void setReoderLevel(Integer reoderLevel) {
        this.reoderLevel = reoderLevel;
    }

    public Integer getPreferredVendor() {
        return preferredVendor;
    }

    public void setPreferredVendor(Integer preferredVendor) {
        this.preferredVendor = preferredVendor;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getReturnable() {
        return returnable;
    }

    public void setReturnable(Integer returnable) {
        this.returnable = returnable;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

}
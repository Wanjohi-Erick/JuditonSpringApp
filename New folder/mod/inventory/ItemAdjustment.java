package com.walgotech.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "ItemAdjustment")
@Table(name = "item_adjustment", schema = "church", indexes = {
        @Index(name = "FK_item_adjustment_items", columnList = "item_id")
})
public class ItemAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Item item;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Size(max = 50)
    @NotNull
    @Column(name = "adjustment_type", nullable = false, length = 50)
    private String adjustmentType;

    @NotNull
    @Column(name = "initial_value", nullable = false)
    private Double initialValue;

    @NotNull
    @Column(name = "adjusted_value", nullable = false)
    private Double adjustedValue;

    @Size(max = 500)
    @NotNull
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Size(max = 100)
    @NotNull
    @Column(name = "account", nullable = false, length = 100)
    private String account;

    @Size(max = 500)
    @NotNull
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Size(max = 100)
    @NotNull
    @Column(name = "adjusted_by", nullable = false, length = 100)
    private String adjustedBy;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "church", nullable = false)
    private Integer church;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public Double getAdjustedValue() {
        return adjustedValue;
    }

    public void setAdjustedValue(Double adjustedValue) {
        this.adjustedValue = adjustedValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdjustedBy() {
        return adjustedBy;
    }

    public void setAdjustedBy(String adjustedBy) {
        this.adjustedBy = adjustedBy;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getchurch() {
        return church;
    }

    public void setchurch(Integer church) {
        this.church = church;
    }

}
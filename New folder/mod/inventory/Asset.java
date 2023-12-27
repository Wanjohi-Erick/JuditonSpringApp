package com.walgotech.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity(name = "Asset")
@Table(name = "assets", schema = "church", indexes = {
        @Index(name = "serial", columnList = "serial", unique = true)
})
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 200)
    @NotNull
    @Column(name = "serial", nullable = false, length = 200)
    private String serial;

    @Size(max = 200)
    @NotNull
    @Column(name = "type", nullable = false, length = 200)
    private String type;

    @Size(max = 200)
    @NotNull
    @Column(name = "status", nullable = false, length = 200)
    private String status;

    @Size(max = 200)
    @NotNull
    @Column(name = "depreciation", nullable = false, length = 200)
    private String depreciation;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Double rate;

    @Size(max = 200)
    @NotNull
    @Column(name = "period", nullable = false, length = 200)
    private String period;

    @Size(max = 200)
    @NotNull
    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Size(max = 200)
    @NotNull
    @Column(name = "used_by", nullable = false, length = 200)
    private String usedBy;

    @Size(max = 200)
    @NotNull
    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @NotNull
    @Column(name = "opening_balance", nullable = false)
    private Double openingBalance;

    @Size(max = 50)
    @Column(name = "asset_life", length = 50)
    private String assetLife;

    @Column(name = "balance_as_at")
    private LocalDate balanceAsAt;

    @NotNull
    @Column(name = "church", nullable = false)
    private Integer church;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(String depreciation) {
        this.depreciation = depreciation;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getAssetLife() {
        return assetLife;
    }

    public void setAssetLife(String assetLife) {
        this.assetLife = assetLife;
    }

    public LocalDate getBalanceAsAt() {
        return balanceAsAt;
    }

    public void setBalanceAsAt(LocalDate balanceAsAt) {
        this.balanceAsAt = balanceAsAt;
    }

    public Integer getchurch() {
        return church;
    }

    public void setchurch(Integer church) {
        this.church = church;
    }

}
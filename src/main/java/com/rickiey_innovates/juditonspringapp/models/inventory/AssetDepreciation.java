package com.rickiey_innovates.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "AssetDepreciation")
@Table(name = "asset_depreciation", schema = "farm", indexes = {
        @Index(name = "asset_depreciation_assets_id_fk", columnList = "asset_id")
})
public class AssetDepreciation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "asset_id", nullable = false)
    private Integer assetId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "depreciated_on")
    private Instant depreciatedOn;

    @Column(name = "amount")
    private Double amount;

    @NotNull
    @Column(name = "farm", nullable = false)
    private Integer farm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getDepreciatedOn() {
        return depreciatedOn;
    }

    public void setDepreciatedOn(Instant depreciatedOn) {
        this.depreciatedOn = depreciatedOn;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getchurch() {
        return farm;
    }

    public void setchurch(Integer church) {
        this.farm = church;
    }

}
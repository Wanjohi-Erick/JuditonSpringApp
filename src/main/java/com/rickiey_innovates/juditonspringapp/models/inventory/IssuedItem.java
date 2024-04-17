package com.rickiey_innovates.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "IssuedItem")
@Table(name = "issued_items", schema = "farm", indexes = {
        @Index(name = "FK_issued_items_items", columnList = "item_id")
})
public class IssuedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Size(max = 100)
    @NotNull
    @Column(name = "individual_id", nullable = false, length = 100)
    private String individualId;

    @Size(max = 100)
    @NotNull
    @Column(name = "individual_category", nullable = false, length = 100)
    private String individualCategory;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Size(max = 50)
    @NotNull
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotNull
    @Column(name = "issued_on", nullable = false)
    private Instant issuedOn;

    @Column(name = "returned_on")
    private LocalDate returnedOn;

    @Size(max = 500)
    @Column(name = "comments", length = 500)
    private String comments;

    @Size(max = 50)
    @Column(name = "issued_by", length = 50)
    private String issuedBy;

    @Size(max = 50)
    @Column(name = "returned_by", length = 50)
    private String returnedBy;

    @NotNull
    @Column(name = "farm", nullable = false)
    private Integer farm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getIndividualId() {
        return individualId;
    }

    public void setIndividualId(String individualId) {
        this.individualId = individualId;
    }

    public String getIndividualCategory() {
        return individualCategory;
    }

    public void setIndividualCategory(String individualCategory) {
        this.individualCategory = individualCategory;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Instant issuedOn) {
        this.issuedOn = issuedOn;
    }

    public LocalDate getReturnedOn() {
        return returnedOn;
    }

    public void setReturnedOn(LocalDate returnedOn) {
        this.returnedOn = returnedOn;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getReturnedBy() {
        return returnedBy;
    }

    public void setReturnedBy(String returnedBy) {
        this.returnedBy = returnedBy;
    }

    public Integer getfarm() {
        return farm;
    }

    public void setfarm(Integer farm) {
        this.farm = farm;
    }

}
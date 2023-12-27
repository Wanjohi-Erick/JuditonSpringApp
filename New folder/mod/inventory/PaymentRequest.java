package com.walgotech.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "PaymentRequest")
@Table(name = "payment_request", schema = "church")
public class PaymentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Size(max = 50)
    @Column(name = "pv", length = 50)
    private String pv;

    @Size(max = 500)
    @NotNull
    @Column(name = "details", nullable = false, length = 500)
    private String details;

    @Size(max = 50)
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Size(max = 200)
    @NotNull
    @Column(name = "created_by", nullable = false, length = 200)
    private String createdBy;

    @Size(max = 200)
    @Column(name = "Reviewed_by", nullable = false, length = 200)
    private String reviewedBy;

    @Column(name = "Reviewed_on")
    private Instant reviewedOn;

    @Size(max = 200)
    @Column(name = "Approved_by", length = 200)
    private String approvedBy;

    @Column(name = "Approved_on")
    private Instant approvedOn;

    @Size(max = 200)
    @Column(name = "Disbursed_by", nullable = false, length = 200)
    private String disbursedBy;

    @Column(name = "Disbursed_on")
    private Instant disbursedOn;

    @NotNull
    @Column(name = "church", nullable = false)
    private Integer church;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Instant getReviewedOn() {
        return reviewedOn;
    }

    public void setReviewedOn(Instant reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(Instant approvedOn) {
        this.approvedOn = approvedOn;
    }

    public String getDisbursedBy() {
        return disbursedBy;
    }

    public void setDisbursedBy(String disbursedBy) {
        this.disbursedBy = disbursedBy;
    }

    public Instant getDisbursedOn() {
        return disbursedOn;
    }

    public void setDisbursedOn(Instant disbursedOn) {
        this.disbursedOn = disbursedOn;
    }

    public Integer getchurch() {
        return church;
    }

    public void setchurch(Integer church) {
        this.church = church;
    }

}
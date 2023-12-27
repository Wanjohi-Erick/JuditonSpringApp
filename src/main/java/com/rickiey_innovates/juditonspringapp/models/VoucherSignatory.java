package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Table(name = "voucher_signatory")
@Entity
public class VoucherSignatory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "voucher_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paymentvoucher voucher;

    @Column(name = "accountant", nullable = false)
    private Integer accountant;

    @Column(name = "acc_date")
    private LocalDate accountantDate;

    @Column(name = "second_signatory", nullable = false)
    private Integer secondSignatory;

    @Column(name = "ss_date")
    private LocalDate ssDate;

    @Column(name = "senior_pastor", nullable = false)
    private Integer seniorPastor;

    @Column(name = "sp_date")
    private LocalDate spDate;

    @Column(name = "treasurer", nullable = false)
    private Integer treasurer;

    @Column(name = "tr_date")
    private LocalDate trDate;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Paymentvoucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Paymentvoucher voucher) {
        this.voucher = voucher;
    }

    public Integer getAccountant() {
        return accountant;
    }

    public void setAccountant(Integer accountant) {
        this.accountant = accountant;
    }

    public Integer getSecondSignatory() {
        return secondSignatory;
    }

    public void setSecondSignatory(Integer secondSignatory) {
        this.secondSignatory = secondSignatory;
    }

    public Integer getSeniorPastor() {
        return seniorPastor;
    }

    public void setSeniorPastor(Integer seniorPastor) {
        this.seniorPastor = seniorPastor;
    }

    public Integer getTreasurer() {
        return treasurer;
    }

    public void setTreasurer(Integer treasurer) {
        this.treasurer = treasurer;
    }

    public LocalDate getAccountantDate() {
        return accountantDate;
    }

    public void setAccountantDate(LocalDate accountantDate) {
        this.accountantDate = accountantDate;
    }

    public LocalDate getSsDate() {
        return ssDate;
    }

    public void setSsDate(LocalDate ssDate) {
        this.ssDate = ssDate;
    }

    public LocalDate getSpDate() {
        return spDate;
    }

    public void setSpDate(LocalDate spDate) {
        this.spDate = spDate;
    }

    public LocalDate getTrDate() {
        return trDate;
    }

    public void setTrDate(LocalDate trDate) {
        this.trDate = trDate;
    }
}

package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "tax")
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "`pv#`", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paymentvoucher pv;

    @Column(name = "paymentpv")
    private Integer paymentpv;

    @Column(name = "type", nullable = false, length = 500)
    private String type;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "status", length = 50, columnDefinition = "VARCHAR(255) DEFAULT 'Pending payment'")
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Paymentvoucher getPv() {
        return pv;
    }

    public void setPv(Paymentvoucher pv) {
        this.pv = pv;
    }

    public Integer getPaymentpv() {
        return paymentpv;
    }

    public void setPaymentpv(Integer paymentpv) {
        this.paymentpv = paymentpv;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
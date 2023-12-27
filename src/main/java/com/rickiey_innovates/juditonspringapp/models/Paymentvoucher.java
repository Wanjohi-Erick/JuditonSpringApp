package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "paymentvouchers")
public class Paymentvoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Pv id`", nullable = false)
    private Integer id;

    @Column(name = "`Voucher #`", nullable = false)
    private String voucher;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "activity", nullable = false)
    private Integer activity;

    @Column(name = "`Payee Name`", nullable = false, length = 500)
    private String payeeName;

    @Column(name = "Details", nullable = false, length = 5000)
    private String details;

    @Column(name = "status", nullable = false, length = 5000)
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farm farm;
}
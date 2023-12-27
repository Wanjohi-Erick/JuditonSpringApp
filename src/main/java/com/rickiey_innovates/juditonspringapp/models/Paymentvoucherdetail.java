package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "paymentvoucherdetails")
public class Paymentvoucherdetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "`pv#`", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paymentvoucher pv;

    @Column(name = "Particulars", nullable = false, length = 1000)
    private String particulars;

    @Column(name = "Qty", nullable = false)
    private Double qty;

    @Column(name = "Rate", nullable = false)
    private Double rate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farm farm;

}
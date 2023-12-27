package com.rickiey_innovates.juditonspringapp.models.payrolls;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "allowances")
public class Allowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "`PAY NO`", nullable = false)
    private Integer payNo;

    @NotNull
    @Column(name = "payroll", nullable = false)
    private Integer payroll;

    @Column(name = "ALLOWANCE")
    private Integer allowance;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "date")
    private Instant date;

}
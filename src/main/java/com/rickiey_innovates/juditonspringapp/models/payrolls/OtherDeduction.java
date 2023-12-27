package com.rickiey_innovates.juditonspringapp.models.payrolls;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "`other deductions`")
public class OtherDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "PAYNO", nullable = false)
    private Integer payno;

    @NotNull
    @Column(name = "payroll", nullable = false)
    private Integer payroll;

    @NotNull
    @Column(name = "DEDUCTION", nullable = false)
    private Integer deduction;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "date")
    private Instant date;

}
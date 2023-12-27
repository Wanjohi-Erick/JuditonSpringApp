package com.rickiey_innovates.juditonspringapp.models.payrolls;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "`monthly pays`")
public class MonthlyPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "payroll", nullable = false)
    private Payroll payroll;

    @NotNull
    @Column(name = "PAYNO", nullable = false)
    private Integer payno;

    @NotNull
    @Column(name = "MONTH", nullable = false)
    private Integer month;

    @NotNull
    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "`NET SALARY`", nullable = false)
    private Double netSalary;

    @NotNull
    @Column(name = "`GROSS SALARY`", nullable = false)
    private Double grossSalary;

    @NotNull
    @Column(name = "TAXABLE", nullable = false)
    private Double taxable;

    @NotNull
    @Column(name = "NHIF", nullable = false)
    private Double nhif;

    @NotNull
    @Column(name = "NSSF", nullable = false)
    private Double nssf;

    @NotNull
    @Column(name = "HELB", nullable = false)
    private Double helb;

    @NotNull
    @Column(name = "`KRA TAX`", nullable = false)
    private Double kraTax;

    @NotNull
    @Column(name = "HOUSING", nullable = false)
    private Double housing;

    @NotNull
    @Column(name = "`PAID ON`", nullable = false)
    private Instant paidOn;

    @Size(max = 50)
    @Column(name = "HOUSE", length = 50)
    private String house;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

}
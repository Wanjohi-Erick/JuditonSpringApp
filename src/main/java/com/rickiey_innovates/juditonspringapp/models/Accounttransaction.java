package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "accounttransactions")
public class Accounttransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Transaction id`", nullable = false)
    private Integer id;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "`Ref #`", length = 500)
    private String ref;

    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Account")
    private Activity account;*/

    @Column(name = "Account")
    private int account;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Bank")
    private Bankaccount bank;

    @Column(name = "`Payee/Payer`", length = 500)
    private String payeePayer;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "`cheque #`", length = 500)
    private String cheque;

    @Column(name = "Activity")
    private Integer activity;

    @Column(name = "Credit")
    private Double credit;

    @Column(name = "Debit")
    private Double debit;

    @Column(name = "Status", length = 500)
    private String status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm_activity")
    private FarmActivity farmActivity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm")
    private Farm farm;

}
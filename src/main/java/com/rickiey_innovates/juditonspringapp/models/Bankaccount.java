package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bankaccounts")
public class Bankaccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`Acc id`", nullable = false)
    private Integer id;

    @Column(name = "`Account Name`", nullable = false, length = 50)
    private String accountName;

    @Column(name = "`Account #`", nullable = false, length = 50)
    private String account;

    @Column(name = "`Bank Name`", nullable = false, length = 50)
    private String bankName;

    @Column(name = "Type", nullable = false, length = 50)
    private String type;

    @Column(name = "Branch", nullable = false, length = 50)
    private String branch;

    @Column(name = "bankcode", nullable = false, length = 50)
    private String bankcode;

    @Column(name = "swiftcode", nullable = false, length = 50)
    private String swiftcode;

    @Column(name = "Opening Balance", nullable = false, length = 50)
    private Double openingBalance;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farm farm;

    public Farm getChurch() {
        return farm;
    }

    public void setChurch(Farm farm) {
        this.farm = farm;
    }

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bank")
    private List<Accounttransaction> accounttransactions;

    public Double getBalance() {
        double creditSum = accounttransactions.stream()
                .filter(transaction -> transaction.getCredit() != null)
                .mapToDouble(Accounttransaction::getCredit)
                .sum();

        double debitSum = accounttransactions.stream()
                .filter(transaction -> transaction.getDebit() != null)
                .mapToDouble(Accounttransaction::getDebit)
                .sum();

        // If both creditSum and debitSum are null, return 0.0
        if (Double.isNaN(creditSum) && Double.isNaN(debitSum)) {
            return 0.0;
        }

        // If creditSum is null, consider it as 0
        creditSum = Double.isNaN(creditSum) ? 0.0 : creditSum;

        // If debitSum is null, consider it as 0
        debitSum = Double.isNaN(debitSum) ? 0.0 : debitSum;

        return creditSum - debitSum;
    }
}
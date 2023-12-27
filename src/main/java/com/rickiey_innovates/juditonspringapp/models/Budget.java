package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @Column(name = "`Budget id`", nullable = false)
    private Integer id;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Project", length = 500)
    private String project;

    @Column(name = "Programme", length = 500)
    private String programme;

    @Column(name = "`Budget Period`", length = 500)
    private String budgetPeriod;

    @Column(name = "`Budget Currency`", length = 500)
    private String budgetCurrency;

    @Column(name = "`Exchange Rate`")
    private Double exchangeRate;

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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(String budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public String getBudgetCurrency() {
        return budgetCurrency;
    }

    public void setBudgetCurrency(String budgetCurrency) {
        this.budgetCurrency = budgetCurrency;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

}
package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "outcomes")
public class Outcome {
    @Id
    @Column(name = "`Outcome id`", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Budget", nullable = false)
    private Budget budget;

    @Column(name = "`Outcome #`", nullable = false, length = 500)
    private String outcome;

    @Column(name = "Outcome", nullable = false, length = 500)
    private String outcome1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome1() {
        return outcome1;
    }

    public void setOutcome1(String outcome1) {
        this.outcome1 = outcome1;
    }

}
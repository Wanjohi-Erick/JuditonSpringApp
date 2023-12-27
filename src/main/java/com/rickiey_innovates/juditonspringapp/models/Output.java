package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "outputs")
public class Output {
    @Id
    @Column(name = "`Output id`", nullable = false)
    private Integer id;

    @Column(name = "`Output #`", nullable = false, length = 500)
    private String output;

    @Column(name = "Output", nullable = false, length = 500)
    private String output1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "outcome", nullable = false)
    private Outcome outcome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput1() {
        return output1;
    }

    public void setOutput1(String output1) {
        this.output1 = output1;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

}
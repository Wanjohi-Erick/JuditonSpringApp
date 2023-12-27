package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "budgetactivities")
public class Budgetactivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Output")
    private Output output;

    @Column(name = "Account", nullable = false)
    private Integer account;

    @Column(name = "`FRAS CRITERIA`", length = 500)
    private String frasCriteria;

    @Column(name = "`SCC Contribution`")
    private Double sCCContribution;

    @Column(name = "`No. of Women`")
    private Integer noOfWomen;

    @Column(name = "`No. of Men`")
    private Integer noOfMen;

    @Column(name = "`Alloc to Women`")
    private Double allocToWomen;

    @Column(name = "`Alloc to Men`")
    private Double allocToMen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getsCCContribution() {
        return sCCContribution;
    }

    public void setsCCContribution(Double sCCContribution) {
        this.sCCContribution = sCCContribution;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getFrasCriteria() {
        return frasCriteria;
    }

    public void setFrasCriteria(String frasCriteria) {
        this.frasCriteria = frasCriteria;
    }

    public Double getSCCContribution() {
        return sCCContribution;
    }

    public void setSCCContribution(Double sCCContribution) {
        this.sCCContribution = sCCContribution;
    }

    public Integer getNoOfWomen() {
        return noOfWomen;
    }

    public void setNoOfWomen(Integer noOfWomen) {
        this.noOfWomen = noOfWomen;
    }

    public Integer getNoOfMen() {
        return noOfMen;
    }

    public void setNoOfMen(Integer noOfMen) {
        this.noOfMen = noOfMen;
    }

    public Double getAllocToWomen() {
        return allocToWomen;
    }

    public void setAllocToWomen(Double allocToWomen) {
        this.allocToWomen = allocToWomen;
    }

    public Double getAllocToMen() {
        return allocToMen;
    }

    public void setAllocToMen(Double allocToMen) {
        this.allocToMen = allocToMen;
    }

}
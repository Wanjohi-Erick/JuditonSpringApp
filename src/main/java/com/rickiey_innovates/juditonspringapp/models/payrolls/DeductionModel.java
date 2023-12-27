package com.rickiey_innovates.juditonspringapp.models.payrolls;

public class DeductionModel {
    private String Deduction;
    private int type;
    private int calculationtype;
    private double costperunit;
    private String visible;
    private Integer id;


    public String getDeduction() {
        return Deduction;
    }

    public void setDeduction(String Deduction) {
        this.Deduction = Deduction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCalculationtype() {
        return calculationtype;
    }

    public void setCalculationtype(int calculationtype) {
        this.calculationtype = calculationtype;
    }

    public double getCostperunit() {
        return costperunit;
    }

    public void setCostperunit(double costperunit) {
        this.costperunit = costperunit;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeductionModel{" +
                "Deduction='" + Deduction + '\'' +
                ", type=" + type +
                ", calculationtype=" + calculationtype +
                ", costperunit=" + costperunit +
                ", visible='" + visible + '\'' +
                ", id=" + id +
                '}';
    }
}

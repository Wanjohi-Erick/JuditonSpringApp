package com.rickiey_innovates.juditonspringapp.models;

public class TitheAndOff {
    MonthlyTithes monthlyTithes;
    MonthlyOffering monthlyOffering;

    public TitheAndOff() {

    }

    public MonthlyTithes getMonthlyTithes() {
        return monthlyTithes;
    }

    public void setMonthlyTithes(MonthlyTithes monthlyTithes) {
        this.monthlyTithes = monthlyTithes;
    }

    public MonthlyOffering getMonthlyOffering() {
        return monthlyOffering;
    }

    public void setMonthlyOffering(MonthlyOffering monthlyOffering) {
        this.monthlyOffering = monthlyOffering;
    }
}

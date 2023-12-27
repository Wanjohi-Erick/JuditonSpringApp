package com.rickiey_innovates.juditonspringapp.models;

public class MonthlyTithes {
    String year, month, credit;

    public MonthlyTithes(String year, String month, String credit) {
        this.year = year;
        this.month = month;
        this.credit = credit;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}

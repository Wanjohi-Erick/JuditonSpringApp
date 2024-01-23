package com.rickiey_innovates.juditonspringapp.models;

public class MonthlyTransactions {
    String year, month, creditOrDebit;

    public MonthlyTransactions(String year, String month, String creditOrDebit) {
        this.year = year;
        this.month = month;
        this.creditOrDebit = creditOrDebit;
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
        return creditOrDebit;
    }

    public void setCredit(String credit) {
        this.creditOrDebit = credit;
    }
}

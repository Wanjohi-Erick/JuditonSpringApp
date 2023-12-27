package com.rickiey_innovates.juditonspringapp.models;

public class SummaryDTO {
    private int memberTotal;
    private String income;
    private String expenses;
    private String balance;
    private double percentageIncrease;
    public int getMemberTotal() {
        return memberTotal;
    }

    public void setMemberTotal(int memberTotal) {
        this.memberTotal = memberTotal;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public double getPercentageExpensesIncrease() {
        return percentageIncrease;
    }

    public void setPercentageExpensesIncrease(double percentageIncrease) {
        this.percentageIncrease = percentageIncrease;
    }
}

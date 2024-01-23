package com.rickiey_innovates.juditonspringapp.models;

import lombok.Data;

@Data
public class IncomeAndExpenditure {
    MonthlyTransactions income;
    MonthlyTransactions expenditure;
}

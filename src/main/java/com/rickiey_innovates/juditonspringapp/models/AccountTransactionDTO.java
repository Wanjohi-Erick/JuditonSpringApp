package com.rickiey_innovates.juditonspringapp.models;

public class AccountTransactionDTO {

    Accounttransaction accounttransaction;
    Bankaccount bankaccount;

    public AccountTransactionDTO(Accounttransaction accounttransaction, Bankaccount bankaccount) {
        this.accounttransaction = accounttransaction;
        this.bankaccount = bankaccount;
    }

    public Accounttransaction getAccounttransaction() {
        return accounttransaction;
    }

    public void setAccounttransaction(Accounttransaction accounttransaction) {
        this.accounttransaction = accounttransaction;
    }

    public Bankaccount getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(Bankaccount bankaccount) {
        this.bankaccount = bankaccount;
    }
}

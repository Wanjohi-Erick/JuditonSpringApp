package com.rickiey_innovates.juditonspringapp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VoucherRequest {
    private String recordFor;
    private Long plantedCropId;
    private LocalDate date;
    private int activity;
    private String payee;
    private String details;
    private int bank;
    private String transRef;
    private double withholding;
    private double vat;
    private double profFees;
    private Double amountBeforeTax;
    private Double totalPayable;
    private List<VoucherTableRow> voucherTableData;

    @Override
    public String toString() {
        return "VoucherRequest{" +
                "recordFor='" + recordFor + '\'' +
                ", plantedCropId=" + plantedCropId +
                ", date=" + date +
                ", activity=" + activity +
                ", payee='" + payee + '\'' +
                ", details='" + details + '\'' +
                ", bank=" + bank +
                ", transRef='" + transRef + '\'' +
                ", withholding=" + withholding +
                ", vat=" + vat +
                ", profFees=" + profFees +
                ", amountBeforeTax=" + amountBeforeTax +
                ", totalPayable=" + totalPayable +
                ", voucherTableData=" + voucherTableData +
                '}';
    }
}


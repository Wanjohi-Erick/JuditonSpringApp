package com.rickiey_innovates.juditonspringapp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReceiptRequest {
    private String recordFor;
    Long plantedCropId;
    private LocalDate date;
    private String receivedFrom;
    private String details;
    private int bank;
    private String transRef;
    private List<ReceiptTableRow> receiptTableRowList;


    @Override
    public String toString() {
        return "ReceiptRequest{" +
                "recordFor=" + recordFor +
                "plantedCropId=" + plantedCropId +
                ", date=" + date +
                ", receivedFrom='" + receivedFrom + '\'' +
                ", details='" + details + '\'' +
                ", bank=" + bank +
                ", transRef='" + transRef + '\'' +
                ", receiptTableRowList=" + receiptTableRowList +
                '}';
    }
}

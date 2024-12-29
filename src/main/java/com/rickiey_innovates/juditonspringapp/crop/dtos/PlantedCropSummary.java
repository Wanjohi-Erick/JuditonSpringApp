package com.rickiey_innovates.juditonspringapp.crop.dtos;

import lombok.Data;

@Data
public class PlantedCropSummary {
    private Long cropId;
    private String cropName;
    private Double exp; // Total debit
    private Double inc; // Total credit
    private String pro; // Profit (credit - debit)
}

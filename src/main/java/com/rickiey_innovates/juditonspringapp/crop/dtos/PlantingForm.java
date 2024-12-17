package com.rickiey_innovates.juditonspringapp.crop.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PlantingForm {
    private Long id;

    private String cropVariety;
    private Long season;
    private String plantingType;
    private String plantingAge;
    private LocalDate plantingDate;
    private int daysToHarvest;
    private String plantingMethod;

    // Bed-specific fields
    private Integer numBeds;
    private Integer rowsPerBed;
    private Double lengthOfBed;
    private Double plantSpacingBed;

    // Row-specific fields
    private Integer numRows;
    private Double lengthOfRow;
    private Double plantSpacingRow;

    // Individual-specific fields
    private Integer numPlants;

    @Override
    public String toString() {
        return "PlantingForm{" +
                "id=" + id +
                ", cropVariety='" + cropVariety + '\'' +
                ", season=" + season +
                ", plantingType='" + plantingType + '\'' +
                ", plantingAge='" + plantingAge + '\'' +
                ", plantingDate=" + plantingDate +
                ", daysToHarvest=" + daysToHarvest +
                ", plantingMethod='" + plantingMethod + '\'' +
                ", numBeds=" + numBeds +
                ", rowsPerBed=" + rowsPerBed +
                ", lengthOfBed=" + lengthOfBed +
                ", plantSpacingBed=" + plantSpacingBed +
                ", numRows=" + numRows +
                ", lengthOfRow=" + lengthOfRow +
                ", plantSpacingRow=" + plantSpacingRow +
                ", numPlants=" + numPlants +
                '}';
    }
}

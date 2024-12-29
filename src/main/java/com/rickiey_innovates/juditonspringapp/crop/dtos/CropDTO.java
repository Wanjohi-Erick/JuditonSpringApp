package com.rickiey_innovates.juditonspringapp.crop.dtos;

import lombok.Value;

import java.io.Serializable;

@Value
public class CropDTO implements Serializable {
    Long id;
    String name;
    Long type;
    Long plantingUnits;
    Long harvestingUnits;
}

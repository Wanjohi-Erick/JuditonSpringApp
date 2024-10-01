package com.rickiey_innovates.juditonspringapp.crop.dtos;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.rickiey_innovates.juditonspringapp.crop.models.CropType}
 */
@Value
public class CropTypeDto implements Serializable {
    Long cropName;
    String cropType;
}
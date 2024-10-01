package com.rickiey_innovates.juditonspringapp.crop.dtos;

import lombok.Value;

import java.io.Serializable;

@Value
public class CropVarietyDTO implements Serializable {
    Long cropName;
    String name;
}

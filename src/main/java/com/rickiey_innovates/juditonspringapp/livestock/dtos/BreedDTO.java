package com.rickiey_innovates.juditonspringapp.livestock.dtos;

import lombok.Value;

import java.io.Serializable;

@Value
public class BreedDTO implements Serializable {
    Long livestockId;
    String name;
}

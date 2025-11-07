package com.rickiey_innovates.juditonspringapp.livestock.dtos;

import lombok.Value;

import java.io.Serializable;

@Value
public class LivestockDTO implements Serializable {
    Long id;
    String name;
}

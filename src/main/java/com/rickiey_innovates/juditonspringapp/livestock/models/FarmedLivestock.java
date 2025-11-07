package com.rickiey_innovates.juditonspringapp.livestock.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rickiey_innovates.juditonspringapp.crop.models.CropVariety;
import com.rickiey_innovates.juditonspringapp.crop.models.Season;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "farmed_livestock")
public class FarmedLivestock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "livestock_breed")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Breed breed;
    private Long keptQuantity;
    private Long harvestedQuantity;
    private LocalDate keepingDate;
    private LocalDate harvestedDate;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm")
    private Farm farm;
}
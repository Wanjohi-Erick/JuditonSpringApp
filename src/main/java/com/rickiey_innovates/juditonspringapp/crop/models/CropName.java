package com.rickiey_innovates.juditonspringapp.crop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "crop_name")
public class CropName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm")
    private Farm farm;
}
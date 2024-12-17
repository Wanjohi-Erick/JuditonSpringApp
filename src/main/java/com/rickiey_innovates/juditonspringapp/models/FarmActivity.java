package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rickiey_innovates.juditonspringapp.crop.models.PlantedCrop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "farm_activity")
public class FarmActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String ref;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "account")
    private Activity account;

    @Column(name = "description", length = 500)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private Accounttransaction accounttransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "planted_crop")
    private PlantedCrop plantedCrop;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm")
    private Farm farm;
}
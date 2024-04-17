package com.rickiey_innovates.juditonspringapp.models.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "assets")
public class Assets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "asset_life", length = 50)
    private String assetLife;

    @Column(name = "balance_as_at")
    private LocalDate balanceAsAt;

    @Size(max = 200)
    @NotNull
    @Column(name = "depreciation", nullable = false, length = 200)
    private String depreciation;

    @Size(max = 200)
    @NotNull
    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Size(max = 200)
    @NotNull
    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Size(max = 200)
    @NotNull
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @NotNull
    @Column(name = "opening_balance", nullable = false)
    private Double openingBalance;

    @Size(max = 200)
    @NotNull
    @Column(name = "period", nullable = false, length = 200)
    private String period;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Double rate;

    @Size(max = 200)
    @NotNull
    @Column(name = "serial", nullable = false, length = 200)
    private String serial;

    @Size(max = 200)
    @NotNull
    @Column(name = "status", nullable = false, length = 200)
    private String status;

    @Size(max = 200)
    @NotNull
    @Column(name = "type", nullable = false, length = 200)
    private String type;

    @Size(max = 200)
    @NotNull
    @Column(name = "used_by", nullable = false, length = 200)
    private String usedBy;

    @NotNull
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm", nullable = false)
    private Farm farm;

}
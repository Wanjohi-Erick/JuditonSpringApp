package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "earnings")
public class Allowances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farm farm;

    @Size(max = 300)
    @Column(name = "Earning", length = 300)
    private String Earning;
    
    @NotNull
    @Column(name = "type", nullable = false, columnDefinition = "double default 0")
    private Integer type;
    
    @NotNull
    @Column(name = "costperunit", nullable = false, columnDefinition = "double default 0")
    private Double costperunit;

    
    @Size(max = 10)
    @Column(name = "visible", length = 10)
    private String visible;

}
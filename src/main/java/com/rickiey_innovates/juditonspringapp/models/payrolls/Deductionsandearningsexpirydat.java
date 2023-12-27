package com.rickiey_innovates.juditonspringapp.models.payrolls;

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
@Table(name = "deductionsandearningsexpirydat")
public class Deductionsandearningsexpirydat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "payno", nullable = false)
    private Employee payno;

    @NotNull
    @Column(name = "item", nullable = false)
    private Integer item;

    @Size(max = 500)
    @NotNull
    @Column(name = "TYPE", nullable = false, length = 500)
    private String type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "startdate")
    private LocalDate startdate;

    @Column(name = "enddate")
    private LocalDate enddate;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "reccurrent")
    private Integer reccurrent;

}
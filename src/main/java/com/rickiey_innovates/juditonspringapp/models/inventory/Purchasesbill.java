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

@Getter
@Setter
@Entity
@Table(name = "purchasesbills")
public class Purchasesbill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billid", nullable = false)
    private Integer id;

    @Size(max = 300)
    @Column(name = "billno", length = 300)
    private String billno;

    @Size(max = 300)
    @Column(name = "billdate", length = 300)
    private String billdate;

    @Size(max = 300)
    @Column(name = "duedate", length = 300)
    private String duedate;

    @Column(name = "suplier")
    private Integer suplier;

    @Size(max = 300)
    @NotNull
    @Column(name = "notes", nullable = false, length = 300)
    private String notes;

    @Size(max = 300)
    @NotNull
    @Column(name = "indentify", nullable = false, length = 300)
    private String indentify;

    @Column(name = "item_code")
    private Integer itemCode;

    @Column(name = "qty")
    private Integer qty;

    @NotNull
    @Column(name = "amt", nullable = false)
    private Double amt;

    @Size(max = 300)
    @NotNull
    @Column(name = "account", nullable = false, length = 300)
    private String account;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm")
    private Farm farm;

}
package com.rickiey_innovates.juditonspringapp.models.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "purchase_order_receives")
public class PurchaseOrderReceive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "purchase_order_item_id")
    private Integer purchaseOrderItemId;

    @Column(name = "quantity_received")
    private Double quantityReceived;

    @Size(max = 500)
    @Column(name = "comments", length = 500)
    private String comments;

    @Column(name = "received_on")
    private Instant receivedOn;

    @Size(max = 100)
    @Column(name = "received_by", length = 100)
    private String receivedBy;

    @Size(max = 100)
    @Column(name = "status", length = 100)
    private String status;

    @Size(max = 100)
    @Column(name = "pv_no", length = 100)
    private String pvNo;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm", nullable = false)
    private Farm farm;

}
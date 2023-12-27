package com.walgotech.juditonspringapp.models.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walgotech.juditonspringapp.models.Church;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "purchase_order_id")
    private Integer purchaseOrderId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_price")
    private Double itemPrice;

    @Column(name = "item_quantity")
    private Integer itemQuantity;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church", nullable = false)
    private Church church;

}
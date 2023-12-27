package com.rickiey_innovates.juditonspringapp.models.payrolls;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "`leave`")
public class Leave {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "payno", nullable = false, length = 50)
    private String payno;

    @Size(max = 500)
    @NotNull
    @Column(name = "category", nullable = false, length = 500)
    private String category;

    @Column(name = "`from`")
    private LocalDate from;

    @Column(name = "`to`")
    private LocalDate to;

    @Size(max = 500)
    @Column(name = "comments", length = 500)
    private String comments;

    @Size(max = 500)
    @Column(name = "status", length = 500)
    private String status;

}
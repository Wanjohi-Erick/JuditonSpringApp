package com.rickiey_innovates.juditonspringapp.models.payrolls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "deductions")
public class  Deductions {
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
    @Column(name = "Deduction", length = 300)
    private String Deduction;
    
    @NotNull
    @Column(name = "type", nullable = false, columnDefinition = "double default 0")
    private Integer type;
    
    @NotNull
    @Column(name = "calculationtype", nullable = false, columnDefinition = "double default 0")
    private Integer calculationtype;
    
    @NotNull
    @Column(name = "costperunit", nullable = false, columnDefinition = "double default 0")
    private Double costperunit;

    @Size(max = 10)
    @Column(name = "visible", length = 10)
    private String visible;
}
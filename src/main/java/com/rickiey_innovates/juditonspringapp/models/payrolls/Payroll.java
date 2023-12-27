package com.rickiey_innovates.juditonspringapp.models.payrolls;

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
@Table(name = "payrolls")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "farm")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Farm farm;

    @Size(max = 20)
    @Column(name = "date", length = 20)
    private LocalDate date;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

}
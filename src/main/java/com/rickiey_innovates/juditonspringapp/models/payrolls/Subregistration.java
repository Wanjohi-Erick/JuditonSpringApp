package com.rickiey_innovates.juditonspringapp.models.payrolls;

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
@Table(name = "subregistration")
public class Subregistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "farm", nullable = false)
    private Farm farm;

    @NotNull
    @Column(name = "biometricsid", nullable = false)
    private Integer biometricsid;

    @Size(max = 300)
    @Column(name = "`FIRST NAME`", length = 300)
    private String firstName;

    @Size(max = 300)
    @Column(name = "Idnu", length = 300)
    private String idnu;

    @Size(max = 1300)
    @Column(name = "`SECOND NAME`", length = 1300)
    private String secondName;

    @Size(max = 300)
    @Column(name = "SURNAME", length = 300)
    private String surname;

    @Size(max = 300)
    @Column(name = "`ID NU`", length = 300)
    private String idNu;

    @Size(max = 300)
    @Column(name = "GENDER", length = 300)
    private String gender;

    @Column(name = "`DATE OF JOINING`")
    private LocalDate dateOfJoining;

    @Size(max = 300)
    @Column(name = "`PHONE NU`", length = 300)
    private String phoneNu;

    @Size(max = 300)
    @Column(name = "EMAIL", length = 300)
    private String email;

    @Size(max = 300)
    @Column(name = "RESIDENCE", length = 300)
    private String residence;

    @Size(max = 300)
    @Column(name = "RELIGION", length = 300)
    private String religion;

    @Size(max = 300)
    @Column(name = "`MEDICAL CONDITION`", length = 300)
    private String medicalCondition;

    @Size(max = 300)
    @Column(name = "SIMAGE", length = 300)
    private String simage;

    @Size(max = 300)
    @Column(name = "Position", length = 300)
    private String position;

    @Size(max = 300)
    @Column(name = "OCCUPATION", length = 300)
    private String occupation;

    @Size(max = 300)
    @Column(name = "BLOODGROUP", length = 300)
    private String bloodgroup;

    @Size(max = 300)
    @Column(name = "ReasonForRemoval", length = 300)
    private String reasonForRemoval;

    @Size(max = 300)
    @Column(name = "whoapproved", length = 300)
    private String whoapproved;

    @Size(max = 300)
    @Column(name = "`BOOKS BORROWED`", length = 300)
    private String booksBorrowed;

    @Size(max = 300)
    @Column(name = "`BOOK RETURNED`", length = 300)
    private String bookReturned;

    @Column(name = "RemovalDate")
    private LocalDate removalDate;

    @Size(max = 300)
    @Column(name = "Status", length = 300)
    private String status;

    @Size(max = 300)
    @Column(name = "currentstatus", length = 300)
    private String currentstatus;

    @Column(name = "gadgetstatus")
    private Integer gadgetstatus;

}
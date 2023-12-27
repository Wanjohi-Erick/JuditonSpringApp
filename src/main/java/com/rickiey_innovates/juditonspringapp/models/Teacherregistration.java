package com.rickiey_innovates.juditonspringapp.models;

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
@Table(name = "teacherregistration")
public class Teacherregistration {
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
    @Column(name = "Trnu", length = 300)
    private String trnu;

    @Size(max = 300)
    @Column(name = "`TSC NU`", length = 300)
    private String tscNu;

    @Size(max = 1300)
    @Column(name = "`FIRST NAME`", length = 1300)
    private String firstName;

    @Size(max = 1300)
    @Column(name = "`SECOND NAME`", length = 1300)
    private String secondName;

    @Size(max = 300)
    @Column(name = "SURNAME", length = 300)
    private String surname;

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
    @Column(name = "UPI", length = 300)
    private String upi;

    @Size(max = 300)
    @Column(name = "ADDRESS", length = 300)
    private String address;

    @Size(max = 300)
    @Column(name = "COUNTY", length = 300)
    private String county;

    @Size(max = 300)
    @Column(name = "`SUB COUNTY`", length = 300)
    private String subCounty;

    @Size(max = 300)
    @Column(name = "CONSTITUENCY", length = 300)
    private String constituency;

    @Size(max = 300)
    @Column(name = "WARD", length = 300)
    private String ward;

    @Size(max = 500)
    @Column(name = "SERVICE", length = 500)
    private String service;

    @Size(max = 300)
    @Column(name = "KRA", length = 300)
    private String kra;

    @Size(max = 300)
    @Column(name = "idnumber", length = 300)
    private String idnumber;

    @Size(max = 300)
    @Column(name = "TITLE", length = 300)
    private String title;

    @Size(max = 300)
    @Column(name = "MARITAL", length = 300)
    private String marital;

    @Size(max = 300)
    @Column(name = "ETHNICITY", length = 300)
    private String ethnicity;

    @Size(max = 3000)
    @Column(name = "SIMAGE", length = 3000)
    private String simage;

    @Size(max = 3000)
    @Column(name = "MAINSUB", length = 3000)
    private String mainsub;

    @Size(max = 3000)
    @Column(name = "PASSWORD", length = 3000)
    private String password;

    @Size(max = 3000)
    @Column(name = "status", length = 3000)
    private String status;

    @Size(max = 255)
    @Column(name = "json")
    private String json;

    @Size(max = 255)
    @Column(name = "currentstatus")
    private String currentstatus;

    @Column(name = "gadgetstatus")
    private Integer gadgetstatus;

}
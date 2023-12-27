package com.rickiey_innovates.juditonspringapp.models.payrolls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farm farm;

    @Size(max = 50)
    @Column(name = "payno", length = 50)
    private String payno;

    @Size(max = 50)
    @Column(name = "employeetype", length = 50)
    private String employeetype;

    @Size(max = 50)
    @Column(name = "fname", length = 50)
    private String fname;

    @Size(max = 1000)
    @Column(name = "sname", length = 1000)
    private String sname;

    @Size(max = 50)
    @Column(name = "surname", length = 50)
    private String surname;

    @Size(max = 500)
    @Column(name = "bankname", length = 500)
    private String bankname;

    @Size(max = 500)
    @Column(name = "accountnumber", length = 500)
    private String accountnumber;

    @Size(max = 1000)
    @Column(name = "idnu", length = 1000)
    private String idnu;

    @Size(max = 50)
    @Column(name = "`kra pin`", length = 50)
    private String kraPin;

    @Size(max = 50)
    @Column(name = "linktoteacher", length = 50)
    private String linktoteacher;

    @Size(max = 50)
    @Column(name = "linktostaff", length = 50)
    private String linktostaff;

    @Size(max = 500)
    @Column(name = "dob", length = 500)
    private String dob;

    @Size(max = 50)
    @Column(name = "gender", length = 50)
    private String gender;

    @Size(max = 50)
    @Column(name = "welfare", length = 50)
    private String welfare;

    @Size(max = 50)
    @Column(name = "sacco", length = 50)
    private String sacco;

    @Column(name = "nssf")
    private Integer nssf;

    @Size(max = 50)
    @Column(name = "`nssf number`", length = 50)
    private String nssfNumber;

    @Column(name = "nhif")
    private Integer nhif;

    @Size(max = 50)
    @Column(name = "`nhif number`", length = 50)
    private String nhifNumber;

    @Column(name = "helb")
    private Integer helb;

    @Size(max = 500)
    @Column(name = "paymeth", length = 500)
    private String paymeth;

    @Size(max = 500)
    @Column(name = "branchname", length = 500)
    private String branchname;

    @Size(max = 500)
    @Column(name = "accounttype", length = 500)
    private String accounttype;

    @Size(max = 500)
    @Column(name = "accholdername", length = 500)
    private String accholdername;

    @Size(max = 500)
    @Column(name = "phone", length = 500)
    private String phone;

    @Size(max = 500)
    @Column(name = "raddress", length = 500)
    private String raddress;

    @Size(max = 500)
    @Column(name = "postaladdress", length = 500)
    private String postaladdress;

    @Size(max = 500)
    @Column(name = "postalcode", length = 500)
    private String postalcode;

    @Column(name = "paye")
    private Integer paye;

    @Size(max = 500)
    @Column(name = "streetnum", length = 500)
    private String streetnum;

    @Size(max = 500)
    @Column(name = "streetname", length = 500)
    private String streetname;

    @Size(max = 500)
    @Column(name = "city", length = 500)
    private String city;

    @Size(max = 500)
    @Column(name = "email", length = 500)
    private String email;

    @Size(max = 500)
    @Column(name = "`pay cycle`", length = 500)
    private String payCycle;

    @Column(name = "housed")
    private Integer housed;

    @Column(name = "housinglev")
    private Integer housinglev;

    @Size(max = 500)
    @Column(name = "occupation", length = 500)
    private String occupation;

    @Size(max = 500)
    @Column(name = "nextofkin", length = 500)
    private String nextofkin;

    @Size(max = 500)
    @Column(name = "nextofkinnumber", length = 500)
    private String nextofkinnumber;

    @Column(name = "startdate")
    private LocalDate startdate;

    @Size(max = 500)
    @Column(name = "status", length = 500)
    private String status;

    @Size(max = 500)
    @Column(name = "SIMAGE", length = 500)
    private String simage;

    @Column(name = "enddate")
    private LocalDate enddate;

}
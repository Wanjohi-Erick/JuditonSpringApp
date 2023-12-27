package com.walgotech.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;;

@Entity(name = "Vendor")
@Table(name = "vendors", schema = "church")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @Size(max = 100)
    @NotNull
    @Column(name = "company", nullable = false, length = 100)
    private String company;

    @Size(max = 100)
    @NotNull
    @Column(name = "phone", nullable = false, length = 100)
    private String phone;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 100)
    @NotNull
    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Size(max = 100)
    @NotNull
    @Column(name = "kra_pin", nullable = false, length = 100)
    private String kraPin;

    @Size(max = 100)
    @NotNull
    @Column(name = "item_group", nullable = false, length = 100)
    private String itemGroup;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private Instant regDate;

    @NotNull
    @Column(name = "church", nullable = false)
    private Integer church;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKraPin() {
        return kraPin;
    }

    public void setKraPin(String kraPin) {
        this.kraPin = kraPin;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Instant getRegDate() {
        return regDate;
    }

    public void setRegDate(Instant regDate) {
        this.regDate = regDate;
    }

    public Integer getchurch() {
        return church;
    }

    public void setchurch(Integer church) {
        this.church = church;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + id +
                ", contactName='" + contactName + '\'' +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", kraPin='" + kraPin + '\'' +
                ", itemGroup='" + itemGroup + '\'' +
                ", regDate=" + regDate +
                ", church=" + church +
                '}';
    }
}
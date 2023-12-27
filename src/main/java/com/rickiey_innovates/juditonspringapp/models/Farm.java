package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "farm")
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "region", nullable = false, length = 500)
    private String region;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "country", nullable = false, length = 500)
    private String country;

    @Column(name = "logo", length = 500)
    private String logo;

    @Column(name = "email", nullable = false, length = 500)
    private String email;

    @Column(name = "zip", nullable = false, length = 500)
    private String zip;

    @Column(name = "phone", nullable = false, length = 500)
    private String phone;

    @Column(name = "upload_path", nullable = false, length = 500)
    private String uploadPath;

    @Column(name = "usebiometricsforpayroll", length = 500)
    private Integer useBioForPayroll;

    @Column(name = "payeeoption", length = 500)
    private Integer payeeOption;

    @Column(name = "nhifoption", length = 500)
    private Integer nhifOption;

    @Column(name = "nssfoption", length = 500)
    private Integer nssfOption;

    @Column(name = "SMSUSERNAME", length = 500)
    private String smsusername;

    @Column(name = "SMSKEY", length = 500)
    private String smskey;

    @Column(name = "SMSID", length = 500)
    private String smsid;

    @Column(name = "sms_account", length = 500)
    private String smsAccount;

    @Column(name = "Employercode", length = 500)
    private String employerCode;

    @Column(name = "TaxRelief", length = 500)
    private String taxRelief;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public Integer getUseBioForPayroll() {
        return useBioForPayroll;
    }

    public void setUseBioForPayroll(Integer useBioForPayroll) {
        this.useBioForPayroll = useBioForPayroll;
    }

    public String getSmsusername() {
        return smsusername;
    }

    public void setSmsusername(String smsusername) {
        this.smsusername = smsusername;
    }

    public String getSmskey() {
        return smskey;
    }

    public void setSmskey(String smskey) {
        this.smskey = smskey;
    }

    public String getSmsid() {
        return smsid;
    }

    public void setSmsid(String smsid) {
        this.smsid = smsid;
    }

    public String getSmsAccount() {
        return smsAccount;
    }

    public void setSmsAccount(String smsAccount) {
        this.smsAccount = smsAccount;
    }

}
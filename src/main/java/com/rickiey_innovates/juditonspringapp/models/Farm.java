package com.rickiey_innovates.juditonspringapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

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

    @Column(name = "Employercode", length = 500)
    private String employerCode;

    @Column(name = "TaxRelief", length = 500)
    private String taxRelief;

    @Size(max = 300)
    @Column(name = "SMSKEY", length = 300)
    private String smsKey;

    @Size(max = 300)
    @Column(name = "SMSUSERNAME", length = 300)
    private String smsUsername;

    @Size(max = 300)
    @Column(name = "SMSID", length = 300)
    private String smsId;

    @Size(max = 300)
    @Column(name = "sms_account", length = 300)
    private String smsAccount;

    @Size(max = 300)
    @Column(name = "sms_balance", length = 300)
    private double smsBalance;

    @Size(max = 300)
    @Column(name = "cost_per_sms", length = 300)
    private Double costPerSms;

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

    public String getSmsAccount() {
        return smsAccount;
    }

    public void setSmsAccount(String smsAccount) {
        this.smsAccount = smsAccount;
    }


    public Integer getPayeeOption() {
        return payeeOption;
    }

    public void setPayeeOption(Integer payeeOption) {
        this.payeeOption = payeeOption;
    }

    public Integer getNhifOption() {
        return nhifOption;
    }

    public void setNhifOption(Integer nhifOption) {
        this.nhifOption = nhifOption;
    }

    public Integer getNssfOption() {
        return nssfOption;
    }

    public void setNssfOption(Integer nssfOption) {
        this.nssfOption = nssfOption;
    }

    public String getEmployerCode() {
        return employerCode;
    }

    public void setEmployerCode(String employerCode) {
        this.employerCode = employerCode;
    }

    public String getTaxRelief() {
        return taxRelief;
    }

    public void setTaxRelief(String taxRelief) {
        this.taxRelief = taxRelief;
    }

    public String getSmsKey() {
        return smsKey;
    }

    public void setSmsKey(String smsKey) {
        this.smsKey = smsKey;
    }

    public String getSmsUsername() {
        return smsUsername;
    }

    public void setSmsUsername(String smsUsername) {
        this.smsUsername = smsUsername;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public double getSmsBalance() {
        return smsBalance;
    }

    public void setSmsBalance(double smsBalance) {
        this.smsBalance = smsBalance;
    }

    public Double getCostPerSms() {
        return costPerSms;
    }

    public void setCostPerSms(Double costPerSms) {
        this.costPerSms = costPerSms;
    }
}
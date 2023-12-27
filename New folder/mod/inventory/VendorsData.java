package com.walgotech.juditonspringapp.models.inventory;

public class VendorsData {
    private String contactName;
    private String itemGroup;
    private String receivablesFormatted;
    private int id;
    private String company;
    private String email;
    private String phone;
    private String payablesFormatted;
    private Object someValue;

    // Constructors, getters, and setters

    public VendorsData(String contactName, String itemGroup, String receivablesFormatted, int id,
                       String company, String email, String phone, String payablesFormatted, Object someValue) {
        this.contactName = contactName;
        this.itemGroup = itemGroup;
        this.receivablesFormatted = receivablesFormatted;
        this.id = id;
        this.company = company;
        this.email = email;
        this.phone = phone;
        this.payablesFormatted = payablesFormatted;
        this.someValue = someValue;
    }

    // Getters and setters for all fields

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getReceivablesFormatted() {
        return receivablesFormatted;
    }

    public void setReceivablesFormatted(String receivablesFormatted) {
        this.receivablesFormatted = receivablesFormatted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayablesFormatted() {
        return payablesFormatted;
    }

    public void setPayablesFormatted(String payablesFormatted) {
        this.payablesFormatted = payablesFormatted;
    }

    public Object getSomeValue() {
        return someValue;
    }

    public void setSomeValue(Object someValue) {
        this.someValue = someValue;
    }
}


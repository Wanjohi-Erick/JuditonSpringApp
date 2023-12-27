package com.walgotech.juditonspringapp.models.inventory;

import java.util.Date;

public class RequisitionData {
    private int id;
    private String department;
    private String requestedBy;
    private Date requestedOn;
    private String status;
    private double cost;
    private int items;

    public RequisitionData(int id, String department, String requestedBy, Date requestedOn, String status,
                           double cost, int items) {
        this.id = id;
        this.department = department;
        this.requestedBy = requestedBy;
        this.requestedOn = requestedOn;
        this.status = status;
        this.cost = cost;
        this.items = items;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(Date requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}

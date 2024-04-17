package com.rickiey_innovates.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "ItemGroup")
@Table(name = "item_group", schema = "farm", indexes = {
        @Index(name = "group_name", columnList = "group_name", unique = true)
})
public class ItemGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @NotNull
    @Column(name = "farm", nullable = false)
    private Integer farm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getfarm() {
        return farm;
    }

    public void setfarm(Integer farm) {
        this.farm = farm;
    }

}
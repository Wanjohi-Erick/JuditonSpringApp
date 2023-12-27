package com.walgotech.juditonspringapp.models.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "ItemGroup")
@Table(name = "item_group", schema = "church", indexes = {
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
    @Column(name = "church", nullable = false)
    private Integer church;

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

    public Integer getchurch() {
        return church;
    }

    public void setchurch(Integer church) {
        this.church = church;
    }

}
package com.rickiey_innovates.juditonspringapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(name = "phone_number")
    private String phone;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Column(length = 600)
    private String sessionid;

    @Column(columnDefinition = "int(11) DEFAULT '0'")
    private Integer logins;

    @Column(columnDefinition = "int(11) DEFAULT '0'")
    private Integer theme;

    @Column(columnDefinition = "varchar(255) DEFAULT ''")
    private String image;

    @Column(columnDefinition = "varchar(255) DEFAULT ''")
    private String signature;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farm")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farm farm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", logins=" + logins +
                ", theme=" + theme +
                ", image='" + image + '\'' +
                ", signature='" + signature + '\'' +
                ", farm=" + farm +
                ", roles=" + roles +
                '}';
    }
}
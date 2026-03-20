package com.fpt.ecoverse_backend.entities;

import com.fpt.ecoverse_backend.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "parent")
    private List<Student> students;

    @OneToMany(mappedBy = "parent")
    private List<RedemptionRequest> redemptionRequests;
}

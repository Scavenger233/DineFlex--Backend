package com.dineflex.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private String cuisine;

    @Column(length = 1000)
    private String description;

    private String address;

    private String phone;

    private String thumbnailUrl;

    private String openingHours;

    private boolean hasEarlyBird;

    private boolean hasLastMinute;

    // 关联关系：一对多（餐厅 → offers）
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers;

    // 餐厅拥有多个时间段（slot）
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailabilitySlot> availabilitySlots;

    // 餐厅拥有多个预订
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;
}

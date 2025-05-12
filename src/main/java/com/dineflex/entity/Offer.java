package com.dineflex.entity;

import com.dineflex.entity.enums.OfferType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "offer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OfferType type;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalTime availableFrom;

    private LocalTime availableTo;

    private String discount;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private List<AvailabilitySlot> slots;
}
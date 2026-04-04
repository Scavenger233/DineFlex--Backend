package com.dineflex.dto.response;

import com.dineflex.entity.Restaurant;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailResponse {
    private Long id;
    private String name;
    private String description;
    private List<String> images;
    private String address;
    private String phone;
    private String openingHours;
    private String location;
    private String cuisine;
    private String thumbnailUrl;
    private boolean hasEarlyBird;
    private boolean hasLastMinute;

    public static RestaurantDetailResponse fromEntity(Restaurant restaurant) {
        return RestaurantDetailResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .images(List.of(restaurant.getThumbnailUrl()))
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .openingHours(restaurant.getOpeningHours())
                .location(restaurant.getLocation())
                .cuisine(restaurant.getCuisine())
                .thumbnailUrl(restaurant.getThumbnailUrl())
                .hasEarlyBird(restaurant.isHasEarlyBird())
                .hasLastMinute(restaurant.isHasLastMinute())
                .build();
    }
}
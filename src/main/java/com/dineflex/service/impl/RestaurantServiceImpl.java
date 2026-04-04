package com.dineflex.service.impl;

import com.dineflex.dto.response.RestaurantDetailResponse;
import com.dineflex.dto.response.RestaurantListResponse;
import com.dineflex.entity.Restaurant;
import com.dineflex.repository.RestaurantRepository;
import com.dineflex.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantListResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream().map(RestaurantListResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public RestaurantDetailResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return RestaurantDetailResponse.fromEntity(restaurant);
    }

}
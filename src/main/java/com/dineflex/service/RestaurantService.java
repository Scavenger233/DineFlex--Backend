package com.dineflex.service;

import com.dineflex.dto.response.RestaurantDetailResponse;
import com.dineflex.dto.response.RestaurantListResponse;

import java.util.List;

public interface RestaurantService {
    List<RestaurantListResponse> getAllRestaurants();
    RestaurantDetailResponse getRestaurantById(Long id);
}
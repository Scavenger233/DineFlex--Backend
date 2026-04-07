package com.dineflex.service.impl;

import com.dineflex.dto.response.RestaurantDetailResponse;
import com.dineflex.dto.response.RestaurantListResponse;
import com.dineflex.entity.Restaurant;
import com.dineflex.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        restaurant1 = Restaurant.builder()
                .id(1L)
                .name("The Sizzling Grill")
                .location("Dublin")
                .cuisine("Irish")
                .description("Great food")
                .address("1 Main St")
                .phone("+353 1 234 5678")
                .openingHours("09:00-22:00")
                .thumbnailUrl("http://example.com/image1.jpg")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        restaurant2 = Restaurant.builder()
                .id(2L)
                .name("Bella Italia")
                .location("Cork")
                .cuisine("Italian")
                .description("Authentic Italian")
                .address("2 High St")
                .phone("+353 21 234 5678")
                .openingHours("10:00-23:00")
                .thumbnailUrl("http://example.com/image2.jpg")
                .hasEarlyBird(false)
                .hasLastMinute(true)
                .build();
    }

    // ───── getAllRestaurants() ─────

    @Test
    void getAllRestaurants_shouldReturnAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));

        List<RestaurantListResponse> result = restaurantService.getAllRestaurants();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("The Sizzling Grill");
        assertThat(result.get(1).getName()).isEqualTo("Bella Italia");
    }

    @Test
    void getAllRestaurants_shouldReturnEmptyList_whenNoRestaurantsExist() {
        when(restaurantRepository.findAll()).thenReturn(List.of());

        List<RestaurantListResponse> result = restaurantService.getAllRestaurants();

        assertThat(result).isEmpty();
    }

    @Test
    void getAllRestaurants_shouldMapFieldsCorrectly() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1));

        List<RestaurantListResponse> result = restaurantService.getAllRestaurants();

        RestaurantListResponse response = result.get(0);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("The Sizzling Grill");
        assertThat(response.getLocation()).isEqualTo("Dublin");
        assertThat(response.getCuisine()).isEqualTo("Irish");
        assertThat(response.isHasEarlyBird()).isTrue();
        assertThat(response.isHasLastMinute()).isFalse();
    }

    @Test
    void getAllRestaurants_shouldCallRepositoryOnce() {
        when(restaurantRepository.findAll()).thenReturn(List.of());

        restaurantService.getAllRestaurants();

        verify(restaurantRepository, times(1)).findAll();
    }

    // ───── getRestaurantById() ─────

    @Test
    void getRestaurantById_shouldReturnRestaurantDetail_whenFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));

        RestaurantDetailResponse response = restaurantService.getRestaurantById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("The Sizzling Grill");
        assertThat(response.getAddress()).isEqualTo("1 Main St");
        assertThat(response.getPhone()).isEqualTo("+353 1 234 5678");
        assertThat(response.getCuisine()).isEqualTo("Irish");
    }

    @Test
    void getRestaurantById_shouldThrowException_whenNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.getRestaurantById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Restaurant not found");
    }

    @Test
    void getRestaurantById_shouldIncludeThumbnailInImages() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));

        RestaurantDetailResponse response = restaurantService.getRestaurantById(1L);

        assertThat(response.getImages()).contains("http://example.com/image1.jpg");
    }
}
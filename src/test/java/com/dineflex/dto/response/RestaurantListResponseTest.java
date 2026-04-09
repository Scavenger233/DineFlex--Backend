package com.dineflex.dto.response;

import com.dineflex.entity.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantListResponseTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .location("Dublin")
                .cuisine("Irish")
                .thumbnailUrl("http://example.com/image.jpg")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();
    }

    @Test
    void fromEntity_shouldMapAllFields() {
        RestaurantListResponse response = RestaurantListResponse.fromEntity(restaurant);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Restaurant");
        assertThat(response.getLocation()).isEqualTo("Dublin");
        assertThat(response.getCuisine()).isEqualTo("Irish");
        assertThat(response.getThumbnailUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(response.isHasEarlyBird()).isTrue();
        assertThat(response.isHasLastMinute()).isFalse();
    }

    @Test
    void builder_shouldSetAllFields() {
        RestaurantListResponse response = RestaurantListResponse.builder()
                .id(1L)
                .name("Test Restaurant")
                .location("Dublin")
                .cuisine("Irish")
                .thumbnailUrl("http://example.com/image.jpg")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Restaurant");
        assertThat(response.isHasEarlyBird()).isTrue();
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        RestaurantListResponse response = new RestaurantListResponse();
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
    }

    @Test
    void setter_shouldUpdateFields() {
        RestaurantListResponse response = new RestaurantListResponse();
        response.setId(1L);
        response.setName("Updated Restaurant");
        response.setHasEarlyBird(true);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Updated Restaurant");
        assertThat(response.isHasEarlyBird()).isTrue();
    }
}
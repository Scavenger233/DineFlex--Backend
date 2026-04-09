package com.dineflex.dto.response;

import com.dineflex.entity.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantDetailResponseTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .location("Dublin")
                .cuisine("Irish")
                .description("Great food")
                .address("1 Main St")
                .phone("+353 1 234 5678")
                .openingHours("09:00-22:00")
                .thumbnailUrl("http://example.com/image.jpg")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();
    }

    @Test
    void fromEntity_shouldMapAllFields() {
        RestaurantDetailResponse response = RestaurantDetailResponse.fromEntity(restaurant);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Restaurant");
        assertThat(response.getDescription()).isEqualTo("Great food");
        assertThat(response.getAddress()).isEqualTo("1 Main St");
        assertThat(response.getPhone()).isEqualTo("+353 1 234 5678");
        assertThat(response.getOpeningHours()).isEqualTo("09:00-22:00");
        assertThat(response.getLocation()).isEqualTo("Dublin");
        assertThat(response.getCuisine()).isEqualTo("Irish");
        assertThat(response.getThumbnailUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(response.getImages()).contains("http://example.com/image.jpg");
        assertThat(response.isHasEarlyBird()).isTrue();
        assertThat(response.isHasLastMinute()).isFalse();
    }

    @Test
    void builder_shouldSetAllFields() {
        RestaurantDetailResponse response = RestaurantDetailResponse.builder()
                .id(1L)
                .name("Test Restaurant")
                .description("Great food")
                .address("1 Main St")
                .phone("+353 1 234 5678")
                .openingHours("09:00-22:00")
                .location("Dublin")
                .cuisine("Irish")
                .thumbnailUrl("http://example.com/image.jpg")
                .images(List.of("http://example.com/image.jpg"))
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Restaurant");
        assertThat(response.isHasEarlyBird()).isTrue();
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        RestaurantDetailResponse response = new RestaurantDetailResponse();
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
    }

    @Test
    void setter_shouldUpdateFields() {
        RestaurantDetailResponse response = new RestaurantDetailResponse();
        response.setId(1L);
        response.setName("Updated Restaurant");
        response.setHasEarlyBird(true);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Updated Restaurant");
        assertThat(response.isHasEarlyBird()).isTrue();
    }
}
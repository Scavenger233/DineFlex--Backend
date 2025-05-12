package com.dineflex.dto.response;

import com.dineflex.entity.Restaurant;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantListResponse {

    private Long id;
    private String name;
    private String thumbnailUrl;
    private String location;
    private String cuisine;
    private boolean hasEarlyBird;
    private boolean hasLastMinute;

    public static RestaurantListResponse fromEntity(Restaurant restaurant) {

        // 临时 Lombok 构建器测试代码（编译失败则说明 Lombok无效）
        RestaurantListResponse test = RestaurantListResponse.builder()
                .id(999L)
                .name("Test")
                .thumbnailUrl("thumb.jpg")
                .location("Dublin")
                .cuisine("Irish")
                .hasEarlyBird(true)
                .hasLastMinute(false)
                .build();

        return RestaurantListResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .thumbnailUrl(restaurant.getThumbnailUrl())
                .location(restaurant.getLocation())
                .cuisine(restaurant.getCuisine())
                .hasEarlyBird(restaurant.isHasEarlyBird())
                .hasLastMinute(restaurant.isHasLastMinute())
                .build();
    }
}

package com.dineflex.seed;

import com.dineflex.entity.*;
import com.dineflex.entity.enums.OfferType;
import com.dineflex.entity.enums.SlotType;
import com.dineflex.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MockDataLoader implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final OfferRepository offerRepository;
    private final AvailabilitySlotRepository slotRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("🚀 Starting mock data loading...");

        // Clear old data
        slotRepository.deleteAll();
        offerRepository.deleteAll();
        restaurantRepository.deleteAll();

        // Add restaurants
        List<Restaurant> restaurants = List.of(
                Restaurant.builder().name("The Sizzling Grill").location("Dublin").cuisine("Steakhouse")
                        .description("Description for The Sizzling Grill").address("Address for The Sizzling Grill")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(false).build(),
                Restaurant.builder().name("Bella Italia").location("Cork").cuisine("Italian")
                        .description("Description for Bella Italia").address("Address for Bella Italia")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(true).build(),
                Restaurant.builder().name("Sushi Zen").location("Galway").cuisine("Japanese")
                        .description("Description for Sushi Zen").address("Address for Sushi Zen")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(true).build(),
                Restaurant.builder().name("Tapas Bar").location("Limerick").cuisine("Spanish")
                        .description("Description for Tapas Bar").address("Address for Tapas Bar")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1515669097368-22e68427d265?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(false).build(),
                Restaurant.builder().name("The Curry House").location("Waterford").cuisine("Indian")
                        .description("Description for The Curry House").address("Address for The Curry House")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(false).build(),
                Restaurant.builder().name("Fish & Chips Co.").location("Sligo").cuisine("Seafood")
                        .description("Description for Fish & Chips Co.").address("Address for Fish & Chips Co.")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(true).build(),
                Restaurant.builder().name("Burger Palace").location("Killarney").cuisine("American")
                        .description("Description for Burger Palace").address("Address for Burger Palace")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(true).build(),
                Restaurant.builder().name("Pizza Express").location("Dundalk").cuisine("Pizza")
                        .description("Description for Pizza Express").address("Address for Pizza Express")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(false).build(),
                Restaurant.builder().name("Thai Orchid").location("Athlone").cuisine("Thai")
                        .description("Description for Thai Orchid").address("Address for Thai Orchid")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(false).build(),
                Restaurant.builder().name("Mexican Fiesta").location("Wexford").cuisine("Mexican")
                        .description("Description for Mexican Fiesta").address("Address for Mexican Fiesta")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1613514785940-daed07799d9b?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(true).build()
        );

        restaurantRepository.saveAll(restaurants);
        System.out.println("✅ Inserted 10 restaurants");

        // 2️⃣ 插入 Offers 和 Slots
        List<Restaurant> all = restaurantRepository.findAll();
        LocalDate today = LocalDate.now();
        List<LocalTime> times = List.of(
                LocalTime.of(17, 30),
                LocalTime.of(18, 0),
                LocalTime.of(19, 30),
                LocalTime.of(20, 30)
        );

        for (Restaurant restaurant : all) {
            Offer early = null, late = null;

            if (restaurant.isHasEarlyBird()) {
                early = Offer.builder()
                        .restaurant(restaurant)
                        .type(OfferType.EARLY_BIRD)
                        .title("Early Dinner Special")
                        .description("3 courses for €25")
                        .availableFrom(LocalTime.of(17, 0))
                        .availableTo(LocalTime.of(19, 0))
                        .build();
                offerRepository.save(early);
            }

            if (restaurant.isHasLastMinute()) {
                late = Offer.builder()
                        .restaurant(restaurant)
                        .type(OfferType.LAST_MINUTE)
                        .title("Late Night Deal")
                        .description("20% off after 20:30")
                        .availableFrom(LocalTime.of(20, 30))
                        .availableTo(LocalTime.of(22, 0))
                        .discount("20%")
                        .build();
                offerRepository.save(late);
            }

            for (int i = 0; i < 7; i++) {
                LocalDate date = today.plusDays(i);
                for (LocalTime time : times) {
                    SlotType type;
                    Offer offer = null;

                    if (time.isBefore(LocalTime.of(19, 0))) {
                        type = SlotType.EARLY_BIRD;
                        offer = early;
                    } else if (time.isAfter(LocalTime.of(20, 0))) {
                        type = SlotType.LAST_MINUTE;
                        offer = late;
                    } else {
                        type = SlotType.REGULAR;
                    }

                    AvailabilitySlot slot = AvailabilitySlot.builder()
                            .restaurant(restaurant)
                            .date(date)
                            .time(time)
                            .type(type)
                            .offer(offer)
                            .isAvailable(true)
                            .build();
                    slotRepository.save(slot);
                }
            }

            System.out.println("✅ Seeded slots for: " + restaurant.getName());
        }

        System.out.println("🎉 Mock data load completed.");
    }
}
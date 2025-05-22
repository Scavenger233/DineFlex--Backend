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
                        .description("Sizzling steaks and grilled favorites served in a lively setting.")
                        .address("12 Castle Street, Dublin, Ireland")
                        .phone("+353 1 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(false).build(),

                Restaurant.builder().name("Bella Italia").location("Cork").cuisine("Italian")
                        .description("Classic Italian dishes with a modern twist, right in the heart of Cork.")
                        .address("15 Oliver Plunkett Street, Cork, Ireland")
                        .phone("+353 21 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(true).build(),

                Restaurant.builder().name("Sushi Zen").location("Galway").cuisine("Japanese")
                        .description("Fresh sushi and traditional Japanese cuisine in a minimalist setting.")
                        .address("5 Eyre Square, Galway, Ireland")
                        .phone("+353 91 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(true).build(),

                Restaurant.builder().name("Tapas Bar").location("Limerick").cuisine("Spanish")
                        .description("Authentic Spanish tapas and wines in a cozy downtown atmosphere.")
                        .address("22 O'Connell Street, Limerick, Ireland")
                        .phone("+353 61 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1515669097368-22e68427d265?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(false).build(),

                Restaurant.builder().name("The Curry House").location("Waterford").cuisine("Indian")
                        .description("Rich, flavorful Indian curries and tandoori specials in a vibrant setting.")
                        .address("34 John Street, Waterford, Ireland")
                        .phone("+353 51 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(false).build(),

                Restaurant.builder().name("Fish & Chips Co.").location("Sligo").cuisine("Seafood")
                        .description("Freshly caught seafood and traditional fish & chips by the seaside.")
                        .address("3 Rockwood Parade, Sligo, Ireland")
                        .phone("+353 71 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(true).build(),

                Restaurant.builder().name("Burger Palace").location("Killarney").cuisine("American")
                        .description("Gourmet burgers with locally sourced ingredients and bold flavors.")
                        .address("10 Plunkett Street, Killarney, Ireland")
                        .phone("+353 64 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(true).build(),

                Restaurant.builder().name("Pizza Express").location("Dundalk").cuisine("Pizza")
                        .description("Wood-fired pizzas made to order with fresh, authentic toppings.")
                        .address("Park Street, Dundalk, County Louth, Ireland")
                        .phone("+353 42 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(false).hasLastMinute(false).build(),

                Restaurant.builder().name("Thai Orchid").location("Athlone").cuisine("Thai")
                        .description("Elegant Thai dining experience with fragrant spices and exotic dishes.")
                        .address("Church Street, Athlone, Ireland")
                        .phone("+353 90 000 0000")
                        .thumbnailUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop")
                        .openingHours("Mon-Sun: 12:00-22:00").hasEarlyBird(true).hasLastMinute(false).build(),

                Restaurant.builder().name("Mexican Fiesta").location("Wexford").cuisine("Mexican")
                        .description("A lively spot for tacos, burritos, and margaritas with a kick.")
                        .address("The Quay, Wexford, Ireland")
                        .phone("+353 53 000 0000")
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
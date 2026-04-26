package com.dineflex.service.impl;

import com.dineflex.dto.request.BookingRequest;
import com.dineflex.dto.response.BookingResponse;
import com.dineflex.entity.*;
import com.dineflex.entity.enums.BookingStatus;
import com.dineflex.entity.enums.SlotType;
import com.dineflex.exception.UserNotFoundException;
import com.dineflex.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AvailabilitySlotRepository availabilitySlotRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequest bookingRequest;
    private Restaurant restaurant;
    private Customer customer;
    private AvailabilitySlot availableSlot;
    private Booking savedBooking;

    @BeforeEach
    void setUp() {
        bookingRequest = BookingRequest.builder()
                .restaurantId(1L)
                .customerEmail("test@example.com")
                .date("2026-06-01")
                .time("18:00")
                .partySize(2)
                .customerName("Test User")
                .customerPhone("+353 87 123 4567")
                .build();

        restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .hasEarlyBird(false)
                .hasLastMinute(false)
                .build();

        customer = Customer.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .passwordHash("hashed")
                .build();

        availableSlot = AvailabilitySlot.builder()
                .id(1L)
                .date(LocalDate.parse("2026-06-01"))
                .time(LocalTime.parse("18:00"))
                .type(SlotType.REGULAR)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();

        savedBooking = Booking.builder()
                .id(1L)
                .restaurant(restaurant)
                .customer(customer)
                .availableSlot(availableSlot)
                .date(LocalDate.parse("2026-06-01"))
                .time(LocalTime.parse("18:00"))
                .partySize(2)
                .status(BookingStatus.CONFIRMED)
                .confirmationCode("DINE123456")
                .build();
    }

    // ───── createBooking() ─────

    @Test
    void createBooking_shouldReturnBooking_whenAllDataIsValid() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(customerRepository.findByCustomerEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(availabilitySlotRepository.findByRestaurantIdAndDateAndTime(
                1L, LocalDate.parse("2026-06-01"), LocalTime.parse("18:00")))
                .thenReturn(Optional.of(availableSlot));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        Booking result = bookingService.createBooking(bookingRequest);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(result.getPartySize()).isEqualTo(2);
        assertThat(result.getRestaurant().getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void createBooking_shouldMarkSlotAsUnavailable_afterBooking() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(customerRepository.findByCustomerEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(availabilitySlotRepository.findByRestaurantIdAndDateAndTime(
                1L, LocalDate.parse("2026-06-01"), LocalTime.parse("18:00")))
                .thenReturn(Optional.of(availableSlot));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        bookingService.createBooking(bookingRequest);

        assertThat(availableSlot.isAvailable()).isFalse();
        verify(availabilitySlotRepository, times(1)).save(availableSlot);
    }

    @Test
    void createBooking_shouldThrowException_whenRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Restaurant not found");
    }

    @Test
    void createBooking_shouldThrowUserNotFoundException_whenCustomerNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(customerRepository.findByCustomerEmail("test@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Customer not found");
    }

    @Test
    void createBooking_shouldThrowException_whenSlotNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(customerRepository.findByCustomerEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(availabilitySlotRepository.findByRestaurantIdAndDateAndTime(
                1L, LocalDate.parse("2026-06-01"), LocalTime.parse("18:00")))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Slot not found");
    }

    @Test
    void createBooking_shouldThrowException_whenSlotIsNotAvailable() {
        availableSlot.setAvailable(false);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(customerRepository.findByCustomerEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(availabilitySlotRepository.findByRestaurantIdAndDateAndTime(
                1L, LocalDate.parse("2026-06-01"), LocalTime.parse("18:00")))
                .thenReturn(Optional.of(availableSlot));

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Slot not available");
    }

    // ───── getBookingById() ─────

    @Test
    void getBookingById_shouldReturnBookingResponse_whenBookingExists() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(savedBooking));

        BookingResponse response = bookingService.getBookingById(1L, "test@example.com");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(response.getRestaurantName()).isEqualTo("Test Restaurant");
        assertThat(response.getConfirmationCode()).isEqualTo("DINE123456");
    }

    @Test
    void getBookingById_shouldThrowException_whenBookingNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(99L, "test@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking not found");
    }

    @Test
    void getBookingById_shouldThrowException_whenCustomerDoesNotOwnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(savedBooking));

        assertThatThrownBy(() -> bookingService.getBookingById(1L, "other@example.com"))
            .isInstanceOf(AccessDeniedException.class)
            .hasMessage("This booking does not belong to the customer");
    }
}

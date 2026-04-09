package com.dineflex.dto.response;

import com.dineflex.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerInfoResponseTest {

    @Test
    void fromEntity_shouldMapAllFields() {
        Customer customer = Customer.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .passwordHash("hashed")
                .build();

        CustomerInfoResponse response = CustomerInfoResponse.fromEntity(customer);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(response.getPhone()).isEqualTo("+353 87 123 4567");
    }

    @Test
    void builder_shouldSetAllFields() {
        CustomerInfoResponse response = CustomerInfoResponse.builder()
                .id(1L)
                .customerName("Test User")
                .customerEmail("test@example.com")
                .phone("+353 87 123 4567")
                .build();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(response.getPhone()).isEqualTo("+353 87 123 4567");
    }

    @Test
    void setter_shouldUpdateFields() {
        CustomerInfoResponse response = CustomerInfoResponse.builder().build();
        response.setCustomerName("Updated Name");
        response.setCustomerEmail("updated@example.com");

        assertThat(response.getCustomerName()).isEqualTo("Updated Name");
        assertThat(response.getCustomerEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void equals_shouldWorkCorrectly() {
        CustomerInfoResponse r1 = CustomerInfoResponse.builder()
                .id(1L).customerName("Test User").build();

        CustomerInfoResponse r2 = CustomerInfoResponse.builder()
                .id(1L).customerName("Test User").build();

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
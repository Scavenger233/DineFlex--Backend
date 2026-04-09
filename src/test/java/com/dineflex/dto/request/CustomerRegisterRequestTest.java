package com.dineflex.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRegisterRequestTest {

    @Test
    void setter_shouldSetAndGetAllFields() {
        CustomerRegisterRequest request = new CustomerRegisterRequest();
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");
        request.setPhone("+353 87 123 4567");
        request.setPassword("password123");

        assertThat(request.getCustomerName()).isEqualTo("Test User");
        assertThat(request.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(request.getPhone()).isEqualTo("+353 87 123 4567");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    void defaultValues_shouldBeNull() {
        CustomerRegisterRequest request = new CustomerRegisterRequest();

        assertThat(request.getCustomerName()).isNull();
        assertThat(request.getCustomerEmail()).isNull();
        assertThat(request.getPhone()).isNull();
        assertThat(request.getPassword()).isNull();
    }

    @Test
    void equals_shouldWorkCorrectly() {
        CustomerRegisterRequest r1 = new CustomerRegisterRequest();
        r1.setCustomerName("Test User");
        r1.setCustomerEmail("test@example.com");

        CustomerRegisterRequest r2 = new CustomerRegisterRequest();
        r2.setCustomerName("Test User");
        r2.setCustomerEmail("test@example.com");

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void toString_shouldContainFields() {
        CustomerRegisterRequest request = new CustomerRegisterRequest();
        request.setCustomerName("Test User");
        request.setCustomerEmail("test@example.com");

        assertThat(request.toString()).contains("Test User");
        assertThat(request.toString()).contains("test@example.com");
    }
}
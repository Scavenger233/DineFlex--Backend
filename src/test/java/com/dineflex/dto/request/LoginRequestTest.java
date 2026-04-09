package com.dineflex.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void setter_shouldSetAndGetFields() {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("test@example.com");
        request.setPassword("password123");

        assertThat(request.getCustomerEmail()).isEqualTo("test@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    void defaultValues_shouldBeNull() {
        LoginRequest request = new LoginRequest();

        assertThat(request.getCustomerEmail()).isNull();
        assertThat(request.getPassword()).isNull();
    }

    @Test
    void equals_shouldWorkCorrectly() {
        LoginRequest r1 = new LoginRequest();
        r1.setCustomerEmail("test@example.com");
        r1.setPassword("password123");

        LoginRequest r2 = new LoginRequest();
        r2.setCustomerEmail("test@example.com");
        r2.setPassword("password123");

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void toString_shouldContainFields() {
        LoginRequest request = new LoginRequest();
        request.setCustomerEmail("test@example.com");

        assertThat(request.toString()).contains("test@example.com");
    }
}
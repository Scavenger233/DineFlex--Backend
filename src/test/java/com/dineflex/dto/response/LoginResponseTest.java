package com.dineflex.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    void builder_shouldSetAllFields() {
        LoginResponse response = LoginResponse.builder()
                .token("jwt-token")
                .customerName("Test User")
                .customerEmail("test@example.com")
                .build();

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        LoginResponse response = new LoginResponse();
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNull();
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LoginResponse response = new LoginResponse("jwt-token", "Test User", "test@example.com");

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getCustomerName()).isEqualTo("Test User");
        assertThat(response.getCustomerEmail()).isEqualTo("test@example.com");
    }

    @Test
    void setter_shouldUpdateFields() {
        LoginResponse response = new LoginResponse();
        response.setToken("new-token");
        response.setCustomerName("New User");
        response.setCustomerEmail("new@example.com");

        assertThat(response.getToken()).isEqualTo("new-token");
        assertThat(response.getCustomerName()).isEqualTo("New User");
        assertThat(response.getCustomerEmail()).isEqualTo("new@example.com");
    }

    @Test
    void equals_shouldWorkCorrectly() {
        LoginResponse r1 = new LoginResponse("token", "Test User", "test@example.com");
        LoginResponse r2 = new LoginResponse("token", "Test User", "test@example.com");

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
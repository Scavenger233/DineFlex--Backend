package com.dineflex.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String TEST_SECRET = "TestSecretKeyForJwtThatIsLongEnough123456";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", TEST_SECRET);
    }

    // ───── generateToken() ─────

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void generateToken_shouldContainCorrectSubject() {
        String token = jwtUtil.generateToken("test@example.com");
        String extracted = jwtUtil.extractUsername(token);
        assertThat(extracted).isEqualTo("test@example.com");
    }

    @Test
    void generateToken_shouldGenerateDifferentTokensForDifferentUsers() {
        String token1 = jwtUtil.generateToken("user1@example.com");
        String token2 = jwtUtil.generateToken("user2@example.com");
        assertThat(token1).isNotEqualTo(token2);
    }

    // ───── extractUsername() ─────

    @Test
    void extractUsername_shouldReturnCorrectEmail() {
        String token = jwtUtil.generateToken("test@example.com");
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("test@example.com");
    }

    @Test
    void extractUsername_shouldHandleDifferentEmails() {
        String token = jwtUtil.generateToken("another@example.com");
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("another@example.com");
    }

    // ───── isTokenValid() ─────

    @Test
    void isTokenValid_shouldReturnTrue_forValidToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forTamperedToken() {
        String token = jwtUtil.generateToken("test@example.com");
        String tamperedToken = token + "tampered";
        assertThat(jwtUtil.isTokenValid(tamperedToken)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forRandomString() {
        assertThat(jwtUtil.isTokenValid("not.a.valid.token")).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forExpiredToken() {
        Key key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200_000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600_000))
                .signWith(key)
                .compact();

        assertThat(jwtUtil.isTokenValid(expiredToken)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forTokenSignedWithDifferentSecret() {
        Key wrongKey = Keys.hmacShaKeyFor("WrongSecretKeyThatIsDifferentFromTestSecret".getBytes());
        String tokenWithWrongSecret = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .signWith(wrongKey)
                .compact();

        assertThat(jwtUtil.isTokenValid(tokenWithWrongSecret)).isFalse();
    }
}
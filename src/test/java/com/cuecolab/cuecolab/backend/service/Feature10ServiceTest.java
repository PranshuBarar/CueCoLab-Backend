package com.cuecolab.cuecolab.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

class Feature10ServiceTest {
    
    private Feature10Service feature10Service;
    
    @BeforeEach
    void setUp() {
        feature10Service = new Feature10Service();
    }
    
    @Test
    void testTransformText_WithValidText() {
        String result = feature10Service.transformText("HELLO WORLD");
        assertThat(result).isEqualTo("hello world");
    }
    
    @Test
    void testTransformText_WithNull() {
        String result = feature10Service.transformText(null);
        assertThat(result).isNull();
    }
    
    @Test
    void testTransformText_WithWhitespace() {
        String result = feature10Service.transformText("  TEST  ");
        assertThat(result).isEqualTo("test");
    }
    
    @Test
    void testIsValidEmail_WithValidEmail() {
        boolean result = feature10Service.isValidEmail("test@example.com");
        assertThat(result).isTrue();
    }
    
    @Test
    void testIsValidEmail_WithInvalidEmail() {
        boolean result = feature10Service.isValidEmail("invalid");
        assertThat(result).isFalse();
    }
    
    @Test
    void testIsValidEmail_WithNull() {
        boolean result = feature10Service.isValidEmail(null);
        assertThat(result).isFalse();
    }
    
    @Test
    void testIsValidEmail_WithEmptyString() {
        boolean result = feature10Service.isValidEmail("");
        assertThat(result).isFalse();
    }
    
    @Test
    void testMultiply() {
        int result = feature10Service.multiply(4, 5);
        assertThat(result).isEqualTo(20);
    }
    
    @Test
    void testMultiply_WithZero() {
        int result = feature10Service.multiply(10, 0);
        assertThat(result).isEqualTo(0);
    }
}


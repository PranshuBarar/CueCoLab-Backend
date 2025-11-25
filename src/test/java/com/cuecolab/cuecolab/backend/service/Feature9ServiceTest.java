package com.cuecolab.cuecolab.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

class Feature9ServiceTest {
    
    private Feature9Service feature9Service;
    
    @BeforeEach
    void setUp() {
        feature9Service = new Feature9Service();
    }
    
    @Test
    void testProcessData_WithValidInput() {
        String result = feature9Service.processData("hello");
        assertThat(result).isEqualTo("Processed: HELLO");
    }
    
    @Test
    void testProcessData_WithNullInput() {
        String result = feature9Service.processData(null);
        assertThat(result).isEqualTo("Empty input");
    }
    
    @Test
    void testProcessData_WithEmptyInput() {
        String result = feature9Service.processData("");
        assertThat(result).isEqualTo("Empty input");
    }
    
    @Test
    void testCalculateSum() {
        int result = feature9Service.calculateSum(5, 3);
        assertThat(result).isEqualTo(8);
    }
    
    @Test
    void testCalculateSum_WithNegativeNumbers() {
        int result = feature9Service.calculateSum(-5, 10);
        assertThat(result).isEqualTo(5);
    }
}


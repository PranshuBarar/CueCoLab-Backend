package com.cuecolab.cuecolab.backend.service;

import org.springframework.stereotype.Service;

@Service
public class Feature10Service {
    
    // Method that will be tested (covered)
    public String transformText(String text) {
        if (text == null) {
            return null;
        }
        return text.toLowerCase().trim();
    }
    
    // Method that will NOT be tested (uncovered)
    public String notTestedMethod(String input) {
        return "This is not covered";
    }
    
    // Another method that will be tested
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }
    
    // Method that will NOT be tested (uncovered)
    public String formatPhoneNumber(String phone) {
        return "Formatted: " + phone;
    }
    
    // Additional method that will be tested
    public int multiply(int a, int b) {
        return a * b;
    }
}


package com.cuecolab.cuecolab.backend.service;

import org.springframework.stereotype.Service;

@Service
public class Feature9Service {
    
    // Method that will be tested (covered)
    public String processData(String input) {
        if (input == null || input.isEmpty()) {
            return "Empty input";
        }
        return "Processed: " + input.toUpperCase();
    }
    
    // Method that will NOT be tested (uncovered)
    public String unusedMethod(String data) {
        return "This method is not covered by tests";
    }
    
    // Another method that will be tested
    public int calculateSum(int a, int b) {
        return a + b;
    }
    
    // Method that will NOT be tested (uncovered)
    public int unusedCalculation(int x) {
        return x * 2;
    }
}


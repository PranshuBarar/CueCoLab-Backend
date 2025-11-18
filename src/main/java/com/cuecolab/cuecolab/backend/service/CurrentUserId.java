package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.securityConfig.otpbasedloginandsignup.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class CurrentUserId {
    public static UUID getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return UUID.fromString(customUserDetails.getUsername());
        }
        return null;
    }
}

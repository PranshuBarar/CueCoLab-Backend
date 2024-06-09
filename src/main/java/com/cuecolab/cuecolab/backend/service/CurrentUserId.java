package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.securityConfig.googlebasedloginandsignup.YoutubeOAuthController;
import com.cuecolab.cuecolab.backend.securityConfig.otpbasedloginandsignup.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class CurrentUserId {

    private static final Logger logger = LoggerFactory.getLogger(CurrentUserId.class);
    public static UUID getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return UUID.fromString(customUserDetails.getUsername());
        }
        return null;
    }
}

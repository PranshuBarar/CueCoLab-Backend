package com.cuecolab.cuecolab.backend.controller;

import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseLoginDTO;
import com.cuecolab.cuecolab.backend.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/is_logged_in")
    public ResponseEntity<?> isLoggedIn() {
        System.out.println("Request received");
        return ResponseEntity.ok(true);
    }

    @GetMapping("/userdata")
    public ResponseEntity<?> getUserAccountLoginDetails() {
        UserAccountDetailsResponseLoginDTO userAccountDetailsResponseLoginDTO = userServiceImpl.getUserAccountLoginDetails();
        return ResponseEntity.ok(userAccountDetailsResponseLoginDTO);
    }

    @GetMapping("/destinations")
    public ResponseEntity<?> getDestinationList(){
        return ResponseEntity.ok(userServiceImpl.getDestinationList());
    }

    //All these API's should extract userId from JWT via Authentication Principal
    //Changes have to made in all these apis then only will these be used
//
//    @PutMapping("/{email}/upgrade")
//    public ResponseEntity<String> upgradeUser(@PathVariable String email) {
//        return ResponseEntity.ok(userService.userUpgrade(email));
//    }
//
//    @PutMapping("/{email}/downgrade")
//    public ResponseEntity<String> downgradeUser(@PathVariable String email) {
//        return ResponseEntity.ok(userService.userDowngrade(email));
//    }
//
//    @GetMapping("/{email}/subscription")
//    public ResponseEntity<Boolean> getUserSubscriptionStatus(@PathVariable String email) {
//        return ResponseEntity.ok(userService.getUserSubscriptionStatus(email));
//    }
//
//    @DeleteMapping("/{email}")
//    public ResponseEntity<String> deleteUserAccount(@PathVariable String email) {
//        return ResponseEntity.ok(userService.userAccountDelete(email));
//    }
}

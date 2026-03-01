package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.TripService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;
    private final UserRepository userRepository;

    public TripController(TripService tripService, UserRepository userRepository){
        this.tripService=tripService;
        this.userRepository=userRepository;
    }

    @PostMapping
    public Trip savedTrip(@RequestBody Trip trip){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tripService.createTrip(loggedUser.getId(),trip);
    }
}

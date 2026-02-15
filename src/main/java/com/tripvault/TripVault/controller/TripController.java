package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.service.TripService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService){
        this.tripService=tripService;
    }

    @PostMapping("/{userId}")
    public Trip savedTrip(@PathVariable Long userId, @RequestBody Trip trip){
        return tripService.createTrip(userId,trip);
    }
}

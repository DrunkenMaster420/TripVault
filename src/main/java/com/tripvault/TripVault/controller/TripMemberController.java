package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.service.TripMemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trips")
public class TripMemberController {

    private final TripMemberService tripMemberService;
    public TripMemberController(TripMemberService tripMemberService){
        this.tripMemberService=tripMemberService;
    }

    @PostMapping("/{tripId}/")
    public TripMember addMember(@PathVariable Long tripId,@PathVariable Long userId,@RequestBody Long allocatedBytes,@PathVariable Long loggedUserId){
        return tripMemberService.addMember(tripId,userId,allocatedBytes,loggedUserId);
    }


}

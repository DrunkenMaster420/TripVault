package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.model.TripMemberRequest;
import com.tripvault.TripVault.service.TripMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripMemberController {

    private final TripMemberService tripMemberService;
    public TripMemberController(TripMemberService tripMemberService){
        this.tripMemberService=tripMemberService;
    }

    @PostMapping("/{tripId}/members")
    public TripMember addMember(@PathVariable Long tripId, @RequestBody TripMemberRequest request){
        Long loggedUserId=1L;
        return tripMemberService.addMember(tripId, request.getUserId(),request.getAllocatedBytes(),loggedUserId);
    }

    @GetMapping("/{tripId}/members")
    public List<TripMember> getAllMembers(@PathVariable Long tripId){
        return tripMemberService.getMembers(tripId);
    }

    @DeleteMapping("/{tripId}/members/{userId}")
    public void removeMember(@PathVariable Long tripId,@PathVariable Long userId){
        Long loggedUserId=1L;
        tripMemberService.removeMember(tripId,userId,loggedUserId);
    }


}

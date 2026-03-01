package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.dto.TripMemberResponse;
import com.tripvault.TripVault.dto.TripMemberRequest;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.TripMemberService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripMemberController {

    private final TripMemberService tripMemberService;
    private final UserRepository userRepository;
    public TripMemberController(TripMemberService tripMemberService,UserRepository userRepository){
        this.tripMemberService=tripMemberService;
        this.userRepository=userRepository;
    }



    @PostMapping("/{tripId}/members")
    public TripMemberResponse addMember(@PathVariable Long tripId, @RequestBody TripMemberRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long loggedUserId = loggedUser.getId();
        return tripMemberService.addMember(tripId, request.getUserId(),request.getAllocatedBytes(),loggedUserId);
    }

    @GetMapping("/{tripId}/members")
    public List<TripMemberResponse> getAllMembers(@PathVariable Long tripId){
        return tripMemberService.getMembers(tripId);
    }

    @DeleteMapping("/{tripId}/members/{userId}")
    public void removeMember(@PathVariable Long tripId,@PathVariable Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long loggedUserId = loggedUser.getId();
        tripMemberService.removeMember(tripId,userId,loggedUserId);
    }


}

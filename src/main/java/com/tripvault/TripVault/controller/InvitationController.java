package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.dto.InvitationRequest;
import com.tripvault.TripVault.dto.InvitationResponse;
import com.tripvault.TripVault.model.TripInvitation;
import com.tripvault.TripVault.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/trips/{tripId}")
    public ResponseEntity<?> inviteMember(
            @PathVariable Long tripId,
            @RequestBody InvitationRequest request,
            Authentication authentication) {

        System.out.println("Invitation endpoint reached");
        System.out.println(authentication.getName());

        invitationService.inviteMember(
                tripId,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok("Invitation sent.");
    }

    @GetMapping
    public List<InvitationResponse> getMyInvitations(Authentication authentication) {

        return invitationService.getMyInvitations(authentication.getName());
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvitation(
            @PathVariable Long id,
            Authentication authentication) {

        invitationService.acceptInvitation(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok("Invitation accepted.");
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectInvitation(
            @PathVariable Long id,
            Authentication authentication) {

        invitationService.rejectInvitation(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok("Invitation rejected.");
    }

    @GetMapping("/count")
    public long getPendingInvitationCount(Authentication authentication) {

        return invitationService.getPendingInvitationCount(
                authentication.getName()
        );
    }
}
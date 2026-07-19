package com.tripvault.TripVault.service;

import com.tripvault.TripVault.dto.InvitationRequest;
import com.tripvault.TripVault.dto.InvitationResponse;
import com.tripvault.TripVault.model.*;
import com.tripvault.TripVault.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripMemberRepository tripMemberRepository;
    private final TripInvitationRepository tripInvitationRepository;
    private final TripMemberStorageRepository tripMemberStorageRepository;

    public InvitationService(
            UserRepository userRepository,
            TripRepository tripRepository,
            TripMemberRepository tripMemberRepository,
            TripInvitationRepository tripInvitationRepository, TripMemberStorageRepository tripMemberStorageRepository
    ) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.tripMemberRepository = tripMemberRepository;
        this.tripInvitationRepository = tripInvitationRepository;
        this.tripMemberStorageRepository = tripMemberStorageRepository;
    }

    @Transactional
    public void inviteMember(
            Long tripId,
            InvitationRequest request,
            String ownerUsername) {
        System.out.println("Service reached");
        System.out.println("Logged in user = " + ownerUsername);

        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow();

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow();
        System.out.println("Trip owner = " + trip.getCreatedBy().getUsername());

        System.out.println(
                owner.getId() + " == " + trip.getCreatedBy().getId()
        );
        if (!trip.getCreatedBy().getId().equals(owner.getId())) {
            throw new RuntimeException("Only trip owner can invite.");
        }

        User receiver = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (tripMemberRepository.existsByTripAndUser(trip, receiver)) {
            throw new RuntimeException("User already belongs to this trip.");
        }

        if (tripInvitationRepository.existsByTripAndReceiverAndStatus(
                trip,
                receiver,
                InvitationStatus.PENDING)) {

            throw new RuntimeException("Invitation already pending.");
        }

        TripInvitation invitation = new TripInvitation();

        invitation.setTrip(trip);
        invitation.setSender(owner);
        invitation.setReceiver(receiver);
        invitation.setAllocatedStorage(request.getAllocatedStorage());
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setCreatedAt(LocalDateTime.now());

        tripInvitationRepository.save(invitation);
    }

    @Transactional
    public void acceptInvitation(Long invitationId, String username) {

        User receiver = userRepository.findByUsername(username)
                .orElseThrow();

        TripInvitation invitation = tripInvitationRepository
                .findByIdAndReceiver(invitationId, receiver)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRespondedAt(LocalDateTime.now());

        tripInvitationRepository.save(invitation);

        TripMember member = new TripMember();
        member.setTrip(invitation.getTrip());
        member.setUser(receiver);
        member.setRole(TripRole.MEMBER);

        member = tripMemberRepository.save(member);

        TripMemberStorage storage = new TripMemberStorage();
        storage.setTripMember(member);

        long allocatedBytes =
                invitation.getAllocatedStorage() * 1024L * 1024L * 1024L;

        storage.setAllocatedBytes(allocatedBytes);
        storage.setUsedBytes(0L);

        tripMemberStorageRepository.save(storage);
    }

    @Transactional
    public void rejectInvitation(Long invitationId,
                                 String username) {

        User receiver = userRepository.findByUsername(username)
                .orElseThrow();

        TripInvitation invitation = tripInvitationRepository
                .findByIdAndReceiver(invitationId, receiver)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setRespondedAt(LocalDateTime.now());

        tripInvitationRepository.save(invitation);
    }

    public List<InvitationResponse> getMyInvitations(String username) {

        User receiver = userRepository.findByUsername(username)
                .orElseThrow();

        return tripInvitationRepository
                .findByReceiverAndStatus(receiver, InvitationStatus.PENDING)
                .stream()
                .map(invitation -> new InvitationResponse(
                        invitation.getId(),
                        invitation.getTrip().getId(),          // add this
                        invitation.getTrip().getName(),
                        invitation.getSender().getName(),
                        invitation.getAllocatedStorage(),
                        invitation.getCreatedAt()
                ))
                .toList();
    }
    public long getPendingInvitationCount(String username) {

        User receiver = userRepository.findByUsername(username)
                .orElseThrow();

        return tripInvitationRepository.countByReceiverAndStatus(
                receiver,
                InvitationStatus.PENDING
        );
    }
}

package com.tripvault.TripVault.service;

import com.tripvault.TripVault.dto.TripMemberResponse;
import com.tripvault.TripVault.model.*;
import com.tripvault.TripVault.repository.TripMemberRepository;
import com.tripvault.TripVault.repository.TripMemberStorageRepository;
import com.tripvault.TripVault.repository.TripRepository;
import com.tripvault.TripVault.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripMemberService {
    private final TripMemberRepository tripMemberRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMemberStorageRepository tripMemberStorageRepository;

    public TripMemberService(TripMemberRepository tripMemberRepository,
                             TripRepository tripRepository,UserRepository userRepository,
                             TripMemberStorageRepository tripMemberStorageRepository) {
        this.tripMemberRepository = tripMemberRepository;
        this.tripRepository=tripRepository;
        this.userRepository=userRepository;
        this.tripMemberStorageRepository=tripMemberStorageRepository;
    }

    @Transactional
    public TripMemberResponse addMember(Long tripId, Long userId, Long allocatedBytes, Long loggedUserId){

        Trip trip=tripRepository.findById(tripId).orElseThrow(()->new RuntimeException("Trip Not Found"));
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));

        if(tripMemberRepository.findByTrip_IdAndUser_Id(tripId,userId)
                .isPresent()) throw new RuntimeException("Member already present");

        TripMember member=new TripMember();
        member.setTrip(trip);
        member.setUser(user);
        member.setRole(TripRole.MEMBER);

        TripMember saved=tripMemberRepository.save(member);

        TripMemberStorage storage=new TripMemberStorage();
        storage.setTripMemberId(saved.getId());
        storage.setAllocatedBytes(allocatedBytes);
        storage.setUsedBytes(0L);
        tripMemberStorageRepository.save(storage);

        return mapToResponse(saved);
    }

    public List<TripMemberResponse> getMembers(Long tripId){
        return tripMemberRepository.findByTrip_Id(tripId) .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void removeMember(Long tripId,Long userId,Long loggedUserId){
        TripMember owner=tripMemberRepository.findByTrip_IdAndUser_Id(tripId,loggedUserId)
                .orElseThrow(()->new RuntimeException("Not a trip member"));
        if(owner.getRole()!=TripRole.OWNER)throw new RuntimeException("Only owner can remove members");
        TripMember member=tripMemberRepository
                .findByTrip_IdAndUser_Id(tripId,userId)
                .orElseThrow(()->new RuntimeException("Member Not Found"));

        if(member.getUser().getId().equals(loggedUserId))throw new RuntimeException("Owner cannot remove itself");

        tripMemberRepository.delete(member);
    }

    private TripMemberResponse mapToResponse(TripMember member) {

        TripMemberStorage storage = tripMemberStorageRepository
                .findByTripMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("Storage not found"));


        TripMemberResponse response = new TripMemberResponse();

        response.setUserId(member.getUser().getId());
        response.setUsername(member.getUser().getUsername());
        response.setName(member.getUser().getName());
        response.setRole(member.getRole().name());
        response.setJoinedAt(member.getJoinedAt());
        response.setIsActive(member.getIsActive());
        response.setAllocatedBytes(storage.getAllocatedBytes());
        response.setUsedBytes(storage.getUsedBytes());

        return response;
    }



}

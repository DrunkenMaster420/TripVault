package com.tripvault.TripVault.service;

import com.tripvault.TripVault.dto.TripMemberResponse;
import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.model.TripRole;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.TripMemberRepository;
import com.tripvault.TripVault.repository.TripRepository;
import com.tripvault.TripVault.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripMemberService {
    private final TripMemberRepository tripMemberRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripMemberService(TripMemberRepository tripMemberRepository,
                             TripRepository tripRepository,UserRepository userRepository) {
        this.tripMemberRepository = tripMemberRepository;
        this.tripRepository=tripRepository;
        this.userRepository=userRepository;
    }

    public TripMemberResponse addMember(Long tripId, Long userId, Long allocatedBytes, Long loggedUserId){
        TripMember owner=tripMemberRepository.findByTrip_IdAndUser_Id(tripId,loggedUserId)
                .orElseThrow(()->new RuntimeException("Not a trip member"));
        if(owner.getRole()!=TripRole.OWNER)throw new RuntimeException("Only owner can add members");
        Trip trip=tripRepository.findById(tripId).orElseThrow(()->new RuntimeException("Trip Not Found"));
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));

        if(tripMemberRepository.findByTrip_IdAndUser_Id(tripId,userId)
                .isPresent()) throw new RuntimeException("Member already present");

        TripMember member=new TripMember();
        member.setTrip(trip);
        member.setUser(user);
        member.setRole(TripRole.MEMBER);
        member.setAllocatedBytes(allocatedBytes);
        member.setUsedBytes(0L);

        TripMember saved=tripMemberRepository.save(member);
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
        TripMemberResponse response = new TripMemberResponse();

        response.setUserId(member.getUser().getId());
        response.setUsername(member.getUser().getUsername());
        response.setName(member.getUser().getName());

        response.setRole(member.getRole().name());
        response.setAllocatedBytes(member.getAllocatedBytes());
        response.setUsedBytes(member.getUsedBytes());
        response.setJoinedAt(member.getJoinedAt());
        response.setIsActive(member.getIsActive());

        return response;
    }



}

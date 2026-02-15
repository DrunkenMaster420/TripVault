package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.model.TripRole;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.TripMemberRepository;
import com.tripvault.TripVault.repository.TripRepository;
import com.tripvault.TripVault.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMemberRepository tripMemberRepository;

    public TripService(TripRepository tripRepository,UserRepository userRepository,TripMemberRepository tripMemberRepository){
        this.tripRepository=tripRepository;
        this.userRepository=userRepository;
        this.tripMemberRepository=tripMemberRepository;
    }


    public Trip createTrip(Long userId,Trip trip){
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("No User Found"));

        trip.setCreatedBy(user);

        Trip savedTrip=tripRepository.save(trip);

        TripMember owner=new TripMember();
        owner.setTrip(savedTrip);
        owner.setUser(user);
        owner.setRole(TripRole.OWNER);
        owner.setAllocatedBytes(0L);
        owner.setUsedBytes(0L);

        tripMemberRepository.save(owner);

        return savedTrip;
    }

}

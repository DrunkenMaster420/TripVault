package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.*;
import com.tripvault.TripVault.repository.TripMemberRepository;
import com.tripvault.TripVault.repository.TripMemberStorageRepository;
import com.tripvault.TripVault.repository.TripRepository;
import com.tripvault.TripVault.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMemberRepository tripMemberRepository;
    private final TripMemberStorageRepository tripMemberStorageRepository;

    public TripService(TripRepository tripRepository,UserRepository userRepository,TripMemberRepository tripMemberRepository, TripMemberStorageRepository tripMemberStorageRepository){
        this.tripRepository=tripRepository;
        this.userRepository=userRepository;
        this.tripMemberRepository=tripMemberRepository;
        this.tripMemberStorageRepository=tripMemberStorageRepository;
    }


    public Trip createTrip(Long userId, Trip trip) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No User Found"));

        trip.setCreatedBy(user);

        Trip savedTrip = tripRepository.save(trip);

        TripMember owner = new TripMember();
        owner.setTrip(savedTrip);
        owner.setUser(user);
        owner.setRole(TripRole.OWNER);

        owner = tripMemberRepository.save(owner);

        TripMemberStorage storage = new TripMemberStorage();
        storage.setTripMember(owner);
        storage.setAllocatedBytes(10L * 1024L * 1024L * 1024L); // 10 GB
        storage.setUsedBytes(0L);

        tripMemberStorageRepository.save(storage);

        return savedTrip;
    }

    public List<Trip> getTripsByUser(Long userId) {
        return tripRepository.findByCreatedBy_Id(userId);
    }

    public List<Trip> getTripsForUser(Long userId) {



        List<TripMember> memberships =
                tripMemberRepository.findByUser_IdAndIsActiveTrue(userId);


        System.out.println("Membership count: " + memberships.size());


        return memberships.stream()
                .map(TripMember::getTrip)
                .toList();
    }

    public Trip getTripById(Long userId, Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        boolean isMember =
                tripMemberRepository.existsByTrip_IdAndUser_IdAndIsActiveTrue(
                        tripId,
                        userId
                );

        if (!isMember) {
            throw new RuntimeException("You are not a member of this trip");
        }

        return trip;
    }
}

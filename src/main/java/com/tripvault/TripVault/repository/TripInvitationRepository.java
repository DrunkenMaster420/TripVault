package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.InvitationStatus;
import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.TripInvitation;
import com.tripvault.TripVault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripInvitationRepository extends JpaRepository<TripInvitation, Long> {

    List<TripInvitation> findByReceiverAndStatus(
            User receiver,
            InvitationStatus status
    );

    Optional<TripInvitation> findByIdAndReceiver(
            Long id,
            User receiver
    );

    boolean existsByTripAndReceiverAndStatus(
            Trip trip,
            User receiver,
            InvitationStatus status
    );

    long countByReceiverAndStatus(
            User receiver,
            InvitationStatus status
    );
}

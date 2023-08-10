package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<ParticipationRequest> findByRequesterId(Long userId);

    Collection<ParticipationRequest> findByEventId(Long eventId);

    @Query("select count(p.id) from ParticipationRequest p " +
            "where p.event.id = :eventId " +
            "and p.status = :status " +
            "group by p.id")
    Optional<Integer> findConfirmedRequest(@Param("eventId") Long eventId,
                                           @Param("status") ParticipationRequestStatus status);
}

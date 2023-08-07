package ru.practicum.explorewithme.request;

import java.util.Collection;

public interface ParticipationRequestService {
    Collection<ParticipationRequestDto> findParticipationRequests(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}

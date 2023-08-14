package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.State;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.request.ParticipationRequestStatus.CONFIRMED;


@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Collection<ParticipationRequestDto> findParticipationRequests(Long userId) {
        return participationRequestRepository
                .findByRequesterId(userId)
                .stream()
                .map(mapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        if (participationRequestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(String.format("Participation request with user id: %s and event id: %s "
                    + "already exists.", userId, eventId));
        }

        if (eventRepository.findByInitiatorIdAndId(userId, eventId).isPresent()) {
            throw new ConflictException("Participation request from event's initiator couldn't be added.");
        }

        if (eventRepository.findByStateAndId(State.PUBLISHED, eventId).isEmpty()) {
            throw new ConflictException("Event must be published first.");
        }

        final Event event = eventRepository.getReferenceById(eventId);
        final Optional<Integer> confirmedRequest = participationRequestRepository
                .findConfirmedRequest(eventId, CONFIRMED);
        if (confirmedRequest.isPresent() && confirmedRequest.get() != 0 && confirmedRequest.get()
                .equals(event.getParticipantLimit())) {
            throw new ConflictException("Participation limit");
        }

        final ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setRequester(userRepository.getReferenceById(userId));

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
        } else {
            participationRequest.setStatus(ParticipationRequestStatus.PENDING);
        }

        participationRequest.setEvent(event);
        return mapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        final Optional<ParticipationRequest> byId = participationRequestRepository.findById(requestId);
        if (byId.isPresent()) {
            byId.get().setStatus(ParticipationRequestStatus.CANCELED);
            return mapper.toParticipationRequestDto(byId.get());
        } else {
            throw new EntityNotFoundException(ParticipationRequest.class,
                    String.format("Entity with id=%d doesn't exist.", requestId));
        }
    }
}

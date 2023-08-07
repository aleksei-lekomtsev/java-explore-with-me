package ru.practicum.explorewithme.event;


import ru.practicum.explorewithme.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    Collection<EventShortDto> findEvents(Long userId, int from, int size);

    EventFullDto create(Long userId, NewEventDto dto);

    EventFullDto findEvent(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    Collection<ParticipationRequestDto> findEventParticipants(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    Collection<EventFullDto> findEvents(List<Long> usersIds, List<State> states, List<Long> categoriesIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Collection<EventShortDto> findEvents(String text, List<Long> categoriesIds, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, int from, int size,
                                         HttpServletRequest request);

    EventFullDto findEvent(Long eventId, HttpServletRequest request);

}

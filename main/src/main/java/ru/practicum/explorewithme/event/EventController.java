package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.explorewithme.Util.DATETIME_PATTERN;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> findEvents(@PathVariable Long userId,
                                                @RequestParam(name = "from", defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return service.findEvents(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId, @RequestBody @Validated NewEventDto dto) {
        return service.create(userId, dto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.findEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @RequestBody @Validated UpdateEventUserRequest updateEventUserRequest) {
        return service.update(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> findEventParticipants(@PathVariable Long userId,
                                                                     @PathVariable Long eventId) {
        return service.findEventParticipants(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId, @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest
                                                                      eventRequestStatusUpdateRequest) {
        return service.changeRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventFullDto> findEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                               @RequestParam(name = "states", required = false) List<State> states,
                                               @RequestParam(name = "categories", required = false)
                                               List<Long> categories,
                                               @RequestParam(name = "rangeStart", required = false)
                                               @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd", required = false)
                                               @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeEnd,
                                               @RequestParam(name = "from", defaultValue = "0")
                                               @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        return service.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody @Validated UpdateEventAdminRequest updateEventAdminRequest) {
        return service.update(eventId, updateEventAdminRequest);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> findEvents(@RequestParam(name = "text", required = false) String text,
                                                @RequestParam(name = "categories", required = false) List<Long>
                                                        categories,
                                                @RequestParam(name = "paid", required = false) Boolean paid,
                                                @RequestParam(name = "rangeStart", required = false)
                                                @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeStart,
                                                @RequestParam(name = "rangeEnd", required = false)
                                                @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime rangeEnd,
                                                @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                                Boolean onlyAvailable,
                                                @RequestParam(name = "sort", required = false) Sort sort,
                                                @RequestParam(name = "from", defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive int size,
                                                HttpServletRequest request) {
        return service.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request);
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEvent(@PathVariable Long eventId, HttpServletRequest request) {
        return service.findEvent(eventId, request);
    }
}


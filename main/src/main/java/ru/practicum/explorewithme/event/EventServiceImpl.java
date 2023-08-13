package ru.practicum.explorewithme.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.exception.BadInputDataException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.hit.HitDto;
import ru.practicum.explorewithme.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.ParticipationRequest;
import ru.practicum.explorewithme.request.ParticipationRequestDto;
import ru.practicum.explorewithme.request.ParticipationRequestMapper;
import ru.practicum.explorewithme.request.ParticipationRequestRepository;
import ru.practicum.explorewithme.stat.StatClient;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.util.EntityPageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.Util.DATETIME_PATTERN;
import static ru.practicum.explorewithme.Util.MIN_DATE;
import static ru.practicum.explorewithme.request.ParticipationRequestStatus.CONFIRMED;
import static ru.practicum.explorewithme.request.ParticipationRequestStatus.REJECTED;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventMapper mapper;
    private final ParticipationRequestMapper participationRequestMapper;
    private final LocationMapper locationMapper;
    private final StatClient statClient;

    private static final String EXPLROREWITHMEMAIN_APP = "explorewithmemain";

    @PersistenceContext
    private final EntityManager entityManager;

    private void createAndSendHit(HttpServletRequest request) {
        final HitDto hitDto = HitDto
                .builder()
                .app(EXPLROREWITHMEMAIN_APP)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statClient.createHit(hitDto);
    }

    private Integer findViews(HttpServletRequest request) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        final ResponseEntity<Object> stats = statClient.findStats(MIN_DATE.format(formatter),
                LocalDateTime.now().format(formatter), request.getRequestURI(), true);
        return (Integer) ((LinkedHashMap) ((ArrayList) stats.getBody()).get(0)).get("hits");
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> findEvents(Long userId, int from, int size) {
        final PageRequest pageRequest = new EntityPageRequest(from, size);

        return eventRepository.findByInitiatorId(userId, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto dto) {
        final Event event = mapper.toEvent(dto);
        event.setInitiator(userRepository.getReferenceById(userId));
        return mapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto findEvent(Long userId, Long eventId) {
        return mapper.toEventFullDto(eventRepository.findById(eventId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Event.class,
                            String.format("Entity with id=%d doesn't exist.", eventId));
                }));
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Event.class,
                            String.format("Entity with id=%d doesn't exist.", eventId));
                });

        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException(String.format("Event with event id: %s is published.", eventId));
        }

        if (updateEventUserRequest.getAnnotation() != null && !updateEventUserRequest.getAnnotation()
                .equals(event.getAnnotation()) && !updateEventUserRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        final Long categoryId = updateEventUserRequest.getCategory();
        if (categoryId != null) {
            final Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> {
                        throw new EntityNotFoundException(Category.class,
                                String.format("Entity with id=%d doesn't exist.", categoryId));
                    });
            event.setCategory(category);
        }

        if (updateEventUserRequest.getDescription() != null && !updateEventUserRequest.getDescription()
                .equals(event.getDescription()) && !updateEventUserRequest.getDescription().isBlank()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getStateAction() != null) {
            switch (StateAction.valueOf(updateEventUserRequest.getStateAction())) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }

        if (updateEventUserRequest.getTitle() != null && !updateEventUserRequest.getTitle()
                .equals(event.getTitle()) && !updateEventUserRequest.getTitle().isBlank()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        return mapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ParticipationRequestDto> findEventParticipants(Long userId, Long eventId) {
        return requestRepository
                .findByEventId(eventId)
                .stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest
                                                                      eventRequestStatusUpdateRequest) {
        final Optional<Integer> confirmedRequest = requestRepository.findConfirmedRequest(eventId, CONFIRMED);
        if (confirmedRequest.isPresent() && confirmedRequest.get() != 0 && confirmedRequest.get()
                .equals(eventRepository.getReferenceById(eventId).getParticipantLimit())
                && eventRequestStatusUpdateRequest.getStatus() != REJECTED) {
            throw new ConflictException("Participation limit");
        }

        final List<ParticipationRequest> allById
                = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        if (!allById.isEmpty() && allById.get(0).getStatus() == CONFIRMED
                && eventRequestStatusUpdateRequest.getStatus() == REJECTED) {
            throw new ConflictException("Couldn't reject confirmed request");
        }

        allById.forEach(participationRequest ->
                participationRequest.setStatus(eventRequestStatusUpdateRequest.getStatus()));

        final List<ParticipationRequestDto> result = allById.stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        final EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        switch (eventRequestStatusUpdateRequest.getStatus()) {
            case CONFIRMED:
                eventRequestStatusUpdateResult.setConfirmedRequests(result);
                break;
            case REJECTED:
                eventRequestStatusUpdateResult.setRejectedRequests(result);
                break;
        }
        return eventRequestStatusUpdateResult;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<EventFullDto> findEvents(List<Long> usersIds, List<State> states, List<Long> categoriesIds,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        final QEvent event = QEvent.event;

        final BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (usersIds != null && !usersIds.isEmpty()) {
            booleanBuilder.and(event.initiator.id.in(usersIds));
        }

        if (states != null && !states.isEmpty()) {
            booleanBuilder.and(event.state.in(states));
        }

        if (categoriesIds != null && !categoriesIds.isEmpty()) {
            booleanBuilder.and(event.category.id.in(categoriesIds));
        }

        if (rangeStart != null) {
            booleanBuilder.and(event.eventDate.between(rangeStart, rangeEnd));
        }

        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        final List<Event> events = queryFactory
                .selectFrom(event)
                .where(booleanBuilder)
                .offset(from)
                .limit(size)
                .fetch();

        return events
                .stream()
                .map(e -> {
                    EventFullDto eventFullDto = mapper.toEventFullDto(e);
                    Optional<Integer> confirmedRequest = requestRepository.findConfirmedRequest(e.getId(),
                            CONFIRMED);
                    confirmedRequest.ifPresent(eventFullDto::setConfirmedRequests);
                    return eventFullDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Event.class,
                            String.format("Entity with id=%d doesn't exist.", eventId));
                });

        if (updateEventAdminRequest.getAnnotation() != null && !updateEventAdminRequest.getAnnotation()
                .equals(event.getAnnotation()) && !updateEventAdminRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        final Long categoryId = updateEventAdminRequest.getCategory();
        if (categoryId != null) {
            final Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> {
                        throw new EntityNotFoundException(Category.class,
                                String.format("Entity with id=%d doesn't exist.", categoryId));
                    });
            event.setCategory(category);
        }

        if (updateEventAdminRequest.getDescription() != null && !updateEventAdminRequest.getDescription()
                .equals(event.getDescription()) && !updateEventAdminRequest.getDescription().isBlank()) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(locationMapper.toLocation(updateEventAdminRequest.getLocation()));
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            switch (event.getState()) {
                case PUBLISHED:
                    throw new ConflictException(String.format("Event with event id: %s is published.", eventId));
                case CANCELED:
                    throw new ConflictException(String.format("Event with event id: %s is canceled.", eventId));
            }

            switch (StateAction.valueOf(updateEventAdminRequest.getStateAction())) {
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
            }
        }

        if (updateEventAdminRequest.getTitle() != null && !updateEventAdminRequest.getTitle()
                .equals(event.getTitle()) && !updateEventAdminRequest.getTitle().isBlank()) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        return mapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> findEvents(String text, List<Long> categoriesIds, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Boolean onlyAvailable, Sort sort, int from, int size,
                                                HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadInputDataException("Range end must be after range start");
        }

        final QEvent event = QEvent.event;

        final BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (text != null && !text.isBlank()) {
            booleanBuilder.and(event.annotation.likeIgnoreCase("%" + text + "%"));
        }

        if (categoriesIds != null && !categoriesIds.isEmpty()) {
            booleanBuilder.and(event.category.id.in(categoriesIds));
        }

        if (paid != null) {
            booleanBuilder.and(event.paid.eq(paid));
        }

        final JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);

        final List<Event> events = queryFactory
                .selectFrom(event)
                .where(booleanBuilder)
                .offset(from)
                .limit(size)
                .fetch();

        createAndSendHit(request);

        return events
                .stream()
                .map(e -> {
                    EventShortDto eventShortDto = mapper.toEventShortDto(e);
                    Optional<Integer> confirmedRequest = requestRepository.findConfirmedRequest(e.getId(),
                            CONFIRMED);
                    confirmedRequest.ifPresent(eventShortDto::setConfirmedRequests);
                    return eventShortDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto findEvent(Long eventId, HttpServletRequest request) {
        createAndSendHit(request);

        final EventFullDto eventFullDto = mapper
                .toEventFullDto(eventRepository.findByIdAndState(eventId, State.PUBLISHED).orElseThrow(
                        () -> {
                            throw new EntityNotFoundException(Event.class,
                                    String.format("Entity with id=%d doesn't exist.", eventId));
                        }));
        eventFullDto.setViews(findViews(request));

        return eventFullDto;
    }
}

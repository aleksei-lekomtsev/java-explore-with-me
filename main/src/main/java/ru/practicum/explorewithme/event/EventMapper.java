package ru.practicum.explorewithme.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(source = "category", target = "category.id")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "state", expression = "java(State.PENDING)")
    Event toEvent(NewEventDto dto);

    @Mapping(source = "category.id", target = "category")
    NewEventDto toNewEventDto(Event event);

    Event toEvent(EventShortDto dto);

    EventShortDto toEventShortDto(Event event);

    Event toEvent(EventFullDto dto);

    @Mapping(target = "confirmedRequests", expression = "java(0)")
    EventFullDto toEventFullDto(Event event);
}

package ru.practicum.explorewithme.event;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toLocation(LocationDto dto);

    LocationDto toLocationDto(Location location);
}

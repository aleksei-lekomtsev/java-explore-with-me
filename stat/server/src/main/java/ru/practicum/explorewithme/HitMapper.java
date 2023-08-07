package ru.practicum.explorewithme;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.hit.HitDto;

@Mapper(componentModel = "spring")
public interface HitMapper {
    @Mapping(source = "timestamp", target = "created")
    Hit toHit(HitDto dto);

    @Mapping(source = "created", target = "timestamp")
    HitDto toDto(Hit hit);
}

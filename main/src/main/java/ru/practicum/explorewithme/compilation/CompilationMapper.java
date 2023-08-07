package ru.practicum.explorewithme.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto dto);

    CompilationDto toCompilationDto(Compilation compilation);
}

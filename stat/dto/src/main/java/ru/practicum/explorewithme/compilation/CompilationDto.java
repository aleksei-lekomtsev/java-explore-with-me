package ru.practicum.explorewithme.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.EventShortDto;

import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Collection<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}

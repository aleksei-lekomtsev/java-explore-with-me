package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exception.EntityNotFoundException;
import ru.practicum.explorewithme.util.EntityPageRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Collection<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        PageRequest pageRequest = new EntityPageRequest(from, size);

        return compilationRepository.findByPinned(pinned, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto findCompilation(Long compId) {
        return mapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Compilation.class,
                            String.format("Entity with id=%d doesn't exist.", compId));
                }));
    }

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto dto) {
        Compilation compilation = mapper.toCompilation(dto);
        if (dto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(dto.getEvents()));
        }
        return mapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        compilationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(Compilation.class,
                            String.format("Entity with id=%d doesn't exist.", compId));
                });

        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            compilation.setEvents(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle()
                .equals(compilation.getTitle()) && !updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        return mapper.toCompilationDto(compilation);
    }
}

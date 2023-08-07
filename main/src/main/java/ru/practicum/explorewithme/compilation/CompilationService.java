package ru.practicum.explorewithme.compilation;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationDto> findCompilations(Boolean pinned, int from, int size);

    CompilationDto findCompilation(Long compId);

    CompilationDto create(NewCompilationDto dto);

    void delete(Long id);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}

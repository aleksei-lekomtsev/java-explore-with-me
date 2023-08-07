package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService service;

    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CompilationDto> findCompilations(@RequestParam(name = "pinned", defaultValue = "false")
                                                       Boolean pinned,
                                                       @RequestParam(name = "from", defaultValue = "0")
                                                       @PositiveOrZero int from,
                                                       @RequestParam(name = "size", defaultValue = "10")
                                                       @Positive int size) {
        return service.findCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto findCompilations(@PathVariable Long compId) {
        return service.findCompilation(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Validated NewCompilationDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        service.delete(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId, @RequestBody @Validated UpdateCompilationRequest dto) {
        return service.update(compId, dto);
    }
}

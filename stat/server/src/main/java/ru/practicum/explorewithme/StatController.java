package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.Util.DATETIME_PATTERN;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody HitDto dto) {
        service.create(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> findStats(@RequestParam(name = "start") @DateTimeFormat(pattern = DATETIME_PATTERN)
                                        LocalDateTime start,
                                        @RequestParam(name = "end") @DateTimeFormat(pattern = DATETIME_PATTERN)
                                        LocalDateTime end,
                                        @RequestParam(name = "uris", defaultValue = "") String[] uris,
                                        @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return service.findStats(start, end, uris, unique);
    }
}

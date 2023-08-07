package ru.practicum.explorewithme;

import ru.practicum.explorewithme.hit.HitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void create(HitDto dto);

    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);

}

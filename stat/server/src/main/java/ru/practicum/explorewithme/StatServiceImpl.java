package ru.practicum.explorewithme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.hit.HitDto;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final HitMapper hitMapper;

    @Transactional
    @Override
    public void create(HitDto dto) {
        repository.save(hitMapper.toHit(dto));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        if (uris.length == 0) {
            return unique
                    ? repository.viewStatsByStartAndEndAndUnique(start, end)
                    : repository.viewStatsByStartAndEnd(start, end);
        } else {
            return unique
                    ? repository.viewStatsByStartAndEndAndUniqueAndByUris(start, end, uris)
                    : repository.viewStatsByStartAndEndAndByUris(start, end, uris);
        }
    }
}

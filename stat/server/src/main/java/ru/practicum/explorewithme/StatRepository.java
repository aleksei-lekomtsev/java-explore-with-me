package ru.practicum.explorewithme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.explorewithme.ViewStatsDto(h.app, h.uri, count(h.id)) " +
            "from Hit h " +
            "where h.created >= :start " +
            "and h.created <= :end " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc")
    List<ViewStatsDto> viewStatsByStartAndEnd(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.created >= :start " +
            "and h.created <= :end " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<ViewStatsDto> viewStatsByStartAndEndAndUnique(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.ViewStatsDto(h.app, h.uri, count(h.id)) " +
            "from Hit h " +
            "where h.created >= :start " +
            "and h.created <= :end " +
            "and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc")
    List<ViewStatsDto> viewStatsByStartAndEndAndByUris(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end,
                                                       @Param("uris") String[] uris);

    @Query("select new ru.practicum.explorewithme.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.created >= :start " +
            "and h.created <= :end " +
            "and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<ViewStatsDto> viewStatsByStartAndEndAndUniqueAndByUris(@Param("start") LocalDateTime start,
                                                                @Param("end") LocalDateTime end,
                                                                @Param("uris") String[] uris);
}

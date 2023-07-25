package ru.practicum.explorewithme.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.client.BaseClient;

import java.util.Map;

public class StatClient extends BaseClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public StatClient(@Value("${explorewithme-stat-server}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createHit(HitDto dto) {
        return post("hit", dto);
    }

    public ResponseEntity<Object> findStats(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("stats?start={start}&end={end}&uris={uris}&unique={unique}", null, parameters);
    }
}

package ru.practicum.explorewithme.hit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.Util.DATETIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitDto {
    private Long id;

    @NotBlank(message = "App must not be null and must contain at least one non-whitespace character.",
            groups = EndpointHitCreateBasicInfo.class)
    private String app;

    @NotBlank(message = "URI must not be null and must contain at least one non-whitespace character.",
            groups = EndpointHitCreateBasicInfo.class)
    private String uri;

    @NotBlank(message = "IP must not be null and must contain at least one non-whitespace character.",
            groups = EndpointHitCreateBasicInfo.class)
    private String ip;

    @DateTimeFormat(pattern = DATETIME_PATTERN)
    @JsonFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime timestamp;
}

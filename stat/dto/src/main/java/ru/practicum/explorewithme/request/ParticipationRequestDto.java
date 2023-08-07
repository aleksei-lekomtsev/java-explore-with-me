package ru.practicum.explorewithme.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.Util.DATETIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    @DateTimeFormat(pattern = DATETIME_PATTERN)
    @JsonFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private String status;
}

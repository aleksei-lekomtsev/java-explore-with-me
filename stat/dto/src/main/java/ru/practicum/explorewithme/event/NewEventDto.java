package ru.practicum.explorewithme.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.Util.DATETIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    private Long id;

    @NotBlank(message = "Annotation must not be null and must contain at least one non-whitespace character.")
    @Size(min = 20, max = 2000, message = "Size of the annotation must be higher or equal 20 and lower or equal 2000.")
    private String annotation;

    @NotNull(message = "Category must not be null")
    private Long category;

    @NotBlank(message = "Description must not be null and must contain at least one non-whitespace character.")
    @Size(min = 20, max = 7000, message = "Size of the description must be higher or equal 20 and lower or equal 7000.")
    private String description;

    @DateTimeFormat(pattern = DATETIME_PATTERN)
    @JsonFormat(pattern = DATETIME_PATTERN)
    @FutureOrPresent
    private LocalDateTime eventDate;

    @NotNull(message = "Location must not be null")
    private LocationDto location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank(message = "Title must not be null and must contain at least one non-whitespace character.")
    @Size(min = 3, max = 120, message = "Size of the title must be higher or equal 20 and lower or equal 7000.")
    private String title;
}

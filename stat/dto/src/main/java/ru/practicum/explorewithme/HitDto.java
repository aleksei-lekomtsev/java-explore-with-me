package ru.practicum.explorewithme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private String timestamp;
}

package ru.practicum.explorewithme.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank(message = "Text must not be null and must contain at least one non-whitespace character.")
    @Size(max = 100, message = "Size of the text must be lower or equal 100.")
    private String text;
}

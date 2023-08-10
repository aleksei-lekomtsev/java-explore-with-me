package ru.practicum.explorewithme.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Name must not be null and must contain at least one non-whitespace character.",
            groups = CategoryCreateBasicInfo.class)
    @Size(max = 50, message = "Size of the name must be lower or equal 50.",
            groups = CategoryCreateBasicInfo.class)
    private String name;
}

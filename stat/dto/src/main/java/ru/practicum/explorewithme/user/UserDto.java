package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Name must not be null and must contain at least one non-whitespace character.",
            groups = UserCreateBasicInfo.class)
    @Size(min = 2, max = 250, message = "Size of the name must be higher or equal to 2 and lower or equal 250.",
            groups = UserCreateBasicInfo.class)
    private String name;

    @NotBlank(message = "Email must not be null and must contain at least one non-whitespace character.",
            groups = UserCreateBasicInfo.class)
    @Email(message = "Email must be well-formed.", groups = UserCreateBasicInfo.class)
    @Size(min = 6, max = 254, message = "Size of the email must be higher or equal to 6 and lower or equal 254.",
            groups = UserCreateBasicInfo.class)
    private String email;
}

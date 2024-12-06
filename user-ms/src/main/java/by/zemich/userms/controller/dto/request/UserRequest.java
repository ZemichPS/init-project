package by.zemich.userms.controller.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequest {

    @NotNull(message = "The ID cannot be null")
    private UUID id;

    @Email(message = "The email must be correct")
    @NotBlank(message = "The email cannot be empty")
    private String email;

    @NotBlank(message = "The name cannot be empty")
    @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters long")
    private String firstName;

    @NotBlank(message = "The last name cannot be empty")
    @Size(min = 2, max = 50, message = "The last name must be between 2 and 50 characters long")
    private String lastName;

    @NotBlank(message = "The password cannot be empty")
    @Size(min = 8, max = 100, message = "The password must be between 8 and 100 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "The password must contain at least one digit, one uppercase letter, one lowercase letter and one special character"
    )
    private String password;

    @NotNull(message = "The role cannot be null")
    private String role;
}
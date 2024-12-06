package by.zemich.userms.controller.dto.response;

import by.zemich.userms.dao.enums.Status;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
}

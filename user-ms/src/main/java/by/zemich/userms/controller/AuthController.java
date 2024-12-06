package by.zemich.userms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Operations related to authentication")
public class AuthController extends BaseRestController {

    @Operation(
            description = "login endpoint"
    )
    @PostMapping
    ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        return null;
    }

    @PostMapping("/activation")
    @Operation(
            description = "activate an user"
    )
    ResponseEntity<URI> activate(UUID userId){
        return null;
    }
}

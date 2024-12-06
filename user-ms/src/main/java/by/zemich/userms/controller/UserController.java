package by.zemich.userms.controller;

import by.zemich.userms.controller.dto.request.UserRequest;
import by.zemich.userms.controller.dto.response.UserResponse;
import by.zemich.userms.controller.mapper.UserMapper;
import by.zemich.userms.dao.entities.User;
import by.zemich.userms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Operations related to users")
public class UserController extends BaseRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            description = "register new user"
    )
    public ResponseEntity<URI> register(@RequestBody @Valid UserRequest userRequest) {
        User newUser = userMapper.toEntity(userRequest);
        User savedUser = userService.save(newUser);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(
            description = "get user by id"
    )
    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> get(@PathVariable(value = "user_id") UUID userId) {
        User foundedUser = userService.findById(userId);
        return ResponseEntity.ok(userMapper.toResponse(foundedUser));
    }

    @Operation(
            description = "get users page"
    )
    @GetMapping()
    public ResponseEntity<Page<UserResponse>> getAll(
            @RequestParam(name = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy
    ) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Direction.DESC, sortBy)
        );

        Page<User> requestedPage = userService.findAll(pageRequest);
        List<UserResponse> userResponses = requestedPage.get()
                .map(userMapper::toResponse)
                .toList();

        PageImpl<UserResponse> resultPage = new PageImpl<>(userResponses, pageRequest, requestedPage.getTotalElements());
        return ResponseEntity.ok(resultPage);
    }


}

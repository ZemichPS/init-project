package by.zemich.userms.controller.mapper;

import by.zemich.userms.controller.dto.request.UserRequest;
import by.zemich.userms.controller.dto.response.UserResponse;
import by.zemich.userms.dao.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}

package com.nihat.jekirdekcase.mappers;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.entities.AppRole;
import com.nihat.jekirdekcase.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    User mapToUser(CreateUserRequest createUserRequest);
    User mapToUser(UpdateUserRequest updateUserRequest);

    CreateUserResponse mapToCreateUserResponse(User user);
    UpdateUserResponse mapToUpdateUserResponse(User user);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToRoleNames")
    GetUserResponse mapToGetUserResponse(User user);

    List<GetUserResponse> mapToGetUserResponseList(Iterable<User> users);


    @Named("mapRolesToRoleNames")
    default List<String> mapRolesToRoleNames(Set<AppRole> roles) {
        return roles.stream()
                .map(AppRole::getRoleName)
                .toList();
    }
}

package com.nihat.jekirdekcase.mappers;

import com.nihat.jekirdekcase.dtos.requests.CreateUserRequest;
import com.nihat.jekirdekcase.dtos.requests.UpdateUserRequest;
import com.nihat.jekirdekcase.dtos.responses.CreateUserResponse;
import com.nihat.jekirdekcase.dtos.responses.GetUserResponse;
import com.nihat.jekirdekcase.dtos.responses.UpdateUserResponse;
import com.nihat.jekirdekcase.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User mapToUser(CreateUserRequest createUserRequest);
    User mapToUser(UpdateUserRequest updateUserRequest);

    CreateUserResponse mapToCreateUserResponse(User user);
    UpdateUserResponse mapToUpdateUserResponse(User user);
    GetUserResponse mapToGetUserResponse(User user);
    List<GetUserResponse> mapToGetUserResponseList(Iterable<User> users);
}

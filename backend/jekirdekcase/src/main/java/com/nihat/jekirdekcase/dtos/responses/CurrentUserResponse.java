package com.nihat.jekirdekcase.dtos.responses;

import java.util.Set;

public record CurrentUserResponse(String username, Set<String> authorities) {}
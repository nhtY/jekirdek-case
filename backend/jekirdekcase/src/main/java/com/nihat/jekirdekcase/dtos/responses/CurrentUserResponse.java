package com.nihat.jekirdekcase.dtos.responses;

import java.util.Collection;
import java.util.Set;

public record CurrentUserResponse(String username, Collection<String> authorities) {}
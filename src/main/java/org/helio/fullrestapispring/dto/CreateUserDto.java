package org.helio.fullrestapispring.dto;


import org.helio.fullrestapispring.enums.RoleName;

public record CreateUserDto(
        String email,
        String password,
        RoleName role
) {
}

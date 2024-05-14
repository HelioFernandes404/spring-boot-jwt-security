package org.helio.fullrestapispring.dto;



import org.helio.fullrestapispring.entities.Role;

import java.util.List;

public record RecoveryUserDto(

        Long id,

        String email,

        List<Role> roles
) {
}

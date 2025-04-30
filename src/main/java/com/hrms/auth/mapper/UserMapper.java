package com.hrms.auth.mapper;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() { }

    /* Convert incoming DTO + resolved Role entities → User entity */
    public static User toEntity(UserDto dto, Set<Role> roles) {
        User u = new User();
        u.setUserId(dto.getUserId());
        u.setEmployeeId(dto.getEmployeeId());
        u.setWorkEmail(dto.getWorkEmail());
        u.setFullName(dto.getFullName());
        u.setIsAdmin(dto.getIsAdmin());
        u.setRoles(roles);
        return u;
    }

    /* Convert entity → DTO for responses */
    public static UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setUserId(entity.getUserId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setWorkEmail(entity.getWorkEmail());
        dto.setFullName(entity.getFullName());
        dto.setIsAdmin(entity.getIsAdmin());
        dto.setRoles(
            entity.getRoles().stream()
                  .map(Role::getName)
                  .collect(Collectors.toSet())
        );
        return dto;
    }
}

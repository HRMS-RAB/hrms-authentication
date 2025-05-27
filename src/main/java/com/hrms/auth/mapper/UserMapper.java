package com.hrms.auth.mapper;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

/** Manual mapper: DTO ⇆ Entity, plus partialUpdate. */
public final class UserMapper {

    private UserMapper() {}

    /*── DTO ➜ Entity ──*/
    public static User toEntity(UserDto dto, Set<Role> roleEntities) {
        User u = new User();
        u.setUserId(dto.getUserId());
        u.setEmployeeId(dto.getEmployeeId());
        u.setDeptId(dto.getDeptId());
        u.setDeptName(dto.getDeptName());
        u.setFullName(dto.getFullName());
        u.setWorkEmail(dto.getWorkEmail());
        u.setGradeLevel(dto.getGradeLevel());
        u.setIsAdmin(dto.getIsAdmin() != null && dto.getIsAdmin());
        u.setActive(dto.getActive() == null || dto.getActive());
        u.setRoles(roleEntities);
        return u;
    }

    /*── Entity ➜ DTO ──*/
    public static UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setUserId(entity.getUserId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setDeptId(entity.getDeptId());
        dto.setDeptName(entity.getDeptName());
        dto.setFullName(entity.getFullName());
        dto.setWorkEmail(entity.getWorkEmail());
        dto.setGradeLevel(entity.getGradeLevel());
        dto.setIsAdmin(entity.getIsAdmin());
        dto.setActive(entity.getActive());
        if (entity.getRoles() != null) {
            dto.setRoles(entity.getRoles()
                               .stream()
                               .map(Role::getName)
                               .collect(Collectors.toSet()));
        }
        return dto;
    }

    /*── partialUpdate (admin API) ──*/
    public static void partialUpdate(User entity, UserDto dto) {
        if (dto.getFullName()    != null) entity.setFullName(dto.getFullName());
        if (dto.getWorkEmail()   != null) entity.setWorkEmail(dto.getWorkEmail());
        if (dto.getDeptId()      != null) entity.setDeptId(dto.getDeptId());
        if (dto.getDeptName()    != null) entity.setDeptName(dto.getDeptName());
        if (dto.getGradeLevel()  != null) entity.setGradeLevel(dto.getGradeLevel());
        if (dto.getActive()      != null) entity.setActive(dto.getActive());
        if (dto.getIsAdmin()     != null) entity.setIsAdmin(dto.getIsAdmin());
    }
}

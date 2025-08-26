package com.soumyadip_cy.journalApp.mapper;

import com.soumyadip_cy.journalApp.dto.UserCreateDTO;
import com.soumyadip_cy.journalApp.dto.UserDTO;
import com.soumyadip_cy.journalApp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
    UserCreateDTO toUserCreateDTO(User user);
    User toUser(UserCreateDTO userCreateDTO);
}

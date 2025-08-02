package com.ansysan.project.authentication_service.mapper;

import com.ansysan.project.authentication_service.dto.UserDto;
import com.ansysan.project.authentication_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}

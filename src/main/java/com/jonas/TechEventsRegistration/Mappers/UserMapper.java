package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserRegisterRequest;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserRegisterResponse;
import com.jonas.TechEventsRegistration.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity (UserRegisterRequest request);

    UserRegisterResponse toResponse (User user);
}

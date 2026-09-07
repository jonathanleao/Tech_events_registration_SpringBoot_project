package com.jonas.TechEventsRegistration.DTO.SecurityDTOs;

import com.jonas.TechEventsRegistration.Entity.UserRoles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private UserRoles role;
}

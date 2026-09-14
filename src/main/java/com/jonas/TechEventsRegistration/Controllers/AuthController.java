package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserLoginRequest;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserLoginResponse;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserRegisterRequest;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserRegisterResponse;
import com.jonas.TechEventsRegistration.SecurityServices.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServices authServices;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register (@Valid @RequestBody UserRegisterRequest request){
        return  new ResponseEntity<>(authServices.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login (@Valid @RequestBody UserLoginRequest request){
        return  new ResponseEntity<>(authServices.login(request), HttpStatus.CREATED);
    }
}

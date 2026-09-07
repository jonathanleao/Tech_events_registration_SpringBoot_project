package com.jonas.TechEventsRegistration.SecurityServices;

import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserLoginRequest;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserLoginResponse;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserRegisterRequest;
import com.jonas.TechEventsRegistration.DTO.SecurityDTOs.UserRegisterResponse;
import com.jonas.TechEventsRegistration.Entity.User;
import com.jonas.TechEventsRegistration.Exceptions.UserAlreadyExistsException;
import com.jonas.TechEventsRegistration.Mappers.UserMapper;
import com.jonas.TechEventsRegistration.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final UserRepository userRepository;

    private final JwtServices jwtServices;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    public UserRegisterResponse register (UserRegisterRequest request){
        if (userRepository.existsByLogin(request.getLogin())){
            throw new UserAlreadyExistsException("user with login " + request.getLogin() + " already exists");
        }

        User entity = userMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));

        User save = userRepository.save(entity);

        return userMapper.toResponse(save);
    }

    public UserLoginResponse login (UserLoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        Optional<User> userByLogin = userRepository.findUserByLogin(request.getLogin());

        String token = jwtServices.generateToken(userByLogin.get());

        return  new UserLoginResponse(token);
    }
}

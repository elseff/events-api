package ru.danila.eventsapi.web.api.modules.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.RoleEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.RoleRepository;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.security.JwtProvider;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthRegisterRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthResponse;
import ru.danila.eventsapi.web.api.modules.user.dto.assembler.UserDtoAssembler;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserDtoAssembler userDtoAssembler;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    JwtProvider jwtProvider;

    public AuthResponse register(AuthRegisterRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(userEntity -> {
            throw new IllegalArgumentException("Пользователь с таким ником уже существует");
        });

        boolean isOrganizer = request.isOrganizer();
        RoleEntity role = isOrganizer
                ? roleRepository.findByName("ROLE_ORGANIZER")
                : roleRepository.findByName("ROLE_USER");

        UserEntity user = userDtoAssembler.mapAuthRegisterRequestToUserEntity(request);
        user.getRoles().add(role);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        log.info("Пользователь {} успешно зарегистрировался", user.getUsername());
        String token = jwtProvider.generateToken(user.getUsername());

        return AuthResponse.builder()
                .registerAt(Timestamp.from(Instant.now()))
                .username(user.getUsername())
                .token(token)
                .role(role.getName())
                .build();
    }
}

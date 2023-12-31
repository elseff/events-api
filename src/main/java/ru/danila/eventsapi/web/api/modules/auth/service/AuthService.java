package ru.danila.eventsapi.web.api.modules.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.RoleEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.RoleRepository;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.security.JwtProvider;
import ru.danila.eventsapi.security.UserDetailsImpl;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthLoginRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthRegisterRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthResponse;
import ru.danila.eventsapi.web.api.modules.auth.dto.assembler.AuthDtoAssembler;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    AuthDtoAssembler authDtoAssembler;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    JwtProvider jwtProvider;

    @Transactional
    public AuthResponse register(AuthRegisterRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(userEntity -> {
            throw new IllegalArgumentException("Пользователь с таким ником уже существует");
        });

        boolean isOrganizer = request.isOrganizer();
        Optional<RoleEntity> roleOptional = isOrganizer
                ? roleRepository.findByName("ROLE_ORGANIZER")
                : roleRepository.findByName("ROLE_USER");

        if (roleOptional.isEmpty())
            throw new IllegalArgumentException("Не найденна роль с таким именем");

        RoleEntity role = roleOptional.get();

        UserEntity user = authDtoAssembler.mapAuthRegisterRequestToUserEntity(request);
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

    @Transactional
    public AuthResponse login(AuthLoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Пользователь с таким ником не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Неверный пароль!");

        String token = jwtProvider.generateToken(request.getUsername());

        return AuthResponse.builder()
                .username(request.getUsername())
                .role(user.getRoles().iterator().next().getName())
                .token(token)
                .build();
    }

    @Transactional
    public UserDetails getCurrentAuthUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

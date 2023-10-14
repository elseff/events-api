package ru.danila.eventsapi.web.api.modules.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.user.dto.UserResponse;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    AuthService authService;

    public Optional<UserEntity> findByUsername(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            log.warn("Пользователь c ником {} не найден", username);

        return userOptional;
    }

    public UserEntity getMe() {
        return userRepository.findByUsername(authService.getCurrentAuthUser().getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Косяк"));
    }
}

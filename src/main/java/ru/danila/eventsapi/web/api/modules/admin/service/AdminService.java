package ru.danila.eventsapi.web.api.modules.admin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.RoleEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.RoleRepository;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.web.api.modules.admin.dto.AdminChangeRoleRequest;
import ru.danila.eventsapi.web.api.modules.admin.dto.AdminResponse;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    UserService userService;

    UserRepository userRepository;

    RoleRepository roleRepository;

    public AdminResponse changeRole(AdminChangeRoleRequest request) {
        UserEntity user = userService.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Пользователь не найден"));

        RoleEntity role = roleRepository.findByName(request.getRoleName()).orElseThrow(
                () -> new IllegalArgumentException("Не найдена роль с таким именем"));

        user.getRoles().clear();
        user.getRoles().add(role);

        userRepository.save(user);

        return AdminResponse.builder()
                .date(Timestamp.from(Instant.now()))
                .message("Пользователю " + user.getUsername() + " дана роль " + request.getRoleName())
                .build();
    }
}

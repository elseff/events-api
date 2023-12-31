package ru.danila.eventsapi.web.api.modules.auth.dto.assembler;

import org.springframework.stereotype.Component;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthRegisterRequest;

import java.util.HashSet;

@Component
public class AuthDtoAssembler {

    public UserEntity mapAuthRegisterRequestToUserEntity(AuthRegisterRequest request) {
        return UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
                .roles(new HashSet<>())
                .build();
    }
}

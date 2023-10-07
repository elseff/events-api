package ru.danila.eventsapi.web.api.modules.user.dto.assembler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthRegisterRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthResponse;

import java.util.HashSet;

@Component
public class UserDtoAssembler {

    public UserEntity mapAuthRegisterRequestToUserEntity(AuthRegisterRequest request){
        return UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
                .roles(new HashSet<>())
                .build();
    }
}

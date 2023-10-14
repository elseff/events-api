package ru.danila.eventsapi.web.api.modules.user.dto.assemlber;

import org.springframework.stereotype.Component;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.web.api.modules.user.dto.UserResponse;

@Component
public class UserDtoAssembler {

    public UserResponse mapUserEntityToUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .role(userEntity.getRoles().iterator().next().getName())
                .build();
    }
}

package ru.danila.eventsapi.web.api.modules.admin.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminChangeRoleRequest {

    @NotNull(message = "Id пользователя не должно быть пустым")
    String username;

    @NotNull(message = "Имя роли не должно быть пустым")
    String roleName;
}

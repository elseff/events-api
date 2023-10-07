package ru.danila.eventsapi.web.api.modules.auth.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRegisterRequest {

    @NotNull(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер имени должен быть больше 2 и меньше 30 символов")
    String firstName;

    @NotNull(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 30, message = "Размер фамилии должен быть больше 2 и меньше 30 символов")
    String lastName;

    @NotNull(message = "Никнейм не должен быть пустым")
    @Size(min = 2, max = 30, message = "Размер никнейма должен быть больше 2 и меньше 30 символов")
    String username;

    @NotNull(message = "Пароль не должен быть пустым")
    @Size(min = 4, message = "Длина пароля должна быть больше 4-ех символов")
    String password;

    boolean isOrganizer;
}

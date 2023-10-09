package ru.danila.eventsapi.web.api.modules.place.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaceCreationRequest {

    @NotNull(message = "Поле адрес не должно быть пустым")
    @Size(min = 10, message = "Размер поля адреса не должен быть меньше 10 символов")
    String address;

    @NotNull(message = "Поле вместимость не должно быть пустым")
    @Positive(message = "Вместимость должна быть больше 0")
    Long capacity;
}

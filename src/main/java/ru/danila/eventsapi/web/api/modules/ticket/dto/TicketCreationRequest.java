package ru.danila.eventsapi.web.api.modules.ticket.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketCreationRequest {

    @NotNull(message = "Имя события не должно быть пустым")
    String eventTitle;
}

package ru.danila.eventsapi.web.api.modules.ticket.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.danila.eventsapi.web.api.modules.event.dto.EventResponse;
import ru.danila.eventsapi.web.api.modules.user.dto.UserResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketCreationResponse {
    EventResponse event;

    UserResponse user;
}

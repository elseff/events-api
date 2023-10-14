package ru.danila.eventsapi.web.api.modules.ticket.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.danila.eventsapi.web.api.modules.event.dto.EventResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {

    EventResponse event;
}

package ru.danila.eventsapi.web.api.modules.ticket.dto.assembler;

import org.springframework.stereotype.Component;
import ru.danila.eventsapi.persistense.TicketEntity;
import ru.danila.eventsapi.web.api.modules.event.dto.EventResponse;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationResponse;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketResponse;
import ru.danila.eventsapi.web.api.modules.user.dto.UserResponse;

@Component
public class TicketDtoAssembler {

    public TicketCreationResponse mapTicketEntityToTicketCreationResponse(TicketEntity entity) {
        return TicketCreationResponse.builder()
                .event(EventResponse.builder()
                        .placeAddress(entity.getEvent().getPlace().getAddress())
                        .title(entity.getEvent().getTitle())
                        .freeSeats(entity.getEvent().getFreeSeats())
                        .date(entity.getEvent().getDate())
                        .build())
                .user(UserResponse.builder()
                        .firstName(entity.getUser().getFirstName())
                        .lastName(entity.getUser().getLastName())
                        .build())
                .build();
    }

    public TicketResponse mapTicketEntityToTicketResponse(TicketEntity entity) {
        return TicketResponse.builder()
                .event(EventResponse.builder()
                        .date(entity.getEvent().getDate())
                        .freeSeats(entity.getEvent().getFreeSeats())
                        .title(entity.getEvent().getTitle())
                        .placeAddress(entity.getEvent().getPlace().getAddress())
                        .build())
                .build();
    }
}

package ru.danila.eventsapi.web.api.modules.event.dto.assembler;

import org.springframework.stereotype.Component;
import ru.danila.eventsapi.persistense.EventEntity;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationRequest;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationResponse;
import ru.danila.eventsapi.web.api.modules.event.dto.EventResponse;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class EventDtoAssembler {
    public EventEntity mapEventCreationRequestToEventEntity(EventCreationRequest request) {
        return EventEntity.builder()
                .title(request.getTitle())
                .date(request.getDate())
                .build();
    }

    public EventCreationResponse mapEventEntityToEventCreationResponse(EventEntity event) {
        return EventCreationResponse.builder()
                .placeAddress(event.getPlace().getAddress())
                .title(event.getTitle())
                .registerAt(Timestamp.from(Instant.now()))
                .build();
    }

    public EventResponse mapEventEntityToEventResponse(EventEntity eventEntity) {
        return EventResponse.builder()
                .title(eventEntity.getTitle())
                .placeAddress(eventEntity.getPlace().getAddress())
                .date(eventEntity.getDate())
                .freeSeats(eventEntity.getFreeSeats())
                .build();
    }
}

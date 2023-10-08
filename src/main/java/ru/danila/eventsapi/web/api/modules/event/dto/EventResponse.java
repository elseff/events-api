package ru.danila.eventsapi.web.api.modules.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {

    String title;

    String placeAddress;

    Timestamp date;

    Long freeSeats;
}

package ru.danila.eventsapi.web.api.modules.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreationRequest {

    String title;

    @JsonFormat(pattern = "yyyy-MM-dd hh-mm")
    Timestamp date;

    String placeAddress;
}

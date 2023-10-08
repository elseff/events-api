package ru.danila.eventsapi.web.api.modules.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.danila.eventsapi.persistense.EventEntity;
import ru.danila.eventsapi.web.api.modules.event.assembler.EventDtoAssembler;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationRequest;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationResponse;
import ru.danila.eventsapi.web.api.modules.event.dto.EventResponse;
import ru.danila.eventsapi.web.api.modules.event.service.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    EventService eventService;

    EventDtoAssembler eventDtoAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventCreationResponse registerEvent(@RequestBody @Valid EventCreationRequest request) {
        EventEntity event = eventService.addEvent(request);

        return eventDtoAssembler.mapEventEntityToEventCreationResponse(event);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponse> findAll() {
        return eventService
                .findAll()
                .stream()
                .map(eventDtoAssembler::mapEventEntityToEventResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{title}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponse findByTitle(@PathVariable String title) {
        EventEntity event = eventService.findByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("Нет события с таким названием"));

        return eventDtoAssembler.mapEventEntityToEventResponse(event);
    }

    @DeleteMapping("/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String title) {
        eventService.deleteByTitle(title);
    }
}

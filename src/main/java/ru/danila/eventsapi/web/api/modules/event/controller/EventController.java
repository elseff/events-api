package ru.danila.eventsapi.web.api.modules.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.danila.eventsapi.persistense.EventEntity;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationRequest;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationResponse;
import ru.danila.eventsapi.web.api.modules.event.dto.EventResponse;
import ru.danila.eventsapi.web.api.modules.event.dto.assembler.EventDtoAssembler;
import ru.danila.eventsapi.web.api.modules.event.service.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Event controller", description = "Управление событиями")
public class EventController {

    EventService eventService;

    EventDtoAssembler eventDtoAssembler;

    @Operation(
            method = "POST",
            summary = "Зарегистрировать событие",
            description = "Зарегистрировать событие в системе",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Событие успешно зарегистрировано",
                            content = @Content(schema = @Schema(implementation = EventCreationResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные данные для регистрации события", content = @Content),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    public EventCreationResponse registerEvent(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Необходимые параметры для добавления события",
            required = true,
            content = @Content(schema = @Schema(implementation = EventCreationRequest.class)))
                                               @RequestBody @Valid EventCreationRequest request) {
        EventEntity event = eventService.addEvent(request);

        return eventDtoAssembler.mapEventEntityToEventCreationResponse(event);
    }

    @Operation(
            method = "GET",
            summary = "Список событий",
            description = "Получить список всех событий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно найдены все события",
                            content = @Content(schema = @Schema(implementation = EventResponse[].class))
                    ),
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponse> findAll() {
        return eventService
                .findAll()
                .stream()
                .map(eventDtoAssembler::mapEventEntityToEventResponse)
                .collect(Collectors.toList());
    }

    @Operation(
            method = "GET",
            summary = "Найти событие",
            description = "Найти событие по названию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Событие успешно найдено",
                            content = @Content(schema = @Schema(implementation = EventResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено", content = @Content),
            }
    )
    @GetMapping("/{title}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponse findByTitle(@Parameter(description = "Название события", required = true)
                                     @PathVariable String title) {
        EventEntity event = eventService.findByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("Нет события с таким названием"));

        return eventDtoAssembler.mapEventEntityToEventResponse(event);
    }

    @Operation(
            method = "DELETE",
            summary = "Удалить событие",
            description = "Удалить событие по названию",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Событие успешно удалено"
                    ),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено", content = @Content),
            }
    )
    @DeleteMapping("/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteById(@Parameter(description = "Название события", required = true)
                           @PathVariable String title) {
        eventService.deleteByTitle(title);
    }
}

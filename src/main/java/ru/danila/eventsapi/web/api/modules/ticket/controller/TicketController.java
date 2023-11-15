package ru.danila.eventsapi.web.api.modules.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceResponse;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationRequest;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationResponse;
import ru.danila.eventsapi.web.api.modules.ticket.dto.assembler.TicketDtoAssembler;
import ru.danila.eventsapi.web.api.modules.ticket.service.TicketService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ticket controller", description = "Управление билетами")
public class TicketController {

    TicketService ticketService;

    TicketDtoAssembler ticketDtoAssembler;

    @Operation(
            method = "POST",
            summary = "Купить билет",
            description = "Купить билет на конкретное событие",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успешно куплен билет на событие",
                            content = @Content(schema = @Schema(implementation = PlaceResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Указанное событие не найдено", content = @Content),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketCreationResponse buyTicket(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Необходимые параметры для покупки билета",
            required = true,
            content = @Content(schema = @Schema(implementation = TicketCreationRequest.class)))
                                            @RequestBody @Valid TicketCreationRequest request) {
        return ticketDtoAssembler.mapTicketEntityToTicketCreationResponse(ticketService.buyTicket(request));
    }
}

package ru.danila.eventsapi.web.api.modules.ticket.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationRequest;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationResponse;
import ru.danila.eventsapi.web.api.modules.ticket.dto.assembler.TickerDtoAssembler;
import ru.danila.eventsapi.web.api.modules.ticket.service.TicketService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {

    TicketService ticketService;

    TickerDtoAssembler tickerDtoAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketCreationResponse buyTicket(@RequestBody @Valid TicketCreationRequest request) {
        return tickerDtoAssembler.mapTicketEntityToTicketCreationResponse(ticketService.buyTicket(request));
    }
}

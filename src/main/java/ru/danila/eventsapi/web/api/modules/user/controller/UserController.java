package ru.danila.eventsapi.web.api.modules.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.danila.eventsapi.persistense.TicketEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketResponse;
import ru.danila.eventsapi.web.api.modules.ticket.dto.assembler.TicketDtoAssembler;
import ru.danila.eventsapi.web.api.modules.ticket.service.TicketService;
import ru.danila.eventsapi.web.api.modules.user.dto.UserResponse;
import ru.danila.eventsapi.web.api.modules.user.dto.assemlber.UserDtoAssembler;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    UserDtoAssembler userDtoAssembler;

    TicketService ticketService;
    
    AuthService authService;
    
    TicketDtoAssembler ticketDtoAssembler;
    
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getMe() {
        return userDtoAssembler.mapUserEntityToUserResponse(userService.getMe());
    }

    @GetMapping("/me/tickets")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketResponse> findPurchasedTickets(){
        UserEntity user = userService.findByUsername(authService.getCurrentAuthUser().getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Косяк"));
        
        List<TicketEntity> tickets = ticketService.findTicketsByUser(user);
        return tickets
                .stream()
                .map(ticketDtoAssembler::mapTicketEntityToTicketResponse)
                .collect(Collectors.toList());
    }
}

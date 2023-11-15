package ru.danila.eventsapi.web.api.modules.user.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@SecurityRequirement(name = "Bearer Authentication")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "UserController controller", description = "Управление пользователем")
public class UserController {

    UserService userService;

    UserDtoAssembler userDtoAssembler;

    TicketService ticketService;

    AuthService authService;

    TicketDtoAssembler ticketDtoAssembler;

    @Operation(
            method = "GET",
            summary = "Профиль пользователя",
            description = "Профиль текущего пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getMe() {
        return userDtoAssembler.mapUserEntityToUserResponse(userService.getMe());
    }

    @Operation(
            method = "GET",
            summary = "Приобритенные билеты",
            description = "Приобритенные билеты пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно найдены приобритенные билеты",
                            content = @Content(
                                    schema = @Schema(implementation = TicketResponse[].class)
                            )
                    )
            }
    )
    @GetMapping("/me/tickets")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketResponse> findPurchasedTickets() {
        UserEntity user = userService.findByUsername(authService.getCurrentAuthUser().getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Косяк"));

        List<TicketEntity> tickets = ticketService.findTicketsByUser(user);
        return tickets
                .stream()
                .map(ticketDtoAssembler::mapTicketEntityToTicketResponse)
                .collect(Collectors.toList());
    }
}

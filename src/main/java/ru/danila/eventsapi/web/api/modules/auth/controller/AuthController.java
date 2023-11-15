package ru.danila.eventsapi.web.api.modules.auth.controller;

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
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthLoginRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthRegisterRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthResponse;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication controller", description = "Управление авторизацией и аутентификацией")
public class AuthController {

    AuthService authService;

    @Operation(
            method = "POST",
            summary = "Регистрация",
            description = "Зарегистрировать новый аккаунт. Выдача токена авторизации",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно зарегистрирован",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные пользовательские данные", content = @Content)
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Необходимые параметры для регистрации пользователя",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRegisterRequest.class)))
                                 @RequestBody @Valid AuthRegisterRequest request) {
        return authService.register(request);
    }

    @Operation(
            method = "POST",
            summary = "Войти",
            description = "Вход в аккаунт. Выдача токена авторизации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные пользовательские данные", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
            }
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Необходимые параметры для получения токена пользователя",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthLoginRequest.class)))
                              @RequestBody @Valid AuthLoginRequest request) {
        return authService.login(request);
    }
}

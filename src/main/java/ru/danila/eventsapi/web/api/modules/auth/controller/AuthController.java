package ru.danila.eventsapi.web.api.modules.auth.controller;

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
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody @Valid AuthRegisterRequest request){
        return authService.register(request);
    }

    public AuthResponse login(@RequestBody @Valid AuthLoginRequest request){
//        return authService.login(request);
        return new AuthResponse();
    }
}

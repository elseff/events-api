package ru.danila.eventsapi.web.api.modules.place.controller;

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
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceCreationRequest;
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceResponse;
import ru.danila.eventsapi.web.api.modules.place.dto.assembler.PlaceDtoAssembler;
import ru.danila.eventsapi.web.api.modules.place.service.PlaceService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Place controller", description = "Управление местами проведения событий")
public class PlaceController {

    PlaceService placeService;

    PlaceDtoAssembler placeDtoAssembler;

    @Operation(
            method = "GET",
            summary = "Список мест",
            description = "Получить список всех мест проведения событий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно найдены все места проведения событий",
                            content = @Content(schema = @Schema(implementation = PlaceResponse[].class))
                    ),
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceResponse> findAll() {
        return placeService
                .findAll()
                .stream()
                .map(placeDtoAssembler::mapPlaceEntityToPlaceResponse)
                .collect(Collectors.toList());
    }

    @Operation(
            method = "POST",
            summary = "Зарегистрировать место",
            description = "Зарегистрировать место проведения события в системе",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Место проведения события успешно зарегистрировано",
                            content = @Content(schema = @Schema(implementation = PlaceResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные данные для регистрации места проведения", content = @Content),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    public PlaceResponse addPlace(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Необходимые параметры для добавления места проведения события",
            required = true,
            content = @Content(schema = @Schema(implementation = PlaceCreationRequest.class)))
                                  @RequestBody @Valid PlaceCreationRequest request) {
        return placeDtoAssembler.mapPlaceEntityToPlaceResponse(placeService.addPlace(request));
    }

    @Operation(
            method = "DELETE",
            summary = "Удалить место",
            description = "Удалить место проведения события по id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Событие успешно удалено"
                    ),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено", content = @Content),
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteById(@Parameter(description = "Id места проведения события", required = true)
                           @PathVariable Long id) {
        placeService.deleteById(id);
    }
}

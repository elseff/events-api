package ru.danila.eventsapi.web.api.modules.place.controller;

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
public class PlaceController {

    PlaceService placeService;

    PlaceDtoAssembler placeDtoAssembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceResponse> findAll() {
        return placeService
                .findAll()
                .stream()
                .map(placeDtoAssembler::mapPlaceEntityToPlaceResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceResponse addPlace(@RequestBody @Valid PlaceCreationRequest request) {
        return placeDtoAssembler.mapPlaceEntityToPlaceResponse(placeService.addPlace(request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        placeService.deleteById(id);
    }
}

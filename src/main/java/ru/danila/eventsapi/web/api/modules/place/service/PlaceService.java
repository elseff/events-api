package ru.danila.eventsapi.web.api.modules.place.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.PlaceEntity;
import ru.danila.eventsapi.persistense.dao.PlaceRepository;
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceCreationRequest;
import ru.danila.eventsapi.web.api.modules.place.dto.assembler.PlaceDtoAssembler;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaceService {

    PlaceRepository placeRepository;

    PlaceDtoAssembler placeDtoAssembler;

    @Transactional
    public List<PlaceEntity> findAll() {
        return placeRepository.findAll();
    }

    @Transactional
    public PlaceEntity addPlace(PlaceCreationRequest request) {
        PlaceEntity place = placeDtoAssembler.mapPlaceCreationRequestToPlaceEntity(request);

        return placeRepository.save(place);
    }

    @Transactional
    public void deleteById(Long id) {
        PlaceEntity place = placeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Место с таким id не найдено"));

        placeRepository.delete(place);
    }
}

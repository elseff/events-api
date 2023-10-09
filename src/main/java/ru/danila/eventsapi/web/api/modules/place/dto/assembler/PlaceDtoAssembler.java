package ru.danila.eventsapi.web.api.modules.place.dto.assembler;

import org.springframework.stereotype.Component;
import ru.danila.eventsapi.persistense.PlaceEntity;
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceCreationRequest;
import ru.danila.eventsapi.web.api.modules.place.dto.PlaceResponse;

@Component
public class PlaceDtoAssembler {

    public PlaceResponse mapPlaceEntityToPlaceResponse(PlaceEntity placeEntity) {
        return PlaceResponse.builder()
                .address(placeEntity.getAddress())
                .capacity(placeEntity.getCapacity())
                .build();
    }

    public PlaceEntity mapPlaceCreationRequestToPlaceEntity(PlaceCreationRequest request) {
        return PlaceEntity.builder()
                .address(request.getAddress())
                .capacity(request.getCapacity())
                .build();
    }
}
